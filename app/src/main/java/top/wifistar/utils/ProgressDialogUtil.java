package top.wifistar.utils;

import android.app.ProgressDialog;
import android.content.Context;

import top.wifistar.R;
import top.wifistar.dialog.LoadingDialog;


/**
 * Created by popoy on 2015/8/11.
 */
public class ProgressDialogUtil {
    /**
     * 获取通用的一个ProgressDialog
     *
     * @param context
     * @return
     */
    public static LoadingDialog getCustomProgressDialog(Context context) {
        LoadingDialog progressDialog = new LoadingDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    public static LoadingDialog getCustomProgressDialog(Context context, int textResource) {
        return getCustomProgressDialog(context, context.getString(textResource));
    }

    public static ProgressDialog getImageCropProgressDialog(Context context, int textResource) {
        return getImageCropProgressDialog(context, context.getString(textResource));
    }

    public static LoadingDialog getCustomProgressDialog(Context context, String text) {
        LoadingDialog progressDialog = new LoadingDialog(context, text);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    public static ProgressDialog getImageCropProgressDialog(Context context, String msg) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.loading));
        progressDialog.setCancelable(false);
        progressDialog.setMessage(msg);
        return progressDialog;
    }

    public static ProgressDialog getSystemProgressDialog(Context context, String msg) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(msg);
        return progressDialog;
    }

    public static ProgressDialog getSystemProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    public static ProgressDialog getSystemProgressDialog(Context context, int text) {
        return getSystemProgressDialog(context, context.getString(text));
    }

}
