package top.wifistar.utils;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import top.wifistar.app.App;
import top.wifistar.realm.BaseRealmDao;
import top.wifistar.realm.MomentRealm;
import top.wifistar.realm.ToBmobObject;

/**
 * Created by boyla on 2018/3/17.
 */

public class RealmUtils {
    public static void deleteFromRealmByObjId(RealmObject realmObj) {
        String objId = ((ToBmobObject)realmObj).getRealmId();
        final RealmResults results=  BaseRealmDao.realm.where(realmObj.getClass()).equalTo("objectId", objId).findAll();
        BaseRealmDao.realm.executeTransaction(realm -> {
            if(results.size() > 0){
                results.deleteAllFromRealm();
            }
        });
    }
}
