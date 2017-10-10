package top.wifistar.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import io.realm.RealmObject;

/**
 * Created by hasee on 2017/4/5.
 */

public class UserProfile extends BmobObject implements ToRealmObject {
    String userId;
    String nickName;
    BmobDate birthday;
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
    Boolean invisible;
    String blocks;
    String mass;
    String momentsNum;

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

    public BmobDate getBirthday() {
        return birthday;
    }

    public void setBirthday(BmobDate birthday) {
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

    public String getMass() {
        return mass;
    }

    public void setMass(String mass) {
        this.mass = mass;
    }

    public String getMomentsNum() {
        return momentsNum;
    }

    public void setMomentsNum(String momentsNum) {
        this.momentsNum = momentsNum;
    }

    @Override
    public RealmObject toRealmObject() {
        UserProfileRealm obj = new UserProfileRealm();
        obj.setUserId(this.getUserId());
        obj.setUserName(this.getNickName());
        obj.setBirthday(this.getBirthday().toString());
        obj.setSex(this.getSex());
        obj.setLanguage(this.getLanguage());
        obj.setMoblie(this.getMoblie());
        obj.setEmail(this.getEmail());
        obj.setDescription(this.getDescription());
        obj.setCountry(this.getCountry());
        obj.setState(this.getState());
        obj.setCity(this.getCity());
        obj.setAvatar(this.getAvatar());
        obj.setPictures(this.getPictures());
        obj.setInvisible(this.getInvisible());
        obj.setBlocks(this.getBlocks());
        obj.setMomentsNum(this.getMomentsNum());
        obj.setUpdatedAt(this.getUpdatedAt());
        return obj;
    }
}
