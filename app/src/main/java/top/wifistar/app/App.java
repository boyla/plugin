package top.wifistar.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.moduth.blockcanary.BlockCanary;
import com.google.gson.Gson;
import com.kongzue.dialog.v2.DialogSettings;
import com.kongzue.dialog.v2.MessageDialog;
import com.lqr.emoji.IImageLoader;
import com.lqr.emoji.LQREmotionKit;

import org.greenrobot.eventbus.EventBus;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import top.wifistar.BuildConfig;
import top.wifistar.activity.SplashActivity;
import top.wifistar.bean.BUser;
import top.wifistar.bean.bmob.User;
import top.wifistar.bean.bmob.UserProfile;
import top.wifistar.corepage.CorePageManager;
import top.wifistar.event.RefreshEvent;
import top.wifistar.im.IMMessageHandler;
import top.wifistar.realm.BaseRealmDao;
import top.wifistar.utils.LogUtils;
import top.wifistar.utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @packagename: com.masonsoft.base
 * @filename: App.java
 * @author: VicentLiu
 * @description:TODO
 * @time: Jan 29, 201511:23:57 AM
 */
public class App extends Application {

    public static App APP_INSTANCE = null;

    private static String TAG = App.class.getSimpleName();

    public final static String DB_NAME = "msgeonames.db";

    public final static String ACACHE_KEY_DB_INSTALLED = "db_installed";

    public static Context mContext;

    public static LinkedList<Activity> activityStack = new LinkedList<Activity>();

    public static Handler mHandler;

    public static UserProfile currentUserProfile;

    public static int authen_attempt_times = 0;
    public static int conn_attempt_times = 0;

    public static String WIFI_HOST;

    public static boolean SELF_WLAN_SERVER_AVALIABLE;

    public static Gson gson = new Gson();

    public static final ExecutorService pool = Executors.newFixedThreadPool(5);
    RealmConfiguration realmConfig;
    public IMMessageHandler imMessageHandler;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
//
//    public static void loadNotification() {
//        RequestHelper.refreshNotice(new HttpHelper(), new CustomRequestCallBack(mContext) {
//            @Override
//            public void onSuccess(String result) {
//                NoticeBean noticeBean = JSON.parseObject(result, NoticeBean.class);
//                App.getInstance().cache_noticeBean = noticeBean;
//                EventBus.getDefault().post(new NoticeEvent(noticeBean));
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//            }
//
//            @Override
//            public void onError(ErrorBean errorBean) {
//            }
//        });
//    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private AppCompatActivity curActivity;

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                curActivity = (AppCompatActivity) activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (curActivity == activity) {
                    curActivity = null;
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
        APP_INSTANCE = this;
        init();

        long last = System.currentTimeMillis();
        Realm.init(APP_INSTANCE);
        realmConfig = new RealmConfiguration.Builder().name("UserData.realm").schemaVersion(1).build();
        Realm.setDefaultConfiguration(realmConfig);
        System.out.println("Runnable Realm.init time : " + (System.currentTimeMillis() - last));

        last = System.currentTimeMillis();
        //Bmob初始化
        Bmob.initialize(APP_INSTANCE, "15210abb365601ec97b87f55b1efa0d4");
        //IM
        String packageName = getApplicationInfo().packageName;
        String processName = getMyProcessName();
        if (packageName.equals(processName)) {
            BmobIM.init(APP_INSTANCE);
            imMessageHandler = new IMMessageHandler(APP_INSTANCE);
            BmobIM.registerDefaultMessageHandler(imMessageHandler);
            System.out.println("Runnable BmobIM.init time : " + (System.currentTimeMillis() - last));
        }

        initPush();
        System.out.println("Runnable initPush time : " + (System.currentTimeMillis() - last));

        last = System.currentTimeMillis();
        Runtime rt = Runtime.getRuntime();
        long maxMemory = rt.maxMemory();
        int memoryClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        Log.d(TAG, "Max memory: " + Long.toString(maxMemory / (1024 * 1024)));
        Log.d(TAG, "Memory class " + memoryClass);
        mContext = getApplicationContext();

        DialogSettings.style = DialogSettings.STYLE_IOS;
        //In this way the VM ignores the file URI exposure
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        //初始化表情包
        LQREmotionKit.init(APP_INSTANCE, new IImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
            }
        });
        System.out.println("Runnable LQREmotionKit.init time : " + (System.currentTimeMillis() - last));
        last = System.currentTimeMillis();
        // init bugly
//        CrashReport.initCrashReport(APP_INSTANCE, getString(R.string.bugly_app_id), BuildConfig.DEBUG);
//        CrashHandler.getInstance().init(APP_INSTANCE);
//        System.out.println("Runnable bugly.init time : " + (System.currentTimeMillis() - last));

        initBlockCanary(this);
    }

    private static void initBlockCanary(Application context) {
        if (BuildConfig.DEBUG) {
            // 在主进程初始化调用哈
            BlockCanary.install(context, new AppBlockCanaryContext()).start();
        }
    }

    protected void init() {
        initHandler();
        CorePageManager.getInstance().init(getBaseContext(), "page_config.json");
    }

    private void initHandler() {
        mHandler = new Handler(Looper.myLooper());
    }

    public static App getInstance() {
        return APP_INSTANCE;
    }


    public static Handler getHandler() {
        return mHandler;
    }

    public void addActivity(Activity activity) {
        activityStack.push(activity);
    }

    public Activity currentActivity() {
        Activity activity = null;
        if (activityStack.size() > 0)
            activity = activityStack.peekFirst();
        return activity;
    }

    public void finishActivity() {
        Activity activity;
        if (activityStack.size() > 0) {
            activity = activityStack.pop();
            activity.finish();
        }
    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    public void finishAllActivity() {
        for (Activity activity : activityStack) {
            activity.finish();
        }
        activityStack.clear();
    }

    public void appExit() {
        try {
            finishAllActivity();
        } catch (Exception ex) {

        }
    }

    public void AppExitNormal() {
        finishAllActivity();
        System.exit(0);
    }

    public static boolean isApplicationBroughtToBackground() {
        ActivityManager am = (ActivityManager) App.getInstance().getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(App.getInstance().getApplicationContext().getPackageName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion() {
        try {
            PackageManager manager = App.mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(App.mContext.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

//    public static int getMessageNum(String user_id) {
//        DBDao db = new DBDao(mContext);
//        return db.countUnReadMessageByUserID(user_id);
//    }

    /**
     * ReportIMLinkAnalysis
     *
     * @param conn_time
     * @param conn_attempt_times
     * @param authen_time
     * @param authen_attempt_times
     * @param status
     */


    public static ArrayList myNoticationList = new ArrayList();

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.w(TAG, "Low memory!");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.w(TAG, "Low memory! " + level);
    }

    public static App getApp() {
        return APP_INSTANCE;
    }

    /**
     * 获取当前运行的进程名
     *
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ConnectionStatus currentIMStatus;

    public static void connectIM() {
        User currentUser = Utils.getCurrentShortUser();
        if (currentUser == null || TextUtils.isEmpty(currentUser.getName())) {
//            Utils.makeSysToast("未取得用户信息,请重新登陆");
            App.getApp().showReloginDialog("提    示", "未取得用户信息,请重新登陆");
            return;
        }
        BmobIM.connect(currentUser.getObjectId(), new ConnectListener() {
            @Override
            public void done(String uid, BmobException e) {
                if (e == null) {
                    //TODO 连接成功后再进行修改本地用户信息的操作，并查询本地用户信息
                    EventBus.getDefault().post(new RefreshEvent());
                    //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                    //TODO 会话：3.6、更新用户资料，用于在会话页面、聊天页面以及个人信息页面显示
                    String url = "";
                    if (!TextUtils.isEmpty(currentUser.getHeadUrl())) {
                        url = currentUser.getHeadUrl().split("_")[0];
                    }
                    BmobIM.getInstance().
                            updateUserInfo(new BmobIMUserInfo(currentUser.getObjectId(),
                                    currentUser.getName(), url));
                } else {
//                    Utils.makeSysToast(e.getMessage());
                    LogUtils.i(e.getMessage());
                }
            }
        });
        //TODO 连接：3.3、监听连接状态，可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus status) {
                /*  DISCONNECT(0, "disconnect"),
                    CONNECTING(1, "connecting"),
                    CONNECTED(2, "connected"),
                    NETWORK_UNAVAILABLE(-1, "Network is unavailable."),
                    KICK_ASS(-2, "kick off by other user");
                **/
                currentIMStatus = status;
                if (status.getCode() == 0 || status.getCode() == -1) {
                    connectIM();
                }
//                Utils.makeSysToast(status.getMsg());
                LogUtils.i(status.getMsg());
            }
        });
    }

    public void showReloginDialog(String title, String message) {
        BUser.logOut();
        if (curActivity == null) {
            return; // 不要忘了判空操作
        }
        MessageDialog msgDialog = MessageDialog.show(curActivity, title, message, "确定", (dialog, which) -> {
//                App.getApp().appExit();
            if (curActivity instanceof SplashActivity) {
                App.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        ((SplashActivity) curActivity).refreshStart();
                    }
                });
                return;
            }
            Intent intent = new Intent(curActivity, SplashActivity.class);
            ComponentName cn = intent.getComponent();
            Intent mainIntent = Intent.makeRestartActivityTask(cn);//ComponentInfo{包名+类名}
            startActivity(mainIntent);
        });
        msgDialog.setCanCancel(false);
    }

    BmobPushManager<BmobInstallation> bmobPush;

    public void pushAndroidMessage(String message, String installId) {
//      bmobPush.pushMessage(message, installId);
        if (bmobPush == null)
            bmobPush = new BmobPushManager();
        BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
        query.addWhereEqualTo("installationId", installId);
        bmobPush.setQuery(query);
        bmobPush.pushMessage(message);
    }

    private void initPush() {
        // 使用推送服务时的初始化操作
        BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
            @Override
            public void done(BmobInstallation bmobInstallation, BmobException e) {
                if (e == null) {
                    Log.i(bmobInstallation.getObjectId() + " push started: ", bmobInstallation.getInstallationId());
                } else {
                    Log.e("push start exception: ", e.getMessage());
                }
            }
        });
        // 启动推送服务
        BmobPush.startWork(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        BaseRealmDao.realm.close();
    }

    public static void checkAndConnectIM() {
        if (App.currentIMStatus == null || !(App.currentIMStatus.getCode() == 2 || App.currentIMStatus.getCode() == 1)) {
            AppExecutor.getInstance().postMain(new Runnable() {
                @Override
                public void run() {
                    App.connectIM();
                }
            });
        }
    }
}
