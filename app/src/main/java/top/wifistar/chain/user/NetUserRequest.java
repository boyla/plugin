package top.wifistar.chain.user;

import android.text.TextUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import top.wifistar.bean.bmob.User;
import top.wifistar.realm.BaseRealmDao;

/**
 * Created by boyla on 2018/6/4.
 */

public class NetUserRequest extends UserRequest{


    @Override
    public int getRequestLevel() {
        return LEV_NETWORK;
    }

    @Override
    public User handleRequest(String shortUserObjId) {
        return null;
    }

    public interface NetRequestCallBack{
        void onSuccess(User user);
        void onFailure(String msg);
    }
    public void handleNetRequest(String shortUserObjId,NetRequestCallBack callBack) {
        BmobQuery<User> query = new BmobQuery<User>();
        query.getObject(shortUserObjId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    callBack.onSuccess(user);
                } else {
                    callBack.onFailure(e.getMessage());
                }
            }
        });
    }

    @Override
    public void insertOrUpdate(User user) {}
}
