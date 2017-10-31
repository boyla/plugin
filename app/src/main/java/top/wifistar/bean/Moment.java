package top.wifistar.bean;

import cn.bmob.v3.BmobObject;
import io.realm.RealmObject;
import top.wifistar.realm.ToRealmObject;

/**
 * Created by hasee on 2017/4/5.
 */

public class Moment extends BmobObject implements ToRealmObject {
    String userId;
    String userName;
    String avatar;
    String content;
    String pictures;
    String likes;



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

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    @Override
    public RealmObject toRealmObject() {
        MomentRealm obj = new MomentRealm();
        obj.setUserId(this.getUserId());
        obj.setUserName(this.getUserName());
        obj.setAvatar(this.getAvatar());
        obj.setPictures(this.getPictures());
        obj.setContent(this.getContent());
        this.getCreatedAt();
        return obj;
    }
}
