package top.wifistar.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by boyla on 2017/11/23.
 */

public class DebugUtils {

    public static final String TAG = "app_debug: ";
    public static final boolean DEBUG_MODE = true;

    public static void printLogD(String logInfo) {
        printLogD(TAG, logInfo);
    }

    public static void printLogD(String tag, String logInfo) {
        if (DEBUG_MODE && !TextUtils.isEmpty(tag)) {
            Log.d(tag, logInfo);
        }
    }
}
