package top.wifistar.bean;

import cn.bmob.v3.BmobUser;
import top.wifistar.bean.bmob.UserProfile;

/**
 * Created by hasee on 2017/1/10.
 */

public class BUser extends BmobUser {
    /**
     * updatedAt : 2017-01-09 23:30:34
     * username : brenta
     * objectId : mDowqqq2
     * createdAt : 2017-01-09 23:08:39
     */

    private String profileId;

    public UserProfile profile;


    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public static BUser getCurrentUser() {
        return (BUser)getCurrentUser(BUser.class);
    }
}
