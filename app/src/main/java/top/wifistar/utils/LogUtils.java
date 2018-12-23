package top.wifistar.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by boyla on 2018/12/22.
 */

public class LogUtils {

    public static boolean logOn = true;
    public static String DEFAULT_LOG_TAG = "WiFiStar_log";

    public static void logI(String str) {
        logI(null,str);
    }

    public static void logI(String tag, String str) {
        if (logOn) {
            Log.i(TextUtils.isEmpty(tag) ? DEFAULT_LOG_TAG : tag, str);
        }
    }
}
