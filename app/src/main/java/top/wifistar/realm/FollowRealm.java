package top.wifistar.realm;

import android.text.TextUtils;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import top.wifistar.bean.bmob.Follow;
import top.wifistar.utils.Utils;

public class FollowRealm  extends RealmObject implements ToBmobObject{

    public String objectId;
    @PrimaryKey
    public String followers;
    public Integer followState;

    @Override
    public Follow toBmobObject() {
        Follow bmob = new Follow();
        bmob.followers = followers;
        bmob.followState = followState;
        bmob.setObjectId(objectId);
        return bmob;
    }

    @Override
    public String getRealmId() {
        return objectId;
    }

    //0无关系；1左关注右；2右关注左；3互相关注
    public boolean isFollowing(){
        if(0==followState){
            return false;
        }
        if(3==followState){
            return true;
        }
        String userId = Utils.getCurrentShortUser().getObjectId();
        if(!TextUtils.isEmpty(userId)){
            if(userId.equals(followers.split("_")[0]) && 1==followState){
                return true;
            }else if(userId.equals(followers.split("_")[1]) && 2==followState){
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
}
