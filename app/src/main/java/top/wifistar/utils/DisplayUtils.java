package top.wifistar.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.WindowManager;

/**
 * Created by boyla on 2018/12/2.
 */

public class DisplayUtils {


    /**
     * 计算出图片初次显示需要放大倍数
     *
     * @param imagePath 图片的绝对路径
     */
    public static ScaleInfo getInitImageScale(Activity activity, String imagePath) {
        ScaleInfo info = new ScaleInfo();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        WindowManager wm = activity.getWindowManager();
        int srcw = wm.getDefaultDisplay().getWidth();
        int srch = wm.getDefaultDisplay().getHeight();
        // 拿到图片的宽和高
        int imagew = bitmap.getWidth();
        int imageh = bitmap.getHeight();
        info.scale = 1.0f;
        //图片宽度大于屏幕，但高度小于屏幕，则缩小图片至填满屏幕宽
        if (imagew > srcw && imageh <= srch) {
            info.scale = srcw * 1.0f / imagew;
        }
        //图片宽度小于屏幕，但高度大于屏幕，则放大图片至填满屏幕宽
        if (imagew <= srcw && imageh > srch) {
            info.scale = srcw * 1.0f / imagew;
        }
        //图片高度和宽度都小于屏幕，则放大图片至填满屏幕宽
        if (imagew < srcw && imageh < srch) {
            info.scale = srcw * 1.0f / imagew;
        }
        //图片高度和宽度都大于屏幕，则缩小图片至填满屏幕宽
        if (imagew > srcw && imageh > srch) {
            info.scale = srcw * 1.0f / imagew;
        }

        if (imagew < srcw * 1.5 && imageh < srch * 1.5) {
            info.showRaw = true;
        }
        return info;
    }

    public static class ScaleInfo {
        public boolean showRaw = false;
        public float scale;
    }
}
