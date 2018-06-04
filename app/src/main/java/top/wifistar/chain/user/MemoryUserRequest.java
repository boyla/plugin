package top.wifistar.chain.user;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import top.wifistar.bean.bmob.User;

/**
 * Created by boyla on 2018/6/4.
 */

public class MemoryUserRequest extends UserRequest{

    Map<String, User> memCacheUsers = new ConcurrentHashMap<>();

    @Override
    public int getRequestLevel() {
        return LEV_MEMORY;
    }

    @Override
    public User handleRequest(String id) {
        return memCacheUsers.get(id);
    }

    @Override
    public void insertOrUpdate(User user) {
        if(user!=null && !TextUtils.isEmpty(user.getObjectId())){
            memCacheUsers.put(user.getObjectId(), user);
        }
    }
}
