package top.wifistar.bean.bmob;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by suneee on 2016/11/17.
 */
public class Photo extends RealmObject{

    @PrimaryKey
    public String url;
    public Integer w = 0;
    public Integer h = 0;
}
