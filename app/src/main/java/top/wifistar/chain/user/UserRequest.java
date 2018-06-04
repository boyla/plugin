package top.wifistar.chain.user;

import top.wifistar.bean.bmob.User;

/**
 * Created by boyla on 2018/6/2.
 */

public abstract class UserRequest {

    public static final int LEV_MEMORY = 0;

    public static final int LEV_DB = 1;

    public static final int LEV_NETWORK = 2;

    public User user;

    public abstract int getRequestLevel();

    public abstract User handleRequest(String id);

    public abstract void insertOrUpdate(User user);
}
