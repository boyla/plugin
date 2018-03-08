package top.wifistar.event;

import top.wifistar.bean.bmob.Moment;

/**
 * Created by boyla on 2017/10/17.
 */

public class PublishMomentEvent {
    public Moment moment;
    //是否为模拟事件
    public boolean isTempEvent;

    public PublishMomentEvent(boolean isTempEvent) {
        this.isTempEvent = isTempEvent;
    }
}
