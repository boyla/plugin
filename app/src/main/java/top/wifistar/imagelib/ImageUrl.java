package top.wifistar.imagelib;

import cn.bmob.v3.BmobObject;

/**
 * Created by suneee on 2016/11/17.
 */
public class ImageUrl extends BmobObject {
    String url;


    public static String uploadUrl(String url){
        ImageUrl imageUrl = new ImageUrl();
        imageUrl.url = url;
        imageUrl.save();
        return url;
    }
}
