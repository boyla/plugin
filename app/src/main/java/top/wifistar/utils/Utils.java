package top.wifistar.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.alibaba.fastjson.JSON;
//import com.am.utility.utils.ToastHelper;
//import com.lidroid.xutils.exception.HttpException;
//import com.mason.sociality.lib.R;
//import com.mason.sociality.lib.app.BaseActivity;
//import com.mason.sociality.lib.app.App;
//import com.mason.sociality.lib.bean.ErrorBean;
//import com.mason.sociality.lib.bean.profile.UserBean;
//import com.mason.sociality.lib.bean.profile.UserSerializeBean;
//import com.mason.sociality.lib.config.ActivityIntentConfig;
//import com.mason.sociality.lib.corepage.CorePageManager;
//import com.mason.sociality.lib.db.UniversalDao;
//import com.mason.sociality.lib.dialog.CustomProgressDialog;
//import com.mason.sociality.lib.http.CustomRequestCallBack;
//import com.mason.sociality.lib.http.HttpHelper;
//import com.mason.sociality.lib.http.RequestHelper;
//import com.mason.sociality.lib.manager.LocationCustomManager;
//import com.mason.sociality.lib.xmpp.XMPPManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.kongzue.dialog.v2.Notification;
import com.scottyab.aescrypt.AESCrypt;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import io.realm.RealmResults;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import top.wifistar.R;
import top.wifistar.activity.ChatActivity;
import top.wifistar.activity.HomeActivity;
import top.wifistar.activity.UserProfileActivity;
import top.wifistar.app.App;
import top.wifistar.bean.BUser;
import top.wifistar.bean.bmob.UserProfile;
import top.wifistar.bean.bmob.User;
import top.wifistar.bean.bmob.BmobUtils;
import top.wifistar.chain.user.NetUserRequest;
import top.wifistar.chain.user.UserChainHandler;
import top.wifistar.httpserver.AppImageUrl;
import top.wifistar.httpserver.WiFiServerService;
import top.wifistar.view.CircleImageView;
import top.wifistar.view.TopReminder;
import top.wifistar.realm.BaseRealmDao;
import top.wifistar.realm.UserRealm;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;
import static com.kongzue.dialog.v2.Notification.TYPE_NORMAL;
import static top.wifistar.utils.ACache.SHORT_USER_ID_CACHE;

/**
 * @packagename: com.masonsoft.base.utils
 * @filename: Utils.java
 * @author: VicentLiu
 * @description:TODO
 * @time: Jan 30, 2015 3:35:16 PM
 */
public class Utils {

    private final static String TAG = Utils.class.getSimpleName();
    public static String time_format_time_zone = "yyyy-MM-dd'T'HH:mm:ss";
    private static DateFormat format = new SimpleDateFormat(time_format_time_zone);
    private static String time_format1 = "HH:mm";
    private static SimpleDateFormat sdf1 = new SimpleDateFormat(time_format1);
    private static String time_format2 = "HH:mm";
    private static SimpleDateFormat sdf2 = new SimpleDateFormat(time_format2);
    private static String time_format3 = "HH:mm";
    private static SimpleDateFormat sdf3 = new SimpleDateFormat(time_format3);
    private static String time_format4 = "MM/dd HH:mm";
    private static SimpleDateFormat sdf4 = new SimpleDateFormat(time_format4);
    private static String time_format5 = "MM/dd/yy HH:mm";
    private static SimpleDateFormat sdf5 = new SimpleDateFormat(time_format5);

    private static String time_format6 = "MM/dd";
    private static SimpleDateFormat sdf6 = new SimpleDateFormat(time_format6);
    private static String time_format7 = "MM/dd/yy";
    private static SimpleDateFormat sdf7 = new SimpleDateFormat(time_format7);
    public static String time_format8 = "M/d/yyyy";
    private static SimpleDateFormat sdf8 = new SimpleDateFormat(time_format8);

    public static long getTimeMillis(String time) {
        Date date;
        try {
            if (time != null) {
                date = format.parse(time);
            } else {
                date = new Date();
            }
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void startPaymentService() {
        try {
            Class<?> clazz = Class.forName("com.mason.sociality.payment.service.GooglePlayServiceImpl");
            Method instanceMethod = clazz.getMethod("getInstance", Context.class);
            Method startupMethod = clazz.getMethod("startSetup");
            Object service = instanceMethod.invoke(null, App.getInstance());
            startupMethod.invoke(service);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

//    public static String transformTimeInside(String time, boolean isLocalTime) {
//        if (time == null || "".equals(time)) return "";
//        try {
//            Date date = format.parse(time);
//            date = new Date(date.getTime() + TimeZone.getDefault().getRawOffset());
//            String ss = "";
//            long tempTime = System.currentTimeMillis();
//            if ((tempTime - date.getTime()) / (24 * 60 * 60 * 1000) == 0) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(date);
//                int day1 = calendar.get(Calendar.DAY_OF_MONTH);
//                calendar.setTimeInMillis(System.currentTimeMillis());
//                int day2 = calendar.get(Calendar.DAY_OF_MONTH);
//                if (day2 - day1 > 0) {
//                    ss = App.getInstance().getResources().getString(R.string.Yesterday) + " " + sdf2.format(date);
//                } else {
//                    ss = sdf1.format(date);
//                }
//            } else if ((tempTime - date.getTime()) / (24 * 60 * 60 * 1000) == 1) {
//                ss = App.getInstance().getResources().getString(R.string.Yesterday) + " " + sdf2.format(date);
//            } else if ((tempTime - date.getTime()) / (24 * 60 * 60 * 1000) <= 6) {
//                ss = getWeekOfDate(date) + " " + sdf3.format(date);
//            } else {
//                Date today = new Date();
//                if (date.getYear() == today.getYear()) {
//                    ss = sdf4.format(date);
//                    String months = ss.split("/")[1];
//                    ss = getMonthOfDate(date) + " " + months;
//                } else {
//                    ss = sdf5.format(date);
//                }
//            }
//            return ss;
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//    @SuppressWarnings("deprecation")
//    public static String transformTimeOutSide(String time, boolean isLocalTime) {
//        if (time == null || "".equals(time)) return "";
//        try {
//            Date date = format.parse(time);
//            date = new Date(date.getTime() + TimeZone.getDefault().getRawOffset());
//            String ss = "";
//            long tempTime = System.currentTimeMillis();
//            if ((tempTime - date.getTime()) / (24 * 60 * 60 * 1000) == 0) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(date);
//                int day1 = calendar.get(Calendar.DAY_OF_MONTH);
//                calendar.setTimeInMillis(System.currentTimeMillis());
//                int day2 = calendar.get(Calendar.DAY_OF_MONTH);
//                if (day2 - day1 > 0) {
////                    ss = App.getInstance().getResources().getString(R.string.Yesterday) + " " + sdf2.format(date);
//                    ss = App.getInstance().getResources().getString(R.string.Yesterday);
//                } else {
//                    ss = App.getInstance().getResources().getString(R.string.Today);
//                }
//            } else if ((tempTime - date.getTime()) / (24 * 60 * 60 * 1000) == 1) {
////                    ss = App.getInstance().getResources().getString(R.string.Yesterday) + " " + sdf2.format(date);
//                ss = App.getInstance().getResources().getString(R.string.Yesterday);
//            } else if ((tempTime - date.getTime()) / (24 * 60 * 60 * 1000) <= 6) {
//                ss = getWeekOfDate(date);
//            } else {
//                Date today = new Date();
//                if (date.getYear() == today.getYear()) {
//                    ss = sdf6.format(date);
//                } else {
//                    ss = sdf6.format(date);
//                }
//            }
//            return ss;
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

//    public static String getWeekOfDate(Date dt) {
//        String[] weekDays = App.getInstance().getResources().getStringArray(R.array.week);
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(dt);
//        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
//        if (w < 0)
//            w = 0;
//        return weekDays[w];
//    }

//    public static String getMonthOfDate(Date dt) {
//        String[] monthDays = App.getInstance().getResources().getStringArray(R.array.month);
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(dt);
//        int m = cal.get(Calendar.MONTH) - 1;
//        if (m < 0)
//            m = 0;
//        return monthDays[m];
//    }


    //--------------------------------------about network-----------------------------------------//
    private static boolean netConnect = false;

    public static boolean isNetConnect() {
        return netConnect;
    }

    public static void setNetConnect(boolean netConnect) {
        Utils.netConnect = netConnect;
    }

    /**
     * check the network
     *
     * @param context
     * @return
     */
    public static boolean isConnect(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        Utils.setNetConnect(true);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * check network and jump to setting
     *
     * @param context
     * @return
     */
//    public static boolean isConnectAndJumpToSetting(final Context context) {
//
//        if (!Utils.isConnect(context)) {
//            final Dialog alertDialog = new Dialog(context, R.style.popup_dialog);
//            alertDialog.setContentView(R.layout.dialog_ok_cancel_view);
//            TextView mMainBodyText = (TextView) alertDialog.findViewById(R.id.dialog_mainbody_text);
//            mMainBodyText.setText(R.string.network_disconnected);
//            TextView mOKBtn = (TextView) alertDialog.findViewById(R.id.dialog_ok_btn);
//            TextView mCancelBtn = (TextView) alertDialog.findViewById(R.id.dialog_cancel_btn);
//            mOKBtn.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (android.os.Build.VERSION.SDK_INT > 10) {
//                        context.startActivity(new Intent(Settings.ACTION_SETTINGS));
//                    } else {
//                        context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
//                    }
//                    alertDialog.cancel();
//                }
//            });
//            mCancelBtn.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    alertDialog.cancel();
//                }
//            });
//            alertDialog.show();
//            return true;
//        } else {
//            return false;
//        }
//    }

    /**
     * Check whether the GPS to open
     *
     * @param context
     * @return
     */
    public static boolean isGPSOpen(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }


    public static boolean isMyGPSOpen(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return gps;
    }


    public static boolean isNetWorkGPSOpen(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (network) {
            return true;
        }
        return false;
    }

    /**
     * Remove duplicated data in list
     *
     * @param list: original list
     * @return new list
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List<?> removeDuplicateWithOrder(List<?> list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            Object element = iterator.next();
            if (set.add(element))
                newList.add(element);
        }
        list.clear();
        list.addAll(newList);
        return list;
    }

//    public static String getSmallPictureUrl(String url) {
//        if (url == null) return "";
//        int width = (int) (200 * ScreenUtils.getDensity(App.getInstance()));
//        return url + "?w=" + width;
//    }

    public static final String ENTER_VIEW_TYPE = "enterviewType";

    /**
     * Go to upgrade view
     *
     * @param context
     * @param enterviewType: the enter view's type,represent different view
     */
    public static void goToUpgradeView(Context context, int enterviewType, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(ENTER_VIEW_TYPE, enterviewType);
        context.startActivity(intent);
    }

    /**
     * calculate Age
     *
     * @param year
     * @param month
     * @param day
     * @return mTextviewAge
     */
    public static int calculateAge(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        int yearBirth = year;
        int monthBirth = month;
        int dayOfMonthBirth = day;
        if ((yearBirth > yearNow)
                || (yearBirth == yearNow && monthBirth > monthNow)
                || (yearBirth == yearNow && monthBirth == monthNow && dayOfMonthBirth > dayOfMonthNow)) {
            return -1;
        }
        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return age;
    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private static long lastClickTime = 0;

    /**
     * To judge that user click fast or not, avoiding click duplicate
     *
     * @return
     */
    public synchronized static boolean isFastClick() {
        long curTime = System.currentTimeMillis();
        if (curTime - lastClickTime < 800)
            return true;
        lastClickTime = curTime;
        return false;
    }

    /**
     * this method can remove repeat item
     *
     * @return List
     */
    public static <T> List<T> removeArrayRepeat(List<T> list) {
        if (list.isEmpty()) {
            return null;
        }
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }
        return list;
    }

//    public static void openNetWorkGPSSettings(final Context context, final Runnable ok, final Runnable cancel) {
//        final Dialog alertDialog = new Dialog(context, R.style.popup_dialog);
//        alertDialog.setContentView(R.layout.dialog_ok_cancel_view);
//        TextView mMainBodyText = (TextView) alertDialog.findViewById(R.id.dialog_mainbody_text);
//        mMainBodyText.setText(R.string.location_open_network_gps_tips);
//        TextView mOKBtn = (TextView) alertDialog.findViewById(R.id.dialog_ok_btn);
//        TextView mCancelBtn = (TextView) alertDialog.findViewById(R.id.dialog_cancel_btn);
//        mOKBtn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                try {
//                    context.startActivity(intent);
//                } catch (ActivityNotFoundException ex) {
//                    intent.setAction(Settings.ACTION_SETTINGS);
//                    try {
//                        context.startActivity(intent);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                alertDialog.cancel();
//                ok.run();
//            }
//        });
//        mCancelBtn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.cancel();
//                cancel.run();
//            }
//        });
//        alertDialog.setOnCancelListener(new OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                alertDialog.cancel();
//                cancel.run();
//            }
//        });
//        alertDialog.show();
//    }

//    /**
//     * When the GPS attempt failed, the pop-up fullImageDialog
//     */
//    public static void createLocationSelectDialog(final Context context, final Runnable gps, final Runnable select) {
//        final Dialog alertDialog = new Dialog(context, R.style.popup_dialog);
//        alertDialog.setContentView(R.layout.dialog_ok_cancel_view);
//        TextView title = (TextView) alertDialog.findViewById(R.id.dialog_title);
////        title.setText(R.string.Failed_to_Get_Location);
//        TextView mMainBodyText = (TextView) alertDialog.findViewById(R.id.dialog_mainbody_text);
//        mMainBodyText.setText(R.string.Failed_to_Get_Location_content);
//        TextView mOKBtn = (TextView) alertDialog.findViewById(R.id.dialog_ok_btn);
//        mOKBtn.setText(R.string.Locate_Again);
//        TextView mCancelBtn = (TextView) alertDialog.findViewById(R.id.dialog_cancel_btn);
//        mCancelBtn.setText(R.string.Select_Manually);
//        mOKBtn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.cancel();
//                select.run();
//            }
//        });
//        mCancelBtn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.cancel();
//                gps.run();
//            }
//        });
//        alertDialog.show();
//    }

//    /**
//     * open gps settings fullImageDialog
//     *
//     * @param context
//     */
//    public static void openGPS(final Context context, final Runnable cancelRunnable) {
//        final Dialog alertDialog = new Dialog(context, R.style.popup_dialog);
//        alertDialog.setContentView(R.layout.dialog_ok_cancel_view);
//        TextView mMainBodyText = (TextView) alertDialog.findViewById(R.id.dialog_mainbody_text);
//        mMainBodyText.setText(R.string.location_open_gps_tips);
//        TextView mOKBtn = (TextView) alertDialog.findViewById(R.id.dialog_ok_btn);
//        TextView mCancelBtn = (TextView) alertDialog.findViewById(R.id.dialog_cancel_btn);
//        mOKBtn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                try {
//                    context.startActivity(intent);
//                } catch (ActivityNotFoundException ex) {
//                    intent.setAction(Settings.ACTION_SETTINGS);
//                    try {
//                        context.startActivity(intent);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                alertDialog.cancel();
//            }
//        });
//        mCancelBtn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//                cancelRunnable.run();
//            }
//        });
//        alertDialog.show();
//    }
//
//    public static void logout(Context context, Bundle bundle, String activityNameAlias, Boolean isShowloading) {
//        UserBean userBean = CacheUtils.getInstance().getUserBean();
//        if (userBean == null)
//            return;
//        final Context c = context;
//        final String ana = activityNameAlias;
//        final Bundle b = bundle;
//        HttpHelper httpHelper = new HttpHelper();
//
//        CustomProgressDialog myDialog = null;
//        if (isShowloading && context instanceof BaseActivity) {
//            myDialog = ProgressDialogUtil.getCustomProgressDialog(context);
//            BaseActivity activity = (BaseActivity) context;
//            if (!activity.isFinished()) {
//                myDialog.show();
//            }
//        }
//
//        final CustomProgressDialog dialog = myDialog;
//
//        RequestHelper.loginOut(httpHelper, new CustomRequestCallBack(context) {
//            @Override
//            public void onSuccess(String result) {
//                cancelDialog(dialog);
//
//                XMPPManager.getInstance().closeConnection();
//                NotificationManager notificationManager = (NotificationManager) c
//                        .getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.cancelAll();
//                App.getInstance().finishAllActivity();
//
//                UniversalDao dao = new UniversalDao(c);
//                dao.deleteAllUser();
//                CacheUtils.getInstance().setUserBean(null);
//                LocationCustomManager.instance = null;
//
//                ActivityUtils.openPage(c, ana, b, Intent.FLAG_ACTIVITY_NEW_TASK);
//            }
//
//            @Override
//            public void onError(ErrorBean errorBean) {
//                cancelDialog(dialog);
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                super.onFailure(error, msg);
//                cancelDialog(dialog);
//            }
//        });
//    }

    public static void cancelDialog(Dialog d) {
        try {
            if (d != null && d.isShowing())
                d.cancel();
        } catch (Exception e) {

        }
    }

//    public static void logout(Context context) {
//        Bundle bundle = new Bundle();
//        bundle.putBoolean("disabilitySplash", true);
//        logout(context, bundle, ActivityIntentConfig.ACTIVITY_INTENT_SPLASH, true);
//    }
//
//    public static void loginConflict(final Context context) {
//        final Dialog alertDialog = new Dialog(context, R.style.Transparent);
//        alertDialog.setContentView(R.layout.dialog_logout_view);
//        TextView mContentTextView = (TextView) alertDialog.findViewById(R.id.relogin_title);
//        String mContent = context.getResources().getString(R.string.relogin_title);
//        String amStr = "am";
//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a MMM dd, yyyy",
//                Locale.ENGLISH);
//        String dateStr = sdf.format(date);
////        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 12) {
////            amStr = "pm";
////        }
////        int hour = Calendar.getInstance().get(Calendar.HOUR);
////        if (hour == 0) {
////            hour = 12;
////        }
////        String timeStr = hour + ":" + (Calendar.getInstance().get(Calendar.MINUTE) >= 10 ? Calendar.getInstance().get(Calendar.MINUTE) : "0" + Calendar.getInstance().get(Calendar.MINUTE)) + " " + amStr;
//        mContent = String.format(mContent, dateStr);
//        mContentTextView.setText(mContent);
//        TextView mOKBtn = (TextView) alertDialog.findViewById(R.id.dialog_ok_btn);
//        TextView mCancelBtn = (TextView) alertDialog.findViewById(R.id.dialog_cancel_btn);
//        alertDialog.setCancelable(false);
//        mOKBtn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                login(App.getInstance().currentActivity(), null);
//                alertDialog.cancel();
//            }
//        });
//        mCancelBtn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.cancel();
//                CacheUtils.getInstance().setUserBean(null);
//            }
//        });
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.cancelAll();
//        if (context instanceof Activity) {
//            ScreenUtils.hideSoftKeyboardIfShow((Activity) context);
//        }
//        alertDialog.show();
//    }
//
//    public static void login(final Context context, final CustomRequestCallBack callback) {
//        final CustomProgressDialog loadingDialog = new CustomProgressDialog(context);
//        loadingDialog.show();
//        UniversalDao db = new UniversalDao(context);
//        UserBean userBean = db.queryUser();
//        if (StringUtils.isNotEmpty(userBean.getUsername())
//                && StringUtils.isNotEmpty(userBean.getPassword())) {
//            UserSerializeBean.userBean.setNickName(userBean.getUsername());
//            UserSerializeBean.userBean.setPassword(userBean.getPassword());
//            UserSerializeBean.userBean.setUserID(userBean.getUsr_id());
//            HttpHelper httpHelper = new HttpHelper();
//
//            RequestHelper.login(httpHelper, userBean.getUsername(), userBean.getPassword(), new CustomRequestCallBack(context) {
//                @Override
//                public void onSuccess(String result) {
//                    if (!TextUtils.isEmpty(result) && result.contains("<html>")) {
//                        ToastUtils.textToast(R.string.request_failed);
//                        loadingDialog.dismiss();
//                        return;
//                    }
//                    CacheUtils.getInstance().setUserBean(JSON.parseObject(result, UserBean.class));
//                    UserSerializeBean.userBean.setNickName(CacheUtils.getInstance().getUserBean().getUsername());
//                    XMPPManager.getInstance().login();
//                    loadingDialog.dismiss();
//                    Class<?> activity = CorePageManager.getInstance().getPageByName(ActivityIntentConfig.ACTIVITY_INTENT_MAIN_INTERFACE);
//                    Intent intent = new Intent(context, activity);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                }
//
//                @Override
//                public void onError(ErrorBean errorBean) {
//                    loadingDialog.dismiss();
//                    ToastUtils.textToast(errorBean.getErrmsg());
//                }
//
//                @Override
//                public void onFailure(HttpException error, String msg) {
//                    super.onFailure(error, msg);
//                    loadingDialog.cancel();
//                }
//            });
//        } else {
//            loadingDialog.dismiss();
//        }
//    }

    /**
     * 保存文件
     *
     * @param bm
     * @param fileName
     */
    public static File getFile(Bitmap bm, String fileName) {
        try {
            String path = Environment.getExternalStorageDirectory() + "/temp/";
            File dirFile = new File(path);
            if (!dirFile.exists()) {
                dirFile.mkdir();
            }
            File myCaptureFile = new File(path + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);

            bos.close();
            return myCaptureFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    //获得独一无二的Psuedo ID
    public static String getUniqueID() {
        String serial = null;

//        String timeStamp = System.currentTimeMillis() + "";
//        String randomNum = (new Random().nextInt(10000) + 10000) + "";

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

//    public static String encrypt(String clearStr) {
//        if (TextUtils.isEmpty(clearStr))
//            return "";
//        String result = "";
//        try {
//            result = AESCrypt.encrypt(Utils.getUniqueID(), clearStr);
//        }catch (GeneralSecurityException e){
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    public static String encrypt(String psw, String clearStr) {
//        if (TextUtils.isEmpty(clearStr))
//            return "";
//        String result = "";
//        try {
//            result = AESCrypt.encrypt(psw, clearStr);
//        }catch (GeneralSecurityException e){
//            e.printStackTrace();
//        }
//        return result;
//    }

    public static String decrypt(String decryptedStr) {
        if (TextUtils.isEmpty(decryptedStr))
            return "";
        String result = "";
        try {
            result = AESCrypt.decrypt(Utils.getUniqueID(), decryptedStr);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String decrypt(String psw, String decryptedStr) {
        if (TextUtils.isEmpty(decryptedStr))
            return "";
        String result = "";
        try {
            result = AESCrypt.decrypt(psw, decryptedStr);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void showToast(String str) {
        Toast.makeText(App.mContext, str, Toast.LENGTH_LONG).show();
    }

    public static void closeKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(),
                    0);
        }
    }

    public static void showToast(TopReminder topReminder, String string) {
        topReminder.setTheme(TopReminder.THEME_WARNING);
        topReminder.show(string, false, true);
    }

    public static void showToast(TopReminder topReminder, String string, int themeType) {
        topReminder.setTheme(themeType);
        topReminder.show(string, false, true);
    }

    public static int getLevelColor(Context mContext) {
        int color;
//        SharedPreferences sp = mContext.getSharedPreferences("cache_data",MODE_PRIVATE);
//        sp.edit().putInt("CURRENT_LEVEL",++lv).commit();
        SharedPreferences sp = mContext.getSharedPreferences("cache_data", MODE_PRIVATE);
        int level = sp.getInt("CURRENT_LEVEL", 1);
        switch (level) {
            case 1:
                color = mContext.getResources().getColor(R.color.level_1);
                break;
            case 2:
                color = mContext.getResources().getColor(R.color.level_2);
                break;
            case 3:
                color = mContext.getResources().getColor(R.color.level_3);
                break;
            case 4:
                color = mContext.getResources().getColor(R.color.level_4);
                break;
            case 5:
                color = mContext.getResources().getColor(R.color.level_5);
                break;
            case 6:
                color = mContext.getResources().getColor(R.color.level_6);
                break;
            default:
                color = mContext.getResources().getColor(R.color.level_1);
        }
        return color;
    }

    public static int getLevelColorID(Context mContext) {
        SharedPreferences sp = mContext.getSharedPreferences("cache_data", MODE_PRIVATE);
        int level = sp.getInt("CURRENT_LEVEL", 1);
        switch (level) {
            case 1:
                return R.color.level_1;
            case 2:
                return R.color.level_2;
            case 3:
                return R.color.level_3;
            case 4:
                return R.color.level_4;
            case 5:
                return R.color.level_5;
            case 6:
                return R.color.level_6;
            default:
                return R.color.level_1;
        }
    }

    public static void setUserAvatar(CircleImageView mCustomLogo) {
        User user = Utils.getCurrentShortUser();
        if (user != null) {
            mCustomLogo.setVisibility(View.INVISIBLE);
            setUserAvatar(user, mCustomLogo);
            mCustomLogo.setVisibility(View.VISIBLE);
        } else {
            App.getHandler().postDelayed(() -> {
                setUserAvatar(mCustomLogo);
            }, 444);
        }
    }

    public static void setUserAvatar(User shortProfile, ImageView imageView) {
        setUserAvatar(shortProfile, imageView, true);
    }

    public static void setUserAvatar(User shortProfile, ImageView imageView, boolean needJumpPage) {
        Context context = imageView.getContext();
        String headImg = shortProfile.getHeadUrl();
        if (!TextUtils.isEmpty(headImg) && headImg.contains("_wh_")) {
            headImg = headImg.split("_wh_")[0];
        }
        if (TextUtils.isEmpty(headImg) || "null".equals(headImg)) {
            int img;
            if (shortProfile.sex == 0) {
                img = R.drawable.default_avartar_female;
            } else {
                img = R.drawable.default_avartar_male;
            }
            Glide.with(context)
                    .load(img)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new GlideCircleTransform(context))
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(new AppImageUrl(shortProfile.getObjectId(), headImg))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new GlideCircleTransform(context))
                    .into(imageView);
        }

//        if (shortProfile == null || shortProfile.sex == null) {
//            Glide.with(context).load(R.drawable.default_avartar_male).into(imageView);
//            return;
//        } else if (!TextUtils.isEmpty(shortProfile.getHeadUrl())) {
//            Glide.with(context).load(shortProfile.getHeadUrl())
//                    .placeholder(null)
//                    .bitmapTransform(new CropCircleTransformation(context))
//                    .into(imageView);
//        } else if (shortProfile.sex == 0) {
//            Glide.with(context).load(R.drawable.default_avartar_female).into(imageView);
//        } else if (shortProfile.sex == 1) {
//            Glide.with(context).load(R.drawable.default_avartar_male).into(imageView);
//        }

        if (needJumpPage) {
            imageView.setOnClickListener(v -> {
                queryShortUser(shortProfile.getObjectId(), new NetUserRequest.NetRequestCallBack() {
                    @Override
                    public void onSuccess(User user) {
                        jumpToProfile(context, user, imageView);
                    }

                    @Override
                    public void onFailure(String msg) {
                        jumpToProfile(context, shortProfile, imageView);
                    }
                });
            });
        }
    }

    public static void jumpToProfile(Context context, User user, ImageView imageView) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ShortUser", user);
        if (context instanceof ChatActivity) {
            bundle.putBoolean("isFromChat", true);
        }
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && imageView != null) {
            // 创建一个包含过渡动画信息的 ActivityOptions 对象
            Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, imageView, App.getApp().getString(R.string.transition_name_head_img)).toBundle();
            // 使用 Intent 跳转界面，并传递共享对象信息
            ActivityCompat.startActivity(context, intent, options);
        } else {
            context.startActivity(intent);
        }
    }


    public static void setUserAvatar(Context context, UserRealm profile, ImageView imageView) {
        if (profile == null || profile.sex == null) {
            return;
        } else if (!TextUtils.isEmpty(profile.headUrl)) {
            Glide.with(context).load(profile.headUrl.split("_")[0])
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(imageView);
        } else if (0 == profile.sex) {
            Glide.with(context).load(R.drawable.default_avartar_female)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(imageView);
        } else if (1 == profile.sex) {
            Glide.with(context).load(R.drawable.default_avartar_male)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(imageView);
        }
    }

    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception var3) {
            return defValue;
        }
    }

    public static int stringToInt(String str) {
        int i;
        if (str == null || str.trim().length() == 0) {
            str = "0";
        }

        try {
            i = Integer.valueOf(str);
        } catch (Exception e) {
            i = 0;
            e.printStackTrace();
        }

        return i;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void updateUserFromProfile(User user) {
        UserProfile profile = App.currentUserProfile;
        User res;
        if (user == null) {
            user = Utils.getCurrentShortUser();
            if (user == null) {
                try {
                    Thread.sleep(300);
                    user = Utils.getCurrentShortUser();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (user == null) {
                res = new User();
                String objId = ACache.get(App.getInstance()).getAsString("SHORT_USER_ID_" + BUser.getCurrentUser().getObjectId());
                res.setObjectId(objId);
                BmobUtils.updateUser(res, profile.getObjectId(), profile.getNickName(), profile.sex);
            }
        } else {
            BmobUtils.updateUser(user);
        }
        App.getHandler().postDelayed(() -> {
            HomeActivity.INSTANCE.refreshTopAvatar();
        }, 566);
    }

    public static User getCurrentShortUser() {
        if (BUser.getCurrentUser() == null) {
            return null;
        }
        String id = ACache.get(App.getInstance()).getAsString("SHORT_USER_ID_" + BUser.getCurrentUser().getObjectId());
        RealmResults<UserRealm> dbData = BaseRealmDao.realm.where(UserRealm.class).equalTo("objectId", id).findAll();
        if (dbData.isLoaded()) {
            // 完成查询
            if (!dbData.isEmpty()) {
                UserRealm userRealm = dbData.first();
                if (userRealm != null) {
                    if (TextUtils.isEmpty(userRealm.name)) {
                        queryUserByNet(id, null);
                    }
                    User res = userRealm.toBmobObject();
                    res.ip = WiFiServerService.hostAddress;
                    return res;
                }
            }
        }
        return null;
    }

    //在不加载图片的前提下获得图片的宽高
    public static String getImageUrlWithWidthHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        return "_wh_" + options.outWidth + "&" + options.outHeight;
    }

    public static boolean isWifiConnected() {
        Context context = App.getApp();
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isInWifi() {
        Context context = App.getApp();
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return true;
            }
        }
        return false;
    }

    public static int getConnectedType() {
        Context context = App.getApp();
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    public static boolean isMobileConnected() {
        Context context = App.getApp();
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isNetworkConnected() {
        Context context = App.getApp();
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static int STATUS_BAR_HEIGHT;

    private static View flTop;

    public static void makeToast(Activity context, String content) {
        makeToast(context, content, 1, false);
    }

    public static void makeToast(Activity context, String content, int time, boolean isBottom) {
        FrameLayout flContent = (FrameLayout) context.getWindow().getDecorView().findViewById(android.R.id.content);
        flTop = flContent.findViewById(R.id.fl_top);
        boolean firstUse = false;
        if (flTop == null) {
            flTop = context.getLayoutInflater().inflate(R.layout.top_reminder, null);
            firstUse = true;
        }
        View status_bar = flTop.findViewById(R.id.status_bar);
        TextView tvReminder = (TextView) flTop.findViewById(R.id.tvReminder);
        //LinearLayout llReminder = (LinearLayout) flTop.findViewById(R.id.llReminder);
        View card_view = flTop.findViewById(R.id.card_view);
        //set margin top to status bar
        if (STATUS_BAR_HEIGHT == 0) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            STATUS_BAR_HEIGHT = context.getResources().getDimensionPixelSize(resourceId);
        }
        status_bar.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        STATUS_BAR_HEIGHT));
        tvReminder.setText(content);
        if (firstUse) {
            card_view.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
            flContent.addView(flTop);
            if (isBottom) {
                flTop.post(() -> {
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = Gravity.BOTTOM;
                    layoutParams.setMargins(0, 0, 0, DensityUtil.dp2px(context, 50));
                    flTop.setLayoutParams(layoutParams);
                });
            }
        } else {
            flTop.setVisibility(View.VISIBLE);
        }
        if (time > 0) {
            flTop.postDelayed(() -> cancelToast(context), 3500);
        }
    }

    public static void cancelToast(Activity context) {
        FrameLayout flContent = (FrameLayout) context.getWindow().getDecorView().findViewById(android.R.id.content);
        flTop = flContent.findViewById(R.id.fl_top);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(400);
        if (flTop != null) {
            flTop.startAnimation(alphaAnimation);
            flTop.setVisibility(View.GONE);
        }
    }

    public static void makeSysToast(String str) {
        Toast.makeText(App.getApp(), str, Toast.LENGTH_LONG).show();
    }

    public static void showSimpleDialog(Context context, String title, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme);
        builder.setTitle(title)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", listener);
        builder.show();
    }

    public static boolean isEmpty(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        } else if ("null".equals(str)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getFuzzyTime2(long createTime) {
        long current = System.currentTimeMillis();
        SimpleDateFormat resSdf;
        SimpleDateFormat isSameDay = new SimpleDateFormat("yyyy-MM-dd");
        if (isSameDay.format(new Date(createTime)).equals(isSameDay.format(new Date(current)))) {
            resSdf = new SimpleDateFormat("HH:mm");
            return resSdf.format(new Date(createTime));
        }
        SimpleDateFormat isSameYear = new SimpleDateFormat("yyyy");
        if (isSameYear.format(new Date(createTime)).equals(isSameYear.format(new Date(current)))) {
            resSdf = new SimpleDateFormat("MM月dd日");
            return resSdf.format(new Date(createTime));
        } else {
            resSdf = new SimpleDateFormat("yyyy年MM月dd日");
            return resSdf.format(new Date(createTime));
        }
    }


    static Map<String, User> cacheUsers = new ConcurrentHashMap<>();
    static UserChainHandler userChainHandler = new UserChainHandler();

    public static void queryShortUser(String shortUserObjId, NetUserRequest.NetRequestCallBack callBack) {
        userChainHandler.getUserFromChain(shortUserObjId, callBack);
    }

    public static void queryUserByNet(String shortUserObjId, NetUserRequest.NetRequestCallBack callBack) {
        userChainHandler.getUserByNet(shortUserObjId, callBack);
    }

    public static void updateUser(User user) {
        userChainHandler.updateUser(user);
    }

    public static void queryShortUserFromDB(String shortUserObjId, NetUserRequest.NetRequestCallBack callBack) {
        RealmResults<UserRealm> dbData = BaseRealmDao.realm.where(UserRealm.class).equalTo("objectId", shortUserObjId).findAll();
        if (dbData.isEmpty()) {
            //didn't find in db, query Bmob
            BmobQuery<User> query = new BmobQuery<User>();
            query.getObject(shortUserObjId, new QueryListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if (e == null) {
                        BaseRealmDao.insertOrUpdate(user.toRealmObject());
                        cacheUsers.put(shortUserObjId, user);
                        callBack.onSuccess(user);
                    } else {
                        callBack.onFailure(e.getMessage());
                    }
                }
            });
        } else {
            UserRealm userRealm = dbData.first();
            User user = userRealm.toBmobObject();
            cacheUsers.put(shortUserObjId, user);
            callBack.onSuccess(user);
        }
    }

    public static boolean checkSdCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    public interface GetCacheGlideFileCallBack {
        void onFinish(File result);

        void onCancelled();
    }

    private static class GetImageCacheAsyncTask extends AsyncTask<String, Void, File> {
        private final Context context;
        GetCacheGlideFileCallBack callBack;

        public GetImageCacheAsyncTask(Context context, GetCacheGlideFileCallBack callBack) {
            this.context = context;
            this.callBack = callBack;
        }

        @Override
        protected File doInBackground(String... params) {
            String imgUrl = params[0];
            try {
                return Glide.with(context)
                        .load(imgUrl)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get(3000, TimeUnit.SECONDS);
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(File result) {
            callBack.onFinish(result);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            callBack.onCancelled();
        }

        @Override
        protected void onCancelled(File file) {
            super.onCancelled(file);
            callBack.onCancelled();
        }
    }

    public interface GetDrawableCallBack {
        void onFinish(Bitmap result);
    }

    public static void getDrawableByUrl(String url, Context context, GetDrawableCallBack callBack) {
        Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                callBack.onFinish(resource);
            }
        });
//        GetImageDrawableAsyncTask task = new GetImageDrawableAsyncTask(context, callBack);
//        task.execute(url);
    }

    private static class GetImageDrawableAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private final Context context;
        GetDrawableCallBack callBack;

        public GetImageDrawableAsyncTask(Context context, GetDrawableCallBack callBack) {
            this.context = context;
            this.callBack = callBack;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String imgUrl = params[0];
            try {
                Glide.with(context).load(imgUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        callBack.onFinish(resource);
                    }
                });
            } catch (Exception ex) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

        }
    }


    public static File getCacheGlideFile(String url, Context context) {
//        GetImageCacheAsyncTask task = new GetImageCacheAsyncTask(context, callBack);
//        task.execute(url);
        File res = null;
        try{
            res =  Glide.with(context)
                    .load(url)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get(3000, TimeUnit.SECONDS);
        }catch (Exception e){
        }
        return res;
    }

    public static String getTimeByMills() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    public static String getFuzzyTime(String rawTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long serverTime;
        try {
            long rawMills = simpleDateFormat.parse(rawTime).getTime();
            serverTime = System.currentTimeMillis() - rawMills;
            String result;
            int time = (int) (serverTime / (1000 * 60));
            if (time < 0) {
                return rawTime;
            }
            if (time == 0) {
                result = "刚刚";
                return result;
            }
            if (time > 0 && time < 60) {
                result = time + "分钟前";
            } else {
                time = (int) (serverTime / (1000 * 60 * 60));
                if (time > 0 && time < 24) {
                    result = time + "小时前";
                } else {
                    time = (int) (serverTime / (1000 * 60 * 60 * 24));
                    if (time == 1) {
                        result = "昨天";
                    } else if (time == 2) {
                        result = "前天";
                    } else if (time < 7) {
                        result = time + "天前";
                    } else if (time < 30) {
                        result = time / 7 + "周前";
                    } else if (time < 360) {
                        result = time / 30 + "月前";
                    } else {
                        result = time / 360 + "年前";
                    }
                }
            }
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
            return rawTime;
        }
    }

    public static String getChatTime(boolean hasYear, long timesamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = simpleDateFormat.format(new Date(timesamp));
        return getFuzzyTime(dateStr);
    }

    public static void setAvatar(String avatar, ImageView iv) {
        Glide.with(iv.getContext())
                .load((!Utils.isEmpty(avatar)) ? avatar : R.drawable.default_avartar)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new GlideCircleTransform(iv.getContext()))
                .into(iv);
    }

    public static String unicodeToString(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuffer retBuf = new StringBuffer();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U')))
                    try {
                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                else
                    retBuf.append(unicodeStr.charAt(i));
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }

    public static void showGlobalNotify(Context context, int id, Drawable drawable, String name, String msg, Intent intent) {
        Notification.show(context, id, drawable, name, msg, 3000, TYPE_NORMAL)
                .setOnNotificationClickListener(new Notification.OnNotificationClickListener() {
                    @Override
                    public void OnClick(int id) {
                        //open chat
//                Utils.showToast("点击了通知");
                        context.startActivity(intent);
                    }
                });
//        Notification.show(App.getApp(), 0, "", "这是一条消息", Notification.SHOW_TIME_SHORT, TYPE_NORMAL);
    }

    public static String getCurrentShortUserId() {
        return ACache.get(App.APP_INSTANCE).getAsString(SHORT_USER_ID_CACHE + BUser.getCurrentUser().getObjectId());
    }
}
