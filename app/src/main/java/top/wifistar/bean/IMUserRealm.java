package top.wifistar.bean;

import org.parceler.Parcel;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
@Parcel(value = Parcel.Serialization.BEAN, analyze = {IMUserRealm.class})
public class IMUserRealm extends RealmObject {

    @PrimaryKey
    public Integer userId;
    public String userName;
    public String photo;
    public String sex;
    public String content;
    public String onlineState;
    public Date time;
    public String isBlockMe;
    public String messageId;
    public String isRead;
    public Integer newMsgCount;
    public String sendState;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOnlineState() {
        return onlineState;
    }

    public void setOnlineState(String onlineState) {
        this.onlineState = onlineState;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getIsBlockMe() {
        return isBlockMe;
    }

    public void setIsBlockMe(String isBlockMe) {
        this.isBlockMe = isBlockMe;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public Integer getNewMsgCount() {
        return newMsgCount;
    }

    public void setNewMsgCount(Integer newMsgCount) {
        this.newMsgCount = newMsgCount;
    }

    public String getSendState() {
        return sendState;
    }

    public void setSendState(String sendState) {
        this.sendState = sendState;
    }
}
