package top.wifistar.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import top.wifistar.R;
import top.wifistar.customview.LoadingView;



public class LoadingDialog extends Dialog {
    private LoadingView viewLoading;

    public LoadingDialog(Context context) {
        this(context, R.style.LoadingDialog, null);
    }

    public LoadingDialog(Context context, String strMessage) {
        this(context, R.style.LoadingDialog, strMessage);
    }

    public LoadingDialog(Context context, int theme, String strMessage) {
        super(context, theme);

        this.setContentView(R.layout.dialog_loading);
        viewLoading = (LoadingView) findViewById(R.id.lv_loading);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;

    }

    public LoadingDialog(Context context, int theme, int testResource) {
        this(context, R.style.LoadingDialog, context.getString(testResource));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        viewLoading.clearAnimation();
        super.dismiss();
    }
}