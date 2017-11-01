package top.wifistar.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
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
import com.scottyab.aescrypt.AESCrypt;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import top.wifistar.R;
import top.wifistar.app.App;
import top.wifistar.bean.BUser;
import top.wifistar.bean.IMUserRealm;
import top.wifistar.bean.bmob.UserProfile;
import top.wifistar.bean.bmob.User;
import top.wifistar.bean.bmob.BmobUtils;
import top.wifistar.customview.CircleImageView;
import top.wifistar.customview.TopReminder;

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
import java.util.Set;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

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

    public static void cancelDialog(Dialog d){
        try{
            if (d != null && d.isShowing())
                d.cancel();
        }catch (Exception e){

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
        }catch (GeneralSecurityException e){
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
        }catch (GeneralSecurityException e){
            e.printStackTrace();
        }
        return result;
    }

    public static void showToast(String str){
        Toast.makeText(App.mContext,str,Toast.LENGTH_LONG).show();
    }

    public static void closeKeyboard(Activity context){
        InputMethodManager imm =  (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(),
                    0);
        }
    }

    public static void showToast(TopReminder topReminder, String string) {
        topReminder.setTheme(TopReminder.THEME_WARNING);
        topReminder.show(string, false, true);
    }

    public static void showToast(TopReminder topReminder, String string,int themeType) {
        topReminder.setTheme(themeType);
        topReminder.show(string, false, true);
    }

    public static int getLevelColor(Context mContext){
        int color;
//        SharedPreferences sp = mContext.getSharedPreferences("cache_data",MODE_PRIVATE);
//        sp.edit().putInt("CURRENT_LEVEL",++lv).commit();
        SharedPreferences sp = mContext.getSharedPreferences("cache_data",MODE_PRIVATE);
        int level = sp.getInt("CURRENT_LEVEL",1);
        switch (level){
            case 1:
                color  = mContext.getResources().getColor(R.color.level_1);
                break;
            case 2:
                color  = mContext.getResources().getColor(R.color.level_2);
                break;
            case 3:
                color  = mContext.getResources().getColor(R.color.level_3);
                break;
            case 4:
                color  = mContext.getResources().getColor(R.color.level_4);
                break;
            case 5:
                color  = mContext.getResources().getColor(R.color.level_5);
                break;
            case 6:
                color  = mContext.getResources().getColor(R.color.level_6);
                break;
            default:
                color  = mContext.getResources().getColor(R.color.level_1);
        }
        return color;
    }

    public static int getLevelColorID(Context mContext){
        SharedPreferences sp = mContext.getSharedPreferences("cache_data",MODE_PRIVATE);
        int level = sp.getInt("CURRENT_LEVEL",1);
        switch (level){
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

    public static void setUserAvatar(Context context, CircleImageView mCustomLogo) {
        UserProfile profile = (UserProfile) ACache.get(context).getAsObject("CURRENT_USER_PROFILE_"+BUser.getCurrentUser().getObjectId());
        if ( profile == null) {
           return;
        }else if(!TextUtils.isEmpty(profile.getAvatar())){
            Glide.with(context).load(profile.getAvatar()).into(mCustomLogo);
        }else if("0".equals(profile.getSex())){
            Glide.with(context).load(R.drawable.default_avartar_female).into(mCustomLogo);
        }else if("1".equals(profile.getSex()) || TextUtils.isEmpty(profile.getSex())){
            Glide.with(context).load(R.drawable.default_avartar_male).into(mCustomLogo);
        }

    }

    public static void setUserAvatar(Context context, UserProfile profile, ImageView imageView) {
        if ( profile == null || TextUtils.isEmpty(profile.getSex())) {
            return;
        }else if(!TextUtils.isEmpty(profile.getAvatar())){
            Glide.with(context).load(profile.getAvatar())
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(imageView);
        }else if("0".equals(profile.getSex())){
            Glide.with(context).load(R.drawable.default_avartar_female).into(imageView);
        }else if("1".equals(profile.getSex())){
            Glide.with(context).load(R.drawable.default_avartar_male).into(imageView);
        }

    }


    public static void setUserAvatar(Context context, IMUserRealm profile, ImageView imageView) {
        if ( profile == null || TextUtils.isEmpty(profile.getSex())) {
            return;
        }else if(!TextUtils.isEmpty(profile.getPhoto())){
            Glide.with(context).load(profile.getPhoto())
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(imageView);
        }else if("0".equals(profile.getSex())){
            Glide.with(context).load(R.drawable.default_avartar_female)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(imageView);
        }else if("1".equals(profile.getSex())){
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
        int i = 0;
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

    public static void updateUser() {
            String objId = ACache.get(App.getInstance()).getAsString("SHORT_USER_ID_" + BUser.getCurrentUser().getObjectId());
            App.getHandler().postDelayed(() -> {
                UserProfile profile = App.currentUserProfile;
                BmobUtils.updateUser(objId, profile.getObjectId(), profile.getNickName(), profile.getAvatar());
            }, 1111);
    }

    public static User getShortUser(){
        UserProfile profile = App.currentUserProfile;
        User user = null;
        if(profile!=null){
            user = new User(profile.getNickName(), profile.getAvatar(),profile.getObjectId());
            String id = ACache.get(App.getInstance()).getAsString("SHORT_USER_ID_" + BUser.getCurrentUser().getObjectId());
            user.setObjectId(id);
        }
        return user;
    }

    public static String friendlyTime(Date time) {
        //获取time距离当前的秒数
        int ct = (int)((System.currentTimeMillis() - time.getTime())/1000);

        if(ct == 0) {
            return "刚刚";
        }

        if(ct > 0 && ct < 60) {
            return ct + "秒前";
        }

        if(ct >= 60 && ct < 3600) {
            return Math.max(ct / 60,1) + "分钟前";
        }
        if(ct >= 3600 && ct < 86400)
            return ct / 3600 + "小时前";
        if(ct >= 86400 && ct < 2592000){ //86400 * 30
            int day = ct / 86400 ;
            return day + "天前";
        }
        if(ct >= 2592000 && ct < 31104000) { //86400 * 30
            return ct / 2592000 + "月前";
        }
        return ct / 31104000 + "年前";
    }

    //在不加载图片的前提下获得图片的宽高
    public static int[] getImageWidthHeight(String path){
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
        return new int[]{options.outWidth,options.outHeight};
    }
}
