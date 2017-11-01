package top.wifistar.realm;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import top.wifistar.bean.bmob.UserProfile;

/**
 * Created by hasee on 2017/4/5.
 */

public class UserProfileRealm extends RealmObject implements ToBmobObject{
    String userId;
    String nickName;
    String birthday;
    String sex;
    String language;
    String moblie;
    String email;
    String description;
    String country;
    String state;
    String city;
    String avatar;
    String pictures;
    boolean invisible;
    String blocks;
    String mass;
    String updatedAt;
    String momentsNum;
    @PrimaryKey
    public String objectId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMoblie() {
        return moblie;
    }

    public void setMoblie(String moblie) {
        this.moblie = moblie;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public Boolean getInvisible() {
        return invisible;
    }

    public void setInvisible(Boolean invisible) {
        this.invisible = invisible;
    }

    public String getBlocks() {
        return blocks;
    }

    public void setBlocks(String blocks) {
        this.blocks = blocks;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public String getMass() {
        return mass;
    }

    public void setMass(String mass) {
        this.mass = mass;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getMomentsNum() {
        return momentsNum;
    }

    public void setMomentsNum(String momentsNum) {
        this.momentsNum = momentsNum;
    }

    @Override
    public BmobObject toBmobObject() {
        UserProfile userProfile = new UserProfile();
        userProfile.setObjectId(objectId);
        userProfile.userId = userId;
        userProfile.nickName = nickName;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            userProfile.birthday = new BmobDate(sdf.parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        userProfile.sex = sex;
        userProfile.language = language;
        userProfile.moblie = moblie;
        userProfile.email = email;
        userProfile.description = description;
        userProfile.country = country;
        userProfile.state = state;
        userProfile.city = city;
        userProfile.avatar = avatar;
        userProfile.pictures = pictures;
        userProfile.invisible = invisible;
        userProfile.blocks = blocks;
        userProfile.mass = mass;
        userProfile.momentsNum = momentsNum;
        userProfile.userId = userId;
        return userProfile;
    }
}
