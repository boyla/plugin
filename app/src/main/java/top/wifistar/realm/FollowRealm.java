package top.wifistar.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import top.wifistar.bean.bmob.Follow;

public class FollowRealm  extends RealmObject implements ToBmobObject{

    @PrimaryKey
    public String objectId;
    public String follower;
    public String followed;
    public boolean isFollowing;

    @Override
    public Follow toBmobObject() {
        Follow bmob = new Follow();
        bmob.follower = follower;
        bmob.followed = followed;
        bmob.isFollowing = isFollowing;
        bmob.setObjectId(objectId);
        return bmob;
    }

    @Override
    public String getRealmId() {
        return objectId;
    }
}
