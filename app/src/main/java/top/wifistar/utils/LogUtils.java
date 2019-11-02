package top.wifistar.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by boyla on 2018/12/22.
 */

public class LogUtils {

    public static boolean logOn = true;
    public static String DEFAULT_LOG_TAG = "LogUtils";

    public static void i(String str) {
        i(null,str);
    }

    public static void i(String tag, String str) {
        if (logOn) {
            Log.i(TextUtils.isEmpty(tag) ? DEFAULT_LOG_TAG : tag, str);
        }
    }

    public static void d(String str) {
        d(null,str);
    }

    public static void d(String tag, String str) {
        if (logOn) {
            Log.d(TextUtils.isEmpty(tag) ? DEFAULT_LOG_TAG : tag, str);
        }
    }

    public static void e(String str) {
        e(null,str);
    }

    public static void e(String tag, String str) {
        if (logOn) {
            Log.e(TextUtils.isEmpty(tag) ? DEFAULT_LOG_TAG : tag, str);
        }
    }
}
