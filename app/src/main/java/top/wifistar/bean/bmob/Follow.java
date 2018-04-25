package top.wifistar.bean.bmob;

import cn.bmob.v3.BmobObject;
import top.wifistar.realm.FollowRealm;
import top.wifistar.realm.ToRealmObject;

public class Follow extends BmobObject implements ToRealmObject {

    public String follower;
    public String followed;
    public boolean isFollowing;

    public Follow(){}

    public Follow(String follower, String followed){
        this.follower = follower;
        this.followed = followed;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getFollowed() {
        return followed;
    }

    public void setFollowed(String followed) {
        this.followed = followed;
    }

    @Override
    public String toString() {
        return "objectId = " + getObjectId()
                + "; follower = " + follower
                + "; followed = " + followed;
    }

    @Override
    public FollowRealm toRealmObject() {
        FollowRealm realm = new FollowRealm();
        realm.follower = follower;
        realm.followed = followed;
        realm.isFollowing = isFollowing;
        realm.objectId = getObjectId();
        return realm;
    }
}
