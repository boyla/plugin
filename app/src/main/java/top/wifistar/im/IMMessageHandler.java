package top.wifistar.im;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.newim.notification.BmobNotificationManager;

import top.wifistar.R;
import top.wifistar.activity.ChatActivity;
import top.wifistar.activity.HomeActivity;
import top.wifistar.activity.SplashActivity;
import top.wifistar.app.BaseActivity;
import top.wifistar.bean.bmob.User;
import top.wifistar.chain.user.NetUserRequest;
import top.wifistar.event.RefreshEvent;
import top.wifistar.realm.BaseRealmDao;
import top.wifistar.realm.IMUserRealm;
import top.wifistar.utils.EventUtils;
import top.wifistar.utils.Utils;

/**
 * Created by boyla on 2018/5/3.
 */

public class IMMessageHandler extends BmobIMMessageHandler {

    private Context context;

    public IMMessageHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onMessageReceive(final MessageEvent event) {
        //当接收到服务器发来的消息时，此方法被调用
        executeMessage(event);
    }

    @Override
    public void onOfflineReceive(final OfflineMessageEvent event) {
        //每次调用connect方法时会查询一次离线消息，如果有，此方法会被调用
        Map<String, List<MessageEvent>> map = event.getEventMap();
        Log.i("Bmob IM:  ", "有" + map.size() + "个用户发来离线消息");
        //挨个检测下离线消息所属的用户的信息是否需要更新
        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
            List<MessageEvent> list = entry.getValue();
            int size = list.size();
            Log.i("Bmob IM:  ", "用户" + entry.getKey() + "发来" + size + "条消息");
            for (int i = 0; i < size; i++) {
                //处理每条消息
                if (BaseRealmDao.realm != null) {
                    executeMessage(list.get(i));
                }
            }
        }
    }

    /**
     * 处理消息
     *
     * @param event
     */
    private void executeMessage(final MessageEvent event) {
        final IMUserRealm rawUser = BaseRealmDao.realm.where(IMUserRealm.class).equalTo("objectId", event.getFromUserInfo().getUserId()).findFirst();
        if (rawUser == null || TextUtils.isEmpty(rawUser.name)) {
            //获取用户信息
            Utils.queryShortUser(event.getFromUserInfo().getUserId(), new NetUserRequest.NetRequestCallBack() {
                @Override
                public void onSuccess(User user) {
                    handleMsg(user.toIMRealm(), event, false);
                }

                @Override
                public void onFailure(String msg) {
                    handleMsg(null, event, false);
                }
            });
        } else {
            handleMsg(rawUser, event, true);
        }
    }

    private void handleMsg(IMUserRealm rawUser, MessageEvent event, boolean needParse) {
        IMUserRealm copyUser = rawUser == null ? new IMUserRealm() : rawUser;
        if (needParse && rawUser != null) {
            copyUser = rawUser.toBmobObject().toIMRealm();
        }
        //不使用IM提供的用户信息，仅仅使用ID来查询BMOB
//        copyUser.objectId = event.getFromUserInfo().getUserId();
//        copyUser.name = event.getFromUserInfo().getName();
//        copyUser.headUrl = event.getFromUserInfo().getAvatar();

        copyUser.unReadNum = rawUser == null ? 1 : rawUser.unReadNum + 1;
        if (BaseActivity.currentActivity instanceof ChatActivity) {
            copyUser.unReadNum = 0;
        }
        copyUser.isInConversation = true;
        copyUser.updateTime = System.currentTimeMillis();
        String lastMsg;
        if (event.getMessage().getMsgType().equals(BmobIMMessageType.TEXT.getType())) {
            lastMsg = event.getMessage().getContent();
        } else if (event.getMessage().getMsgType().equals(BmobIMMessageType.IMAGE.getType())) {
            lastMsg = "[图片]";
        } else if (event.getMessage().getMsgType().equals(BmobIMMessageType.VOICE.getType())) {
            lastMsg = "[语音]";
        } else if (event.getMessage().getMsgType().equals(BmobIMMessageType.LOCATION.getType())) {
            lastMsg = "[位置]" + event.getMessage().getContent();
        } else {//开发者自定义的消息类型，需要自行处理
            lastMsg = "[未知]";
        }
        copyUser.lastMsg = lastMsg;

//        Realm realm = Realm.getDefaultInstance();
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//
//            }
//        });

        BaseRealmDao.insertOrUpdate(copyUser);
        EventUtils.post(copyUser);

        BmobIMConversation conversation = event.getConversation();

        //TODO 用户管理：2.7、更新用户资料，用于在会话页面、聊天页面以及个人信息页面显示
        BmobIM.getInstance().updateUserInfo(event.getFromUserInfo());
        //TODO 会话：4.7、更新会话资料-如果消息是暂态消息，则不更新会话资料
        BmobIMMessage msg = event.getMessage();
        if (!msg.isTransient()) {
            BmobIM.getInstance().updateConversation(conversation);
        }
        if (BmobIMMessageType.getMessageTypeValue(msg.getMsgType()) == 0) {
            //自定义消息类型：0
            processCustomMessage(msg, event.getFromUserInfo());
        } else {
            //SDK内部内部支持的消息类型
            processSDKMessage(msg, event);
        }
    }

    /**
     * 处理SDK支持的消息
     *
     * @param msg
     * @param event
     */
    private void processSDKMessage(BmobIMMessage msg, MessageEvent event) {
        if (BmobNotificationManager.getInstance(context).isShowNotification()) {
            //如果需要显示通知栏，SDK提供以下两种显示方式：
            Intent pendingIntent;
            if (HomeActivity.INSTANCE == null) {
                pendingIntent = new Intent(context, SplashActivity.class);
            } else {
                pendingIntent = new Intent(context, HomeActivity.class);
            }
            pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            //TODO 消息接收：8.5、多个用户的多条消息合并成一条通知：有XX个联系人发来了XX条消息
            //BmobNotificationManager.getInstance(context).showNotification(event, pendingIntent);

            //TODO 消息接收：8.6、自定义通知消息：始终只有一条通知，新消息覆盖旧消息
            BmobIMUserInfo info = event.getFromUserInfo();
            Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            Utils.queryShortUser(info.getUserId(), new NetUserRequest.NetRequestCallBack() {
                @Override
                public void onSuccess(User user) {
                    if (TextUtils.isEmpty(user.headUrl) || TextUtils.isEmpty(user.headUrl.split("_")[0])) {
                        if (HomeActivity.INSTANCE == null) {
                            BmobNotificationManager.getInstance(context).showNotification(largeIcon,
                                    info.getName(), msg.getContent(), "您有一条新消息", pendingIntent);
                            return;
                        }
                        Utils.showGlobalNotify(HomeActivity.INSTANCE, 0, new BitmapDrawable(largeIcon), info.getName(), msg.getContent(), pendingIntent);
                        return;
                    }
                    Utils.getDrawableByUrl(user.headUrl.split("_")[0], context, new Utils.GetDrawableCallBack() {
                        @Override
                        public void onFinish(Bitmap result) {
                            if (HomeActivity.INSTANCE == null) {
                                BmobNotificationManager.getInstance(context).showNotification(result,
                                        info.getName(), msg.getContent(), "您有一条新消息", pendingIntent);
                                return;
                            }
                            Utils.showGlobalNotify(HomeActivity.INSTANCE, 0, new BitmapDrawable(result), info.getName(), msg.getContent(), pendingIntent);
                        }
                    });

                }

                @Override
                public void onFailure(String fMsg) {
//                    BmobNotificationManager.getInstance(context).showNotification(largeIcon,
//                            info.getName(), msg.getContent(), "您有一条新消息", pendingIntent);
                    if (HomeActivity.INSTANCE == null) {
                        BmobNotificationManager.getInstance(context).showNotification(largeIcon,
                                info.getName(), msg.getContent(), "您有一条新消息", pendingIntent);
                        return;
                    }
                    Utils.showGlobalNotify(HomeActivity.INSTANCE, 0, new BitmapDrawable(largeIcon), info.getName(), msg.getContent(), pendingIntent);
                }
            });
            //这里可以是应用图标，也可以将聊天头像转成bitmap

        } else {
            //直接发送消息事件
            EventBus.getDefault().post(event);
        }
    }

    /**
     * 处理自定义消息类型
     *
     * @param msg
     */
    private void processCustomMessage(BmobIMMessage msg, BmobIMUserInfo info) {
        //消息类型
        String type = msg.getMsgType();
        //发送页面刷新的广播
        EventBus.getDefault().post(new RefreshEvent());
        //处理消息
//        if (type.equals(AddFriendMessage.ADD)) {//接收到的添加好友的请求
//            NewFriend friend = AddFriendMessage.convert(msg);
//            //本地好友请求表做下校验，本地没有的才允许显示通知栏--有可能离线消息会有些重复
//            long id = NewFriendManager.getInstance(context).insertOrUpdateNewFriend(friend);
//            if (id > 0) {
//                showAddNotify(friend);
//            }
//        } else if (type.equals(AgreeAddFriendMessage.AGREE)) {//接收到的对方同意添加自己为好友,此时需要做的事情：1、添加对方为好友，2、显示通知
//            AgreeAddFriendMessage agree = AgreeAddFriendMessage.convert(msg);
//            addFriend(agree.getFromId());//添加消息的发送方为好友
//            //这里应该也需要做下校验--来检测下是否已经同意过该好友请求，我这里省略了
//            showAgreeNotify(info, agree);
//        } else {
//            Toast.makeText(context, "接收到的自定义消息：" + msg.getMsgType() + "," + msg.getContent() + "," + msg.getExtra(), Toast.LENGTH_SHORT).show();
//        }
    }

    /**
     * 显示对方添加自己为好友的通知
     *
     * @param friend
     */
//    private void showAddNotify(NewFriend friend) {
//        Intent pendingIntent = new Intent(context, HomeActivity.class);
//        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        //这里可以是应用图标，也可以将聊天头像转成bitmap
//        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
//        BmobNotificationManager.getInstance(context).showNotification(largeIcon,
//                friend.getName(), friend.getMsg(), friend.getName() + "请求添加你为朋友", pendingIntent);
//    }

    /**
     * 显示对方同意添加自己为好友的通知
     *
     * @param info
     * @param agree
     */
//    private void showAgreeNotify(BmobIMUserInfo info, AgreeAddFriendMessage agree) {
//        Intent pendingIntent = new Intent(context, HomeActivity.class);
//        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
//        BmobNotificationManager.getInstance(context).showNotification(largeIcon, info.getName(), agree.getMsg(), agree.getMsg(), pendingIntent);
//    }

    /**
     * TODO 好友管理：9.11、收到同意添加好友后添加好友
     *
     * @param uid
     */
    private void addFriend(String uid) {
        User user = new User();
        user.setObjectId(uid);
//        UserModel.getInstance()
//                .agreeAddFriend(user, new SaveListener<String>() {
//                    @Override
//                    public void done(String s, BmobException e) {
//                        if (e == null) {
//                            Log.e("Bmob IM:  ", "success");
//                        } else {
//                            Log.e("Bmob IM:  ", e.getMessage());
//                        }
//                    }
//                });
    }
}
