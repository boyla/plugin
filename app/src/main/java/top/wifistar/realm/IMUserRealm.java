package top.wifistar.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import top.wifistar.bean.bmob.User;

/**
 * Created by boyla on 2018/5/11.
 */

public class IMUserRealm extends RealmObject implements ToBmobObject {
    //just for chat page
    public int unReadNum;
    public boolean isInConversation = false;
    public long updateTime;
    public boolean sendSuccess = true;
    public String lastMsg = "Hello dude.";

    //nick name, from profile
    public String name;
    public String headUrl;
    public String id;
    @PrimaryKey
    public String objectId;
    public String favorMoments = "";
    public String loaction;
    public String country;
    public String region;
    public String city;
    public String headBgUrl;
    public Integer sex;
    public Integer age;
    public String recentImgs;
    public String startWords;
    public String selfIntroduce;
    public String birth;
    public String follows;

    @Override
    public String toString() {
        return "objectId = " + objectId
                + "; name = " + name
                + "; headUrl = " + headUrl;
    }

    @Override
    public String getRealmId() {
        return objectId;
    }

    @Override
    public User toBmobObject() {
        User user = new User();
        user.name = name;
        user.headUrl = headUrl;
        user.id = id;
        user.favorMoments = favorMoments;
        user.sex = sex;
        user.age = age;
        user.loaction = loaction;
        user.headBgUrl = headBgUrl;
        user.country = country;
        user.region = region;
        user.city = city;
        user.recentImgs = recentImgs;
        user.setObjectId(objectId);
        user.startWords = startWords;
        user.selfIntroduce = selfIntroduce;
        user.birth = birth;
        user.follows = follows;

        return user;
    }
}
