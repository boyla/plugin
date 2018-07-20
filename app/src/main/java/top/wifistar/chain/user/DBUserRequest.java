package top.wifistar.chain.user;

import android.text.TextUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.realm.RealmResults;
import top.wifistar.bean.bmob.User;
import top.wifistar.realm.BaseRealmDao;
import top.wifistar.realm.UserRealm;

/**
 * Created by boyla on 2018/6/4.
 */

public class DBUserRequest extends UserRequest{

    @Override
    public int getRequestLevel() {
        return LEV_DB;
    }

    @Override
    public User handleRequest(String shortUserObjId) {
        RealmResults<UserRealm> dbData = BaseRealmDao.realm.where(UserRealm.class).equalTo("objectId", shortUserObjId).findAll();
        if (!dbData.isEmpty()) {
            dbData.first().toBmobObject();
        }
        return null;
    }

    @Override
    public void insertOrUpdate(User user) {
        if(user!=null && !TextUtils.isEmpty(user.getObjectId()) && !TextUtils.isEmpty(user.getName())){
            BaseRealmDao.insertOrUpdate(user.toRealmObject());
        }
    }

    public UserRealm queryUserRealm(String shortUserObjId) {
        RealmResults<UserRealm> dbData = BaseRealmDao.realm.where(UserRealm.class).equalTo("objectId", shortUserObjId).findAll();
        if (!dbData.isEmpty()) {
            dbData.first();
        }
        return null;
    }
}
