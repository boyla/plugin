//package top.wifistar.dialog;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.graphics.Bitmap;
//import android.graphics.drawable.Drawable;
//import android.support.annotation.NonNull;
//import android.support.annotation.StyleRes;
//import android.text.Spannable;
//import android.text.TextPaint;
//import android.text.method.LinkMovementMethod;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//
//
///**
// * Created by peakhu on 16/2/24.
// */
//public class CustomAlertDialog {
//
//    private Context mContext;
//
//    @BindViewById(id = "lnlAlertDialogRootView")
//    private LinearLayout lnlAlertDialogRootView;
//
//    @BindViewById(id = "ivAlertDialogIcon")
//    private ImageView ivAlertDialogIcon;
//
//    @BindViewById(id = "tvAlertDialogTitle")
//    private TextView tvAlertDialogTitle;
//
//    @BindViewById(id = "tvAlertDialogMsg")
//    private TextView tvAlertDialogMsg;
//
//    @BindViewById(id = "btnAlertDialogNegative")
//    private Button btnAlertDialogNegative;
//
//    @BindViewById(id = "vAlertDialogVSep")
//    private View vAlertDialogVSep;
//
//    @BindViewById(id = "btnAlertDialogPositive")
//    private Button btnAlertDialogPositive;
//
//    private Dialog mDialog;
//
//    private boolean isShowing = false;
//
//    private boolean isPositiveButtonShowing = false;
//
//    private boolean isNegativeButtonShowing = false;
//
//    private OnPressBackListener backListener;
//
//    public CustomAlertDialog(Context context) {
//        this.mContext = context;
//    }
//
//    public interface OnPressBackListener{
//        public void onBack(Dialog d);
//    }
//
//    private class MyDialog extends Dialog{
//        public MyDialog(@NonNull Context context, @StyleRes int themeResId) {
//            super(context, themeResId);
//        }
//
//        @Override
//        public void onBackPressed() {
//            if (backListener ==null){
//                super.onBackPressed();
//            }else {
//                backListener.onBack(this);
//            }
//
//        }
//    }
//
//    public CustomAlertDialog builder() {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_custom_alert, null);
//        MasonViewUtils.getInstance(mContext).inject(this, view);
//
//        mDialog = new MyDialog(mContext, R.style.AlertDialogStyle);
//        mDialog.setContentView(view);
//        mDialog.setCanceledOnTouchOutside(false);
//        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if(keyCode==KeyEvent.KEYCODE_BACK){
//                    EventBus.getDefault().post(new CancelHideFromAllDialogEvent());
//                    EventBus.getDefault().post(new CancelLivingWithDialogEvent());
//                    EventBus.getDefault().post(new CancelShowFavOnlyEvent());
//                }
//                return false;
//            }
//        });
//
//        lnlAlertDialogRootView.setLayoutParams(new FrameLayout.LayoutParams(
//                (int) (ViewUtil.getDisplayMetrics(mContext).widthPixels * 0.85),
//                LinearLayout.LayoutParams.WRAP_CONTENT));
//
//        return this;
//    }
//
//    public CustomAlertDialog builder(int layoutResId, int style) {
//        View view = LayoutInflater.from(mContext).inflate(layoutResId, null);
//        MasonViewUtils.getInstance(mContext).inject(this, view);
//
//        mDialog.setContentView(view);
//        mDialog = new Dialog(mContext, style);
//        mDialog.setCanceledOnTouchOutside(false);
//
//
//        lnlAlertDialogRootView.setLayoutParams(new FrameLayout.LayoutParams(
//                (int) (ViewUtil.getDisplayMetrics(mContext).widthPixels * 0.85),
//                LinearLayout.LayoutParams.WRAP_CONTENT));
//
//        return this;
//    }
//
//    public CustomAlertDialog setIcon(int resId) {
//        ivAlertDialogIcon.setVisibility(View.VISIBLE);
//        ivAlertDialogIcon.setImageResource(resId);
//        return this;
//    }
//
//
//    public CustomAlertDialog setOnPressBackListener(OnPressBackListener listener){
//        backListener = listener;
//        return this;
//    }
//
//    public CustomAlertDialog setIcon(Drawable drawable) {
//        ivAlertDialogIcon.setVisibility(View.VISIBLE);
//        ivAlertDialogIcon.setImageDrawable(drawable);
//        return this;
//    }
//
//    public CustomAlertDialog setIcon(Bitmap bitmap) {
//        ivAlertDialogIcon.setVisibility(View.VISIBLE);
//        ivAlertDialogIcon.setImageBitmap(bitmap);
//        return this;
//    }
//
//    public TextView getAlertDialogTitle() {
//        return tvAlertDialogTitle;
//    }
//
//    public CustomAlertDialog setTitle(String title) {
//        tvAlertDialogTitle.setVisibility(View.VISIBLE);
//        tvAlertDialogTitle.setText(title);
//        return this;
//    }
//
//    public CustomAlertDialog setTitleColor(int color) {
//        tvAlertDialogTitle.setTextColor(color);
//        return this;
//    }
//
//    public CustomAlertDialog setTitle(int titleId) {
//        tvAlertDialogTitle.setVisibility(View.VISIBLE);
//        tvAlertDialogTitle.setText(titleId);
//        return this;
//    }
//
//    public CustomAlertDialog setMsg(String msg) {
//        tvAlertDialogMsg.setVisibility(View.VISIBLE);
//        tvAlertDialogMsg.setText(msg);
//        return this;
//    }
//
//    public CustomAlertDialog setMsg(Spannable msg) {
//        tvAlertDialogMsg.setVisibility(View.VISIBLE);
//        tvAlertDialogMsg.setMovementMethod(LinkMovementMethod.getInstance());
//        tvAlertDialogMsg.setText(msg);
//        return this;
//    }
//
//    public CustomAlertDialog setMsg(int msgId) {
//        tvAlertDialogMsg.setVisibility(View.VISIBLE);
//        tvAlertDialogMsg.setText(msgId);
//        return this;
//    }
//
//    public TextView getMsgTextView(){
//        return tvAlertDialogMsg;
//    }
//
//    public CustomAlertDialog setCancelable(boolean cancel) {
//        mDialog.setCancelable(cancel);
//        return this;
//    }
//
//    public CustomAlertDialog setPositiveButton(String text, final View.OnClickListener listener) {
//        isPositiveButtonShowing = true;
//
//        btnAlertDialogPositive.setVisibility(View.VISIBLE);
//        btnAlertDialogPositive.setText(text);
//
//        btnAlertDialogPositive.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != listener) {
//                    listener.onClick(v);
//                }
//                mDialog.dismiss();
//            }
//        });
//        return this;
//    }
//
//    public CustomAlertDialog setPositiveButton(int textId, final View.OnClickListener listener) {
//        String text = mContext.getResources().getString(textId);
//        setPositiveButton(text, listener);
//        return this;
//    }
//
//    public CustomAlertDialog setPositiveButton(String text, Boolean isBold, final View.OnClickListener listener) {
//        TextPaint tp = btnAlertDialogPositive.getPaint();
//        tp.setFakeBoldText(isBold);
//
//        setPositiveButton(text, listener);
//        return this;
//    }
//
//    public CustomAlertDialog setPositiveButton(int textId, Boolean isBold, final View.OnClickListener listener) {
//        String text = mContext.getResources().getString(textId);
//        setPositiveButton(text, isBold, listener);
//        return this;
//    }
//
//    public CustomAlertDialog setNegativeButton(String text, final View.OnClickListener listener) {
//        isNegativeButtonShowing = true;
//
//        btnAlertDialogNegative.setVisibility(View.VISIBLE);
//
//        btnAlertDialogNegative.setText(text);
//
//        btnAlertDialogNegative.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != listener) {
//                    listener.onClick(v);
//                }
//                mDialog.dismiss();
//            }
//        });
//        return this;
//    }
//
//    public CustomAlertDialog setNegativeButton(int textId, final View.OnClickListener listener) {
//        String text = mContext.getResources().getString(textId);
//        setNegativeButton(text, listener);
//        return this;
//    }
//
//    public CustomAlertDialog setNegativeButton(String text, Boolean isBold, final View.OnClickListener listener) {
//        TextPaint tp = btnAlertDialogNegative.getPaint();
//        tp.setFakeBoldText(isBold);
//
//        setNegativeButton(text, listener);
//        return this;
//    }
//
//    public CustomAlertDialog setNegativeButton(int textId, Boolean isBold, final View.OnClickListener listener) {
//        String text = mContext.getResources().getString(textId);
//        setNegativeButton(text, isBold, listener);
//        return this;
//    }
//
//    public void show() {
//        if (isPositiveButtonShowing && isNegativeButtonShowing) {
//            vAlertDialogVSep.setVisibility(View.VISIBLE);
//        } else {
//            vAlertDialogVSep.setVisibility(View.GONE);
//        }
//
//        if (isNegativeButtonShowing && !isPositiveButtonShowing) {
//            btnAlertDialogNegative.setBackgroundResource(R.drawable.alert_dialog_single_selector);
//        }
//
//        if (!isNegativeButtonShowing && isPositiveButtonShowing) {
//            btnAlertDialogPositive.setBackgroundResource(R.drawable.alert_dialog_single_selector);
//        }
//
//        mDialog.show();
//
//        isShowing = true;
//    }
//
//    public void dismiss() {
//        mDialog.dismiss();
//        isShowing = false;
//    }
//
//    public Boolean isShowing() {
//        return isShowing;
//    }
//
//
//}