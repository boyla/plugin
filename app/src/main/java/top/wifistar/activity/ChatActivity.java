package top.wifistar.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.greysonparrelli.permiso.Permiso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMFileMessage;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMLocationMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMVideoMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.BmobRecordManager;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.newim.listener.OnRecordChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.exception.BmobException;
import top.wifistar.R;
import top.wifistar.adapter.ChatMsgAdapter;
import top.wifistar.adapter.viewholder.im.OnRecyclerViewListener;
import top.wifistar.app.ToolbarActivity;
import top.wifistar.bean.bmob.User;
import top.wifistar.im.IMUtils;
import top.wifistar.realm.BaseRealmDao;
import top.wifistar.realm.IMUserRealm;
import top.wifistar.utils.EventUtils;
import top.wifistar.utils.Utils;

/**
 * 聊天界面
 *
 * @author :smile
 * @project:ChatActivity
 * @date :2016-01-25-18:23
 */
public class ChatActivity extends ToolbarActivity implements MessageListHandler {

    LinearLayout ll_chat;
    SwipeRefreshLayout sw_refresh;
    RecyclerView rc_view;
    EditText edit_msg;
    Button btn_chat_add;
    Button btn_chat_emo;
    Button btn_speak;
    Button btn_chat_voice;
    Button btn_chat_keyboard;
    Button btn_chat_send;
    LinearLayout layout_more;
    LinearLayout layout_add;
    LinearLayout layout_emo;
    // 语音有关
    RelativeLayout layout_record;
    TextView tv_voice_tips;
    ImageView iv_record;
    private Drawable[] drawable_Anims;// 话筒动画
    BmobRecordManager recordManager;

    ChatMsgAdapter adapter;
    protected LinearLayoutManager layoutManager;
    BmobIMConversation mConversationManager;
    User shortUser;
    boolean isFromProfile;

    @Override
    protected void initUI() {
        super.setContentView(R.layout.activity_chat);
        ll_chat = bindViewById(R.id.ll_chat);
        sw_refresh = bindViewById(R.id.sw_refresh);
        rc_view = bindViewById(R.id.rc_view);
        edit_msg = bindViewById(R.id.edit_msg);
        btn_chat_add = bindViewById(R.id.btn_chat_add);
        btn_chat_emo = bindViewById(R.id.btn_chat_emo);
        btn_speak = bindViewById(R.id.btn_speak);
        btn_chat_voice = bindViewById(R.id.btn_chat_voice);
        btn_chat_keyboard = bindViewById(R.id.btn_chat_keyboard);
        btn_chat_send = bindViewById(R.id.btn_chat_send);
        layout_more = bindViewById(R.id.layout_more);
        layout_add = bindViewById(R.id.layout_add);
        ll_chat = bindViewById(R.id.ll_chat);
        ll_chat = bindViewById(R.id.ll_chat);
        ll_chat = bindViewById(R.id.ll_chat);
        ll_chat = bindViewById(R.id.ll_chat);
        layout_emo = bindViewById(R.id.layout_emo);
        BmobIMConversation conversationEntrance = (BmobIMConversation) getIntent().getExtras().getSerializable("c");
        shortUser = (User) getIntent().getExtras().getSerializable("ShortUser");
        isFromProfile = getIntent().getExtras().getBoolean("isFromProfile");
        setCenterTitle(shortUser.getName());
        //TODO 消息：5.1、根据会话入口获取消息管理，聊天页面
        mConversationManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);
        initSwipeLayout();
        initVoiceView();
        initBottomView();
        setClickListener();
    }


    @Override
    protected void initTopBar() {
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.setting) {
                    Utils.makeSysToast("Settings");
                }
                return true;
            }
        });
    }

    private void initSwipeLayout() {
        sw_refresh.setEnabled(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rc_view.setLayoutManager(layoutManager);
        adapter = new ChatMsgAdapter(this, mConversationManager);
        rc_view.setAdapter(adapter);
        rc_view.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                rc_view.postDelayed(() -> rc_view.scrollToPosition(adapter.getCount() - 1), 100);
            }
        });
        ll_chat.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_chat.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                sw_refresh.setRefreshing(true);
                //自动刷新
                queryMessages(null);
            }
        });
        //下拉加载
        sw_refresh.setOnRefreshListener(() -> {
            BmobIMMessage msg = adapter.getFirstMessage();
            queryMessages(msg);
        });
        //设置RecyclerView的点击事件
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {}

            @Override
            public boolean onItemLongClick(int position) {
                Utils.showSimpleDialog(ChatActivity.this, "要删除该消息吗？", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO 消息：5.3、删除指定聊天消息
                        mConversationManager.deleteMessage(adapter.getItem(position));
                        adapter.remove(position);
                    }});
                return true;
            }
        });
    }

    private void initBottomView() {
        edit_msg.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
                scrollToBottom();
            }
            return false;
        });
        edit_msg.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                scrollToBottom();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    btn_chat_send.setVisibility(View.VISIBLE);
                    btn_chat_keyboard.setVisibility(View.GONE);
                    btn_chat_voice.setVisibility(View.GONE);
                } else {
                    if (btn_chat_voice.getVisibility() != View.VISIBLE) {
                        btn_chat_voice.setVisibility(View.VISIBLE);
                        btn_chat_send.setVisibility(View.GONE);
                        btn_chat_keyboard.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 初始化语音布局
     *
     * @param
     * @return void
     */
    private void initVoiceView() {
        btn_speak.setOnTouchListener(new VoiceTouchListener());
        initVoiceAnimRes();
        initRecordManager();
    }

    public User getCurrentUser() {
        return shortUser;
    }

    /**
     * 初始化语音动画资源
     *
     * @param
     * @return void
     * @Title: initVoiceAnimRes
     */
    private void initVoiceAnimRes() {
        drawable_Anims = new Drawable[]{
                getResources().getDrawable(R.drawable.chat_icon_voice2),
                getResources().getDrawable(R.drawable.chat_icon_voice3),
                getResources().getDrawable(R.drawable.chat_icon_voice4),
                getResources().getDrawable(R.drawable.chat_icon_voice5),
                getResources().getDrawable(R.drawable.chat_icon_voice6)};
    }

    private void initRecordManager() {
        // 语音相关管理器
        recordManager = BmobRecordManager.getInstance(this);
        // 设置音量大小监听--在这里开发者可以自己实现：当剩余10秒情况下的给用户的提示，类似微信的语音那样
        recordManager.setOnRecordChangeListener(new OnRecordChangeListener() {


            @Override
            public void onVolumeChanged(int value) {
                iv_record.setImageDrawable(drawable_Anims[value]);

            }

            @Override
            public void onTimeChanged(int recordTime, String localPath) {
//                Logger.i("voice", "已录音长度:" + recordTime);
                if (recordTime >= BmobRecordManager.MAX_RECORD_TIME) {// 1分钟结束，发送消息
                    // 需要重置按钮
                    btn_speak.setPressed(false);
                    btn_speak.setClickable(false);
                    // 取消录音框
                    layout_record.setVisibility(View.INVISIBLE);
                    // 发送语音消息
                    sendVoiceMessage(localPath, recordTime);
                    //是为了防止过了录音时间后，会多发一条语音出去的情况。
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            btn_speak.setClickable(true);
                        }
                    }, 1000);
                }
            }
        });
    }

    public boolean isFromProfile() {
        return isFromProfile;
    }

    /**
     * 长按说话
     *
     * @author smile
     * @date 2014-7-1 下午6:10:16
     */
    class VoiceTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!Utils.checkSdCard()) {
                        Utils.makeSysToast("发送语音需要sdcard支持！");
                        return false;
                    }
                    try {
                        v.setPressed(true);
                        layout_record.setVisibility(View.VISIBLE);
                        tv_voice_tips.setText(getString(R.string.voice_cancel_tips));
                        // 开始录音
                        recordManager.startRecording(mConversationManager.getConversationId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        tv_voice_tips.setText(getString(R.string.voice_cancel_tips));
                        tv_voice_tips.setTextColor(Color.RED);
                    } else {
                        tv_voice_tips.setText(getString(R.string.voice_up_tips));
                        tv_voice_tips.setTextColor(Color.WHITE);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    layout_record.setVisibility(View.INVISIBLE);
                    try {
                        if (event.getY() < 0) {// 放弃录音
                            recordManager.cancelRecording();
                        } else {
                            int recordTime = recordManager.stopRecording();
                            if (recordTime > 1) {
                                // 发送语音文件
                                sendVoiceMessage(recordManager.getRecordFilePath(mConversationManager.getConversationId()), recordTime);
                            } else {// 录音时间过短，则提示录音过短的提示
                                layout_record.setVisibility(View.GONE);
                                showShortToast().show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                default:
                    return false;
            }
        }
    }

    Toast toast;

    /**
     * 显示录音时间过短的Toast
     *
     * @return void
     * @Title: showShortToast
     */
    private Toast showShortToast() {
        if (toast == null) {
            toast = new Toast(this);
        }
        View view = LayoutInflater.from(this).inflate(
                R.layout.include_chat_voice_short, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }

    private void setClickListener() {
        edit_msg.setOnClickListener(v -> {
            if (layout_more.getVisibility() == View.VISIBLE) {
                layout_add.setVisibility(View.GONE);
                layout_emo.setVisibility(View.GONE);
                layout_more.setVisibility(View.GONE);
            }
        });

        btn_chat_emo.setOnClickListener(v -> {
            if (layout_more.getVisibility() == View.GONE) {
                Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
                    @Override
                    public void onPermissionResult(Permiso.ResultSet resultSet) {
                        if (resultSet.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            showEditState(true);
                        } else {
                            Toast.makeText(ChatActivity.this, "发送文件需要权限，请到应用权限中进行设置", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                        Permiso.getInstance().showRationaleInDialog("需要文件读取权限", "发送文件需要权限，请到应用权限中进行设置", null, callback);
                    }
                }, Manifest.permission.READ_EXTERNAL_STORAGE);

            } else {
                if (layout_add.getVisibility() == View.VISIBLE) {
                    layout_add.setVisibility(View.GONE);
                    layout_emo.setVisibility(View.VISIBLE);
                } else {
                    layout_more.setVisibility(View.GONE);
                }
            }
        });

        btn_chat_add.setOnClickListener(v -> {
            if (layout_more.getVisibility() == View.GONE) {
                layout_more.setVisibility(View.VISIBLE);
                layout_add.setVisibility(View.VISIBLE);
                layout_emo.setVisibility(View.GONE);
                hideSoftInputView();
            } else {
                if (layout_emo.getVisibility() == View.VISIBLE) {
                    layout_emo.setVisibility(View.GONE);
                    layout_add.setVisibility(View.VISIBLE);
                } else {
                    layout_more.setVisibility(View.GONE);
                }
            }
        });
        btn_chat_voice.setOnClickListener(v -> {
            edit_msg.setVisibility(View.GONE);
            layout_more.setVisibility(View.GONE);
            btn_chat_voice.setVisibility(View.GONE);
            btn_chat_keyboard.setVisibility(View.VISIBLE);
            btn_speak.setVisibility(View.VISIBLE);
            hideSoftInputView();
        });
        btn_chat_keyboard.setOnClickListener(v -> {
            showEditState(false);
        });

        btn_chat_send.setOnClickListener(v -> {
            Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
                @Override
                public void onPermissionResult(Permiso.ResultSet resultSet) {
                    if (resultSet.isPermissionGranted(Manifest.permission.READ_PHONE_STATE)) {
                        sendMessage();
                    } else {
                        Utils.makeSysToast("需要读取手机状态权限，请到应用权限中进行设置");
                    }
                }

                @Override
                public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                    Permiso.getInstance().showRationaleInDialog("需要权限", "需要读取手机状态权限，请到应用权限中进行设置", null, callback);
                }
            }, Manifest.permission.READ_PHONE_STATE);

        });
        layout_add.findViewById(R.id.tv_picture).setOnClickListener(v -> {
            sendLocalImageMessage();
        });
        layout_add.findViewById(R.id.tv_camera).setOnClickListener(v -> {
            sendRemoteImageMessage();
        });
        layout_add.findViewById(R.id.tv_location).setOnClickListener(v -> {
            sendLocationMessage();
        });
    }

    /**
     * 根据是否点击笑脸来显示文本输入框的状态
     *
     * @param isEmo 用于区分文字和表情
     * @return void
     */
    private void showEditState(boolean isEmo) {
        edit_msg.setVisibility(View.VISIBLE);
        btn_chat_keyboard.setVisibility(View.GONE);
        btn_chat_voice.setVisibility(View.VISIBLE);
        btn_speak.setVisibility(View.GONE);
        edit_msg.requestFocus();
        if (isEmo) {
            layout_more.setVisibility(View.VISIBLE);
            layout_more.setVisibility(View.VISIBLE);
            layout_emo.setVisibility(View.VISIBLE);
            layout_add.setVisibility(View.GONE);
            hideSoftInputView();
        } else {
            layout_more.setVisibility(View.GONE);
            showSoftInputView();
        }
    }

    /**
     * 显示软键盘
     */
    public void showSoftInputView() {
        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .showSoftInput(edit_msg, 0);
        }
    }

    /**
     * 发送文本消息
     */
    private void sendMessage() {
        String text = edit_msg.getText().toString();
        if (TextUtils.isEmpty(text.trim())) {
            Utils.makeSysToast("请输入内容");
            return;
        }
        //TODO 发送消息：6.1、发送文本消息
        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setBmobIMUserInfo(IMUtils.getIMUserInfoByUser(Utils.getCurrentShortUser()));
        msg.setContent(text);
        //可随意设置额外信息
        Map<String, Object> map = new HashMap<>();
        map.put("level", "1");
        msg.setExtraMap(map);
        //save to local
        IMUserRealm userToSave = shortUser.toIMRealm();
        userToSave.updateTime = System.currentTimeMillis();
        userToSave.unReadNum = 0;
        userToSave.isInConversation = true;
        userToSave.sendSuccess = false;
        userToSave.lastMsg = text;
        BaseRealmDao.insertOrUpdate(userToSave);
        EventUtils.post(userToSave);
        this.userToSave = userToSave;
        mConversationManager.sendMessage(msg, listener);
    }

    /**
     * 发送本地图片文件
     */
    public void sendLocalImageMessage() {
        //TODO 发送消息：6.2、发送本地图片消息
        //正常情况下，需要调用系统的图库或拍照功能获取到图片的本地地址，开发者只需要将本地的文件地址传过去就可以发送文件类型的消息
        BmobIMImageMessage image = new BmobIMImageMessage("/storage/emulated/0/netease/cloudmusic/网易云音乐相册/小梦大半_1371091013186741.jpg");
        mConversationManager.sendMessage(image, listener);
    }

    /**
     * 直接发送远程图片地址
     */
    public void sendRemoteImageMessage() {
        //TODO 发送消息：6.3、发送远程图片消息
        BmobIMImageMessage image = new BmobIMImageMessage();
        image.setRemoteUrl("https://avatars3.githubusercontent.com/u/11643472?v=4&u=df609c8370b3ef7a567457eafd113b3ba6ba3bb6&s=400");
        mConversationManager.sendMessage(image, listener);
    }


    /**
     * 发送本地音频文件
     */
    private void sendLocalAudioMessage() {
        //TODO 发送消息：6.4、发送本地音频文件消息
        BmobIMAudioMessage audio = new BmobIMAudioMessage("此处替换为你本地的音频文件地址");
        mConversationManager.sendMessage(audio, listener);
    }


    /**
     * 发送远程音频文件
     */
    private void sendRemoteAudioMessage() {
        //TODO 发送消息：6.5、发送本地音频文件消息
        BmobIMAudioMessage audio = new BmobIMAudioMessage();
        audio.setRemoteUrl("此处替换为你远程的音频文件地址");
        mConversationManager.sendMessage(audio, listener);
    }

    /**
     * 发送本地视频文件
     */
    private void sendLocalVideoMessage() {
        BmobIMVideoMessage video = new BmobIMVideoMessage("此处替换为你本地的视频文件地址");
        //TODO 发送消息：6.6、发送本地视频文件消息
        mConversationManager.sendMessage(video, listener);
    }

    /**
     * 发送远程视频文件
     */
    private void sendRemoteVideoMessage() {
        //TODO 发送消息：6.7、发送本地音频文件消息
        BmobIMAudioMessage audio = new BmobIMAudioMessage();
        audio.setRemoteUrl("此处替换为你远程的音频文件地址");
        mConversationManager.sendMessage(audio, listener);
    }

    /**
     * 发送本地文件
     */
    public void sendLocalFileMessage() {
        //TODO 发送消息：6.8、发送本地文件消息
        BmobIMFileMessage file = new BmobIMFileMessage("此处替换为你本地的文件地址");
        mConversationManager.sendMessage(file, listener);
    }

    /**
     * 发送远程文件
     */
    public void sendRemoteFileMessage() {
        //TODO 发送消息：6.9、发送远程文件消息
        BmobIMFileMessage file = new BmobIMFileMessage();
        file.setRemoteUrl("此处替换为你远程的文件地址");
        mConversationManager.sendMessage(file, listener);
    }

    /**
     * 发送语音消息
     *
     * @param local
     * @param length
     * @return void
     * @Title: sendVoiceMessage
     */
    private void sendVoiceMessage(String local, int length) {
        //TODO 发送消息：6.5、发送本地音频文件消息
        BmobIMAudioMessage audio = new BmobIMAudioMessage(local);
        //可设置额外信息-开发者设置的额外信息，需要开发者自己从extra中取出来
        Map<String, Object> map = new HashMap<>();
        map.put("from", "优酷");
        //TODO 自定义消息：7.1、给消息设置额外信息
        audio.setExtraMap(map);
        //设置语音文件时长：可选
//        audio.setDuration(length);
        mConversationManager.sendMessage(audio, listener);
    }


    /**
     * 发送地理位置消息
     */
    public void sendLocationMessage() {
        //TODO 发送消息：6.10、发送位置消息
        //测试数据，真实数据需要从地图SDK中获取
        BmobIMLocationMessage location = new BmobIMLocationMessage("广州番禺区", 23.5, 112.0);
        Map<String, Object> map = new HashMap<>();
        map.put("from", "百度地图");
        location.setExtraMap(map);
        mConversationManager.sendMessage(location, listener);
    }

    /**
     * 消息发送监听器
     */
    IMUserRealm userToSave;
    public MessageSendListener listener = new MessageSendListener() {

        @Override
        public void onProgress(int value) {
            super.onProgress(value);
            //文件类型的消息才有进度值
        }

        @Override
        public void onStart(BmobIMMessage msg) {
            super.onStart(msg);
            adapter.addMessage(msg);
            edit_msg.setText("");
            scrollToBottom();
        }

        @Override
        public void done(BmobIMMessage msg, BmobException e) {
            adapter.notifyDataSetChanged();
            edit_msg.setText("");
            scrollToBottom();
            if (e != null) {
                Utils.makeSysToast(e.getMessage());
            } else {
                if (userToSave != null) {
                    userToSave.sendSuccess = true;
                    BaseRealmDao.insertOrUpdate(userToSave);
                    EventUtils.post(userToSave);
                }
            }
        }
    };

    /**
     * 首次加载，可设置msg为null，下拉刷新的时候，默认取消息表的第一个msg作为刷新的起始时间点，默认按照消息时间的降序排列
     *
     * @param msg
     */
    public void queryMessages(BmobIMMessage msg) {
        //TODO 消息：5.2、查询指定会话的消息记录
        mConversationManager.queryMessages(msg, 10, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                sw_refresh.setRefreshing(false);
                if (e == null) {
                    if (null != list && list.size() > 0) {
                        adapter.addMessages(list);
                        layoutManager.scrollToPositionWithOffset(list.size() - 1, 0);
                    }
                } else {
                    Utils.makeSysToast(e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });
    }

    private void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(adapter.getItemCount() - 1, 0);
    }


    //TODO 消息接收：8.2、单个页面的自定义接收器
    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        //当注册页面消息监听时候，有消息（包含离线消息）到来时会回调该方法
        for (int i = 0; i < list.size(); i++) {
            addMessage2Chat(list.get(i));
        }
    }

    /**
     * 添加消息到聊天界面中
     *
     * @param event
     */
    private void addMessage2Chat(MessageEvent event) {
        BmobIMMessage msg = event.getMessage();
        if (mConversationManager != null && event != null && mConversationManager.getConversationId().equals(event.getConversation().getConversationId()) //如果是当前会话的消息
                && !msg.isTransient()) {//并且不为暂态消息
            if (adapter.findPosition(msg) < 0) {//如果未添加到界面中
                adapter.addMessage(msg);
                //更新该会话下面的已读状态
                mConversationManager.updateReceiveStatus(msg);
            }
            scrollToBottom();
        } else {
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (layout_more.getVisibility() == View.VISIBLE) {
                layout_more.setVisibility(View.GONE);
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Permiso.getInstance().setActivity(this);
        //锁屏期间的收到的未读消息需要添加到聊天界面中
        addUnReadMessage();
        //添加页面消息监听器
        BmobIM.getInstance().addMessageListHandler(this);
        // 有可能锁屏期间，在聊天界面出现通知栏，这时候需要清除通知
        BmobNotificationManager.getInstance(this).cancelNotification();
    }

    /**
     * 添加未读的通知栏消息到聊天界面
     */
    private void addUnReadMessage() {
        List<MessageEvent> cache = BmobNotificationManager.getInstance(this).getNotificationCacheList();
        if (cache.size() > 0) {
            int size = cache.size();
            for (int i = 0; i < size; i++) {
                MessageEvent event = cache.get(i);
                addMessage2Chat(event);
            }
        }
        scrollToBottom();
    }

    @Override
    protected void onPause() {
        //移除页面消息监听器
        BmobIM.getInstance().removeMessageListHandler(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //清理资源
        if (recordManager != null) {
            recordManager.clear();
        }
        //TODO 消息：5.4、更新此会话的所有消息为已读状态
        if (mConversationManager != null) {
            mConversationManager.updateLocalCache();
        }
        hideSoftInputView();
        super.onDestroy();
    }

    protected <T extends View> T bindViewById(int resId) {
        return (T) findViewById(resId);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_config, menu);
        return true;
    }
}
