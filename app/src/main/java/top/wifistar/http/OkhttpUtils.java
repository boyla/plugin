package top.wifistar.http;

import android.text.TextUtils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by boyla on 2019/9/8.
 */

public class OkhttpUtils {
    private static OkHttpClient client;

    public static String getResponseStr(String url) {
        String res = null;
        if(TextUtils.isEmpty(url)){
            return res;
        }
        if (client == null) {
            client = new OkHttpClient();
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            res = response.body().string();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return res;
    }
}
