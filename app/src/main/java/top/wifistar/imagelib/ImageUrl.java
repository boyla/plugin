package top.wifistar.imagelib;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import top.wifistar.utils.LogUtils;

/**
 * Created by suneee on 2016/11/17.
 */
public class ImageUrl extends BmobObject {
    String url;


    public static String uploadUrl(String url) {
        ImageUrl imageUrl = new ImageUrl();
        imageUrl.url = url;
        imageUrl.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e != null) {
                    LogUtils.d("uploadUrl", url);
                }
            }
        });
        return url;
    }
}
