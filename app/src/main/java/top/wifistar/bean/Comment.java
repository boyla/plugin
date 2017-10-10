package top.wifistar.bean;

import cn.bmob.v3.BmobObject;
import io.realm.RealmObject;

/**
 * Created by hasee on 2017/4/5.
 */

public class Comment extends BmobObject implements ToRealmObject {

    String momentId;
    String userId;
    String userName;
    String avatar;
    String content;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMomentId() {
        return momentId;
    }

    public void setMomentId(String momentId) {
        this.momentId = momentId;
    }

    @Override
    public RealmObject toRealmObject() {
        CommentRealm obj = new CommentRealm();
        obj.setUserId(this.getUserId());
        obj.setUserName(this.getUserName());
        obj.setAvatar(this.getAvatar());
        obj.setMomentId(this.getMomentId());
        obj.setContent(this.getContent());
        return obj;
    }
}
