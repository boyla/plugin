package top.wifistar.httpserver;

import android.text.TextUtils;

import com.bumptech.glide.load.model.GlideUrl;

public class AppImageUrl extends GlideUrl {
    private String keyUrl;

    public AppImageUrl(String userId, String url) {
        super(getRealUrl(userId,url));
        keyUrl = url;
    }

    private static String getRealUrl(String userId, String url) {
        String wifiHost = NetUtils.userHostMap.get(userId);
        if(TextUtils.isEmpty(wifiHost)){
            return url;
        }else{
            return wifiHost;
        }
    }

    @Override
    public String getCacheKey() {
        //将token部分参数替换为空字符串后返回作为缓存Key
        return keyUrl;
    }

}
