package top.wifistar.utils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by hasee on 2017/3/27.
 */

public class EventUtils {

    public static void registerEventBus(Object subscriber){
        EventBus.getDefault().register(subscriber);
    }

    public static void unregisterEventBus(Object subscriber){
        EventBus.getDefault().unregister(subscriber);
    }

    public static void post(Object event){
        EventBus.getDefault().post(event);
    }
}
