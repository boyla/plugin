package top.wifistar.bean.bmob;

import android.text.TextUtils;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by boyla on 2019/1/2.
 */

public class Topic extends BmobObject {
    public String name;
    public String owner;
    public String info;
    public String headImg;
    public String bg;

    public static void getTopicListByUserId(String userId, FindListener<Topic> findListener) {
        if (TextUtils.isEmpty(userId) || findListener == null) {
            return;
        }
        BmobQuery<Topic> query = new BmobQuery<>();
        query.addWhereEqualTo("owner", userId);
        query.setLimit(20);
        query.findObjects(findListener);
    }
}
