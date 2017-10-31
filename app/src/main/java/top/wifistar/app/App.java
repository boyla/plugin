package top.wifistar.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import cn.bmob.v3.Bmob;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import top.wifistar.R;
import top.wifistar.bean.bmob.UserProfile;
import top.wifistar.corepage.CorePageManager;
import top.wifistar.realm.BaseRealmDao;
import top.wifistar.utils.ACache;
import top.wifistar.utils.CacheUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
public class App extends MultiDexApplication {

    public static App APP_INSTANCE = null;

    private static String TAG = App.class.getSimpleName();

    public final static String DB_NAME = "msgeonames.db";

    public final static String ACACHE_KEY_DB_INSTALLED = "db_installed";

    public static Context mContext;

    private static App sInstance;

    public static int mCurrentState = 1;

    public static LinkedList<Activity> activityStack = new LinkedList<Activity>();

    private static Handler mHandler;

    public static UserProfile currentUserProfile;

    public static int authen_attempt_times = 0;
    public static int conn_attempt_times = 0;

    public static Boolean isApplicationToBackground = true;
//    public NoticeBean cache_noticeBean = new NoticeBean();
//
//    public DbUtils countryDbUtils;

    public static final ExecutorService pool = Executors.newFixedThreadPool(5);
    RealmConfiguration realmConfig;

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }
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

    @Override
    public void onCreate() {
        super.onCreate();

        APP_INSTANCE = this;
        Realm.init(this);
        realmConfig = new RealmConfiguration.Builder().name("UserData.realm").build();
        Realm.setDefaultConfiguration(realmConfig);
        BaseRealmDao.realm = getRealm();
//        if (getApplicationInfo().packageName
//                .equals(getCurProcessName(getApplicationContext()))
//                || "io.rong.push"
//                .equals(getCurProcessName(getApplicationContext()))) {
//
//            /**
//             * IMKit SDK调用第一步 初始化
//             */
//            RongIM.init(this);
//        }

        //第一：默认初始化
        Bmob.initialize(this, "15210abb365601ec97b87f55b1efa0d4");


        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);

        Runtime rt = Runtime.getRuntime();
        long maxMemory = rt.maxMemory();
        int memoryClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        Log.d(TAG, "Max memory: " + Long.toString(maxMemory / (1024 * 1024)));
        Log.d(TAG, "Memory class " + memoryClass);

        sInstance = this;

        mContext = getApplicationContext();
        init();
        CacheUtils.getInstance().init(mContext);

//        registerActivityLifecycleCallbacks(new LifeCycleHander());
//
//        MustacheManager.getInstance().init(getApplicationContext());
//
//        Map<String,String> mutexKeysMap1 = new HashMap<String, String>();
//        mutexKeysMap1.put("32",MustacheManager.MUTEX_KEY_ONE_TO_ALL);
//        MustacheManager.getInstance().getFilterBodyType().mutexKeysMap = mutexKeysMap1;
//        Map<String,String> mutexKeysMap2 = new HashMap<String, String>();
//        mutexKeysMap2.put("64",MustacheManager.MUTEX_KEY_ONE_TO_ALL);
//        MustacheManager.getInstance().getFilterEthnicity().mutexKeysMap = mutexKeysMap2;
//        MustacheManager.getInstance().getFilterSmoking().mutexKeysMap = mutexKeysMap2;


        new DataBaseSyncTask(getDefaultDBInstallDir(), DB_NAME).execute(0);


    }

    private String getDefaultDBInstallDir() {
        String defaultDBInstallDir = "";
        try {
            defaultDBInstallDir = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/"
                    + mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return defaultDBInstallDir;
    }

    public class DataBaseSyncTask extends AsyncTask<Integer, Integer, String> {

        private String dbDir;

        private String dbName;

        public DataBaseSyncTask(String dbDir, String dbName) {
            this.dbDir = dbDir;

            this.dbName = dbName;
        }

        @Override
        protected String doInBackground(Integer... integers) {
            String result = flushDbToSDCard(dbDir, dbName);

            return result;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(String result) {
            if (!TextUtils.isEmpty(result) && result.equals("ok")) {
                //              countryDbUtils = DbUtils.create(getBaseContext(), dbDir, dbName);
            }
        }

        private synchronized String flushDbToSDCard(String dbDir, String dbName) {
            String result = "failed";

            InputStream inputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
                File dbFile = new File(dbDir, dbName);

                ACache mACache = ACache.get(mContext);
                String isDbInstalled = mACache.getAsString(ACACHE_KEY_DB_INSTALLED);
                if (null != isDbInstalled && isDbInstalled.equals("true") && dbFile.exists()) {
                    return "ok";
                }

                inputStream = mContext.getResources().openRawResource(R.raw.msgeonames);
                fileOutputStream = new FileOutputStream(dbFile);
                byte[] buffer = new byte[512];
                int count = 0;

                while ((count = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, count);
                }

                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();

                mACache.put(ACACHE_KEY_DB_INSTALLED, "true");
                result = "ok";
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != fileOutputStream) {
                        fileOutputStream.close();
                    }

                    if (null != inputStream) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return result;
        }
    }

    protected void init() {
        initHandler();
        CorePageManager.getInstance().init(getBaseContext(), "page_config.json");
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                for (int i = 0; i < activityStack.size(); i++) {
                    Activity activity = activityStack.get(i);
                    if (activity instanceof BaseActivity) {
                        ((BaseActivity) activity).dispatcherMessage(msg);
                    }
                }
            }

        };
    }

    public static App getInstance() {
        return sInstance;
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

    public void AppExit() {
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

    public Realm getRealm() {
        return Realm.getInstance(realmConfig);
    }
}
