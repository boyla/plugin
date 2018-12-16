package top.wifistar.bean.bmob;

import android.text.TextUtils;

import cn.bmob.v3.BmobObject;
import top.wifistar.realm.FollowRealm;
import top.wifistar.realm.ToRealmObject;
import top.wifistar.utils.Utils;

public class Follow extends BmobObject implements ToRealmObject {

    public String followers;
    public Integer followState = 0;

    public Follow(String userId) {
        this.followers = getFollowers(userId);
    }

    public Follow() {}

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public Integer getFollowState() {
        return followState;
    }

    public void setFollowState(Integer followState) {
        this.followState = followState;
    }

    //0无关系；1左关注右；2右关注左；3互相关注
    public boolean isFollowing() {
        if (0 == followState) {
            return false;
        }
        if (3 == followState) {
            return true;
        }
        String userId = Utils.getCurrentShortUser().getObjectId();
        if (!TextUtils.isEmpty(userId)) {
            if (userId.equals(followers.split("_")[0]) && 1 == followState) {
                return true;
            } else if (userId.equals(followers.split("_")[1]) && 2 == followState) {
                return true;
            }
        }
        return false;
    }

    public void addFollow() {
        if (3 == followState) {
            return;
        }
        String userId = Utils.getCurrentShortUser().getObjectId();
        if (!TextUtils.isEmpty(userId)) {
            if (userId.equals(followers.split("_")[0])) {
                followState += 1;
            } else if (userId.equals(followers.split("_")[1])) {
                followState += 2;
            }
        }
    }

    public void cancelFollow() {
        if (0 == followState) {
            return;
        }
        String userId = Utils.getCurrentShortUser().getObjectId();
        if (!TextUtils.isEmpty(userId)) {
            if (userId.equals(followers.split("_")[0])) {
//                if (1 == followState) {
//                    followState = 0;
//                } else if (3 == followState) {
//                    followState = 2;
//                }
                followState -= 1;
            } else if (userId.equals(followers.split("_")[1])) {
//                if (2 == followState) {
//                    followState = 0;
//                } else if (3 == followState) {
//                    followState = 1;
//                }
                followState -= 2;
            }
        }
    }

    @Override
    public String toString() {
        return "objectId = " + getObjectId()
                + "; followers = " + followers
                + "; followState = " + followState;
    }

    @Override
    public FollowRealm toRealmObject() {
        FollowRealm realm = new FollowRealm();
        realm.followers = followers;
        realm.followState = followState;
        realm.objectId = getObjectId();
        return realm;
    }

    public static String getFollowers(String userId) {
        String myId = Utils.getCurrentShortUser().getObjectId();
        boolean userLeft = userId.compareTo(myId) > 0;
        return userLeft ? userId + "_" + myId : myId + "_" + userId;
    }
}
