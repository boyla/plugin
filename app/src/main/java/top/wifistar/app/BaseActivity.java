
package top.wifistar.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import io.realm.Realm;
import top.wifistar.bean.User;
import top.wifistar.corepage.CorePageManager;
import top.wifistar.realm.BaseRealmDao;
import top.wifistar.utils.ACache;
import top.wifistar.utils.CacheUtils;

import java.util.List;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected static final String TAG = BaseActivity.class.getSimpleName();

    private boolean isFinished = false;

    protected Context mContext;

    protected Activity mActivity;

    protected Resources mResources;

    protected InputMethodManager imm;

    protected Bundle mSavedInstanceState;

    protected ACache myCache;

    public static BaseActivity currentActivity;

    public Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BaseRealmDao.realm = Realm.getDefaultInstance();

        realm = BaseRealmDao.realm;

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(null);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        App.getInstance().addActivity(this);

        mSavedInstanceState = savedInstanceState;

        mContext = this;

        mActivity = this;

        mResources = getResources();

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        setContentBefore();

        getExtraData();

        initCache();

        initUI();

        //register network monitor receiver
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

    }

    private void initCache() {
        User userBean = CacheUtils.getInstance().getUserBean();
        if (userBean != null && userBean.id() != null) {
            myCache = ACache.get(getApplicationContext(), userBean.id());
        } else {
            myCache = ACache.get(getApplicationContext(), "guest");
        }
    }

    protected void setContentBefore(){

    }

    protected void getExtraData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentActivity = this;
    }

    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void finish() {
        isFinished = true;
        super.finish();
    }


    protected void initUI(){}

    public static Class<? extends BaseActivity> topClassActivity;

    public static boolean isApplicationBroughtToBackground(Application app) {
        ActivityManager am = (ActivityManager) app.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(app.getApplicationContext().getPackageName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (App.isApplicationToBackground && CacheUtils.getInstance().getUserBean() != null && CacheUtils.getInstance().getUserBean().id() != null) {
            App.isApplicationToBackground = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        App.isApplicationToBackground = true;
    }

    public void dispatcherMessage(Message msg) {
    }

    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    protected <T extends View> T bindViewById(int resId) {
        return (T) super.findViewById(resId);
    }

    protected <T extends View> T bindViewById(int resId, boolean isSetOnClickListener) {
        T view = (T) super.findViewById(resId);
        if (isSetOnClickListener) {
            view.setOnClickListener(this);
        }
        return view;
    }

    @Override
    protected void onDestroy() {
        mActivity  = null;
        if(App.getInstance().currentActivity() == this){
            App.activityStack.pop();
        }else{
            App.activityStack.remove(this);
        }
//        this.unregisterReceiver(myReceiver);
        super.onDestroy();
        realm.close();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Activity aty = BaseActivity.this;
//        if (aty.getCurrentFocus() != null) {
//            if (aty.getCurrentFocus().getWindowToken() != null) {
//                imm.hideSoftInputFromWindow(aty.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        }

        return super.dispatchTouchEvent(ev);
    }

    public void closeKeyBoard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public Fragment gotoPage(int fragmentContainerID, String pageName, String prePage) {
        return CorePageManager.getInstance().gotoPage(fragmentContainerID, getSupportFragmentManager(), pageName, prePage, null, null);
    }

    public void openKeyBoard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInputFromInputMethod(view.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {

    }


}