package top.wifistar.bean.bmob;

import android.util.Log;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import top.wifistar.bean.bmob.User;
import top.wifistar.realm.BaseRealmDao;

/**
 * Created by boyla on 2017/9/29.
 */

public class BmobUtils {
    public static void updateUser(User user, String profileId, String nickName, String avatar, Integer sex) {

        user.name = nickName;
        user.id = profileId;
        user.headUrl = avatar;
        user.sex = sex;
        BaseRealmDao.insertOrUpdate(user.toRealmObject());

        user.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("ShortUser:", "更新成功");
                } else {
                    Log.i("ShortUser:", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    public static void updateUser(User user) {
        BaseRealmDao.insertOrUpdate(user.toRealmObject());
        user.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("ShortUser:", "更新成功");
                } else {
                    Log.i("ShortUser:", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
}
