package top.wifistar.im;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import top.wifistar.bean.bmob.User;
import top.wifistar.utils.Utils;

public class IMUtils {
    //创建一个暂态会话入口，发送添加好友请求，同意好友请求
    public static BmobIMConversation getTempConversationEntrance(BmobIMUserInfo info) {
        return BmobIM.getInstance().startPrivateConversation(info, true, null);
    }

    //创建一个常态会话入口，好友聊天，陌生人聊天
    public static BmobIMConversation getConversationEntrance(BmobIMUserInfo info) {
        BmobIMConversation res = null;
        try {
            res = BmobIM.getInstance().startPrivateConversation(info, null);
        } catch (Exception e) {
            BmobIMClient bc = BmobIMClient.getInstance();
            hookBmobClient(bc, Utils.getCurrentShortUser().getObjectId());
            res = bc.startPrivateConversation(info, null);
        }
        return res;
    }

    private static void hookBmobClient(BmobIMClient bc, String userId) {
        try {
            Field field = BmobIMClient.class.getDeclaredField("i");
            field.setAccessible(true);   //这一步是关键，将访问name字段的权限打开
            field.set(bc, userId);   //修改被private修饰的
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<BmobIMConversation> getAllConversations() {
        return BmobIM.getInstance().loadAllConversation();
    }

    public static long getUnreadNum(String conversationId) {
        return BmobIM.getInstance().getUnReadCount(conversationId);
    }

    public static long getAllUnreadNum() {
        return BmobIM.getInstance().getAllUnReadCount();
    }

    public static void deleteConversation(BmobIMConversation c) {
        BmobIM.getInstance().deleteConversation(c);
    }

    public static void deleteConversation(String conversationId) {
        BmobIM.getInstance().deleteConversation(conversationId);
    }

    public static void clearAllConversation() {
        BmobIM.getInstance().clearAllConversation();
    }

    public static void updateConversation(BmobIMConversation conversation) {
        BmobIM.getInstance().updateConversation(conversation);
    }

    public static BmobIMConversation getConversationEntranceByShortUser(User shortUser) {
        BmobIMUserInfo info = getIMUserInfoByUser(shortUser);
        if (info == null) {
            return null;
        }
        return getConversationEntrance(info);
    }

    public static BmobIMUserInfo getIMUserInfoByUser(User shortUser) {
        if (shortUser != null) {
            String url = "";
            if (!TextUtils.isEmpty(shortUser.getHeadUrl())) {
                url = shortUser.getHeadUrl().split("_")[0];
            }
            BmobIMUserInfo info = new BmobIMUserInfo(shortUser.getObjectId(), shortUser.getName(), url);
            info.setId(System.currentTimeMillis());
            return info;
        } else {
            return null;
        }
    }
}
