package top.wifistar.http;

import android.text.TextUtils;

import java.security.cert.CertificateException;

import javax.net.ssl.X509TrustManager;

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
        if (TextUtils.isEmpty(url)) {
            return res;
        }
        if (client == null) {
            client = getOkHttpClient();
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

    public static OkHttpClient getOkHttpClient() {
        //定义一个信任所有证书的TrustManager
        final X509TrustManager trustAllCert = new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        };
        //设置OkHttpClient
        return new OkHttpClient.Builder().sslSocketFactory(new SSL(trustAllCert), trustAllCert).build();
    }
}
