package top.wifistar.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.widget.TextView;

import com.lqr.emoji.MoonUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import top.wifistar.app.App;

/**
 * Created by boyla on 2018/12/9.
 */

public class ChatUtils {

    public static SpannableString getEmotionContent(final TextView tv, String source) {
        return getEmotionContent(tv, source, false);
    }

    public static SpannableString getEmotionContent(final TextView tv, String source, boolean oneCenter) {
        if (oneCenter) {
            if (null != source && source.length() < 5) {
                tv.setGravity(Gravity.CENTER);
            } else {
                tv.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            }
        }

        SpannableString spannableString = new SpannableString(source);
        String regexEmotion = "\\[[^\\[]{1,10}\\]";
        Pattern patternEmotion = Pattern.compile(regexEmotion);
        Matcher matcherEmotion = patternEmotion.matcher(spannableString);
        while (matcherEmotion.find()) {
            // 获取匹配到的具体字符
            String key = matcherEmotion.group();
            // 匹配字符串的开始位置
            int start = matcherEmotion.start();
            // 利用表情名字获取到对应的图片
            BitmapDrawable d = (BitmapDrawable) MoonUtils.getEmotDrawable(App.getApp(), key, 0.6F);
            if (d != null) {
                // 压缩表情图片
                int size = (int) tv.getTextSize() * 13 / 10;
                Bitmap bitmap = d.getBitmap();
                Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
                ImageSpan span = new ImageSpan(App.getApp(), scaleBitmap);
                spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }

}
