package top.wifistar.event;

import top.wifistar.bean.bmob.User;

/**
 * Created by boyla on 2018/6/22.
 */

public class EurekaEvent {
    public User user;

    public EurekaEvent(User user) {
        this.user = user;
    }
}
