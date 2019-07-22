package top.wifistar.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.TextView;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import top.wifistar.R;
import top.wifistar.bean.bmob.Version;

/**
 * Created by boyla on 2018/6/8.
 */

public class UpdateUtils {


    public static void check4Update(Activity activity) throws Exception {
        PackageManager pm = activity.getPackageManager();//context为当前Activity上下文
        PackageInfo pi = pm.getPackageInfo(activity.getPackageName(), 0);
        ApplicationInfo applicationInfo = pm.getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
        String appName = (String) pm.getApplicationLabel(applicationInfo);
        int version = pi.versionCode;

        BmobQuery<Version> queryVersion = new BmobQuery<>();
        queryVersion.getObject("N1dpJJJK", new QueryListener<Version>() {
            @Override
            public void done(Version newestVersion, BmobException e) {
                if (e == null && newestVersion.version > version) {
                    AllenVersionChecker
                            .getInstance()
                            .downloadOnly(createUIData(newestVersion.url, newestVersion.versionInfo))
                            .setCustomVersionDialogListener(createCustomDialogTwo())
                            .executeMission(activity);
                }
            }
        });
    }

    private static CustomVersionDialogListener createCustomDialogTwo() {
        return new CustomVersionDialogListener() {
            @Override
            public Dialog getCustomVersionDialog(Context context, UIData versionBundle) {
                BaseDialog baseDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.custom_dialog_two_layout);
                TextView textView = baseDialog.findViewById(R.id.tv_msg);
                textView.setText(versionBundle.getContent());
                baseDialog.setCanceledOnTouchOutside(true);
                return baseDialog;
            }
        };
    }

    private static UIData createUIData(String url, String content) {
        UIData uiData = UIData.create();
        uiData.setTitle("版本更新");
        uiData.setDownloadUrl(url);
        uiData.setContent(content);
        return uiData;
    }


    public static class BaseDialog extends Dialog {
        private int res;

        public BaseDialog(Context context, int theme, int res) {
            super(context, theme);
            // TODO 自动生成的构造函数存根
            setContentView(res);
            this.res = res;
            setCanceledOnTouchOutside(false);
        }

    }
}
