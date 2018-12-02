package top.wifistar.utils;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.tencent.mmkv.MMKV;

public class CacheUtils {

    private static MMKV defaultMMKV;
    private static MMKV userMMKV;
    private static String currentUserId;

    public static MMKV getMMKV() {
        String userId = null;
        if(Utils.getCurrentShortUser()!=null){
            userId = Utils.getCurrentShortUser().getObjectId();
        }
        if(TextUtils.isEmpty(userId)){
            if(defaultMMKV == null){
                defaultMMKV = MMKV.defaultMMKV();
            }
            return defaultMMKV;
        }else{
            if(userMMKV == null){
                userMMKV = MMKV.mmkvWithID("mmkv_user_" + userId);
                currentUserId = userId;
            }else if(!userId.equals(currentUserId)){
                userMMKV = MMKV.mmkvWithID("mmkv_user_" + userId);
                currentUserId = userId;
            }
            return userMMKV;
        }
    }

    public static void importSpToMMKV(SharedPreferences... sp) {
        new Thread(
                () -> {
                    for (SharedPreferences s : sp) {
                        getMMKV().importFromSharedPreferences(s);
                    }
                }
        ).start();
    }

    public static SharedPreferences.Editor putInt(String key, int value) {
        return getMMKV().putInt(key, value);
    }

    public static int getInt(String key, int defaultValue) {
        return getMMKV().getInt(key, defaultValue);
    }

    public static SharedPreferences.Editor putBoolean(String key, boolean value) {
        return getMMKV().putBoolean(key, value);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return getMMKV().getBoolean(key, defaultValue);
    }

    public static SharedPreferences.Editor putString(String key, String value) {
        return getMMKV().putString(key, value);
    }

    public static String getString(String key, String defaultValue) {
        return getMMKV().getString(key, defaultValue);
    }
    public static <T> T getPreference(String key, T defaultValue) {
        MMKV mmkv = getMMKV();
        if (mmkv.contains(key)) {
            if (defaultValue instanceof String) {
                return (T) mmkv.getString(key, (String) defaultValue);
            } else if (defaultValue instanceof Long) {
                return (T) Long.valueOf(mmkv.getLong(key, (Long) defaultValue));
            } else if (defaultValue instanceof Integer) {
                return (T) Integer.valueOf(mmkv.getInt(key, (Integer) defaultValue));
            } else if (defaultValue instanceof Float) {
                return (T) Float.valueOf(mmkv.getFloat(key, (Float) defaultValue));
            } else if (defaultValue instanceof Boolean) {
                return (T) Boolean.valueOf(mmkv.getBoolean(key, (Boolean) defaultValue));
            } else {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static <T> boolean setPreference(String key, T value, boolean isAsync) {
        MMKV mmkv = getMMKV();
        SharedPreferences.Editor editor = null;
        if (value instanceof Boolean) {
            editor = mmkv.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor = mmkv.putInt(key, (Integer) value);
        } else if (value instanceof String) {
            editor = mmkv.putString(key, (String) value);
        } else if (value instanceof Long) {
            editor = mmkv.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            editor = mmkv.putFloat(key, (Float) value);
        }
        if(editor==null){
            throw new IllegalArgumentException("only support String、Boolean、Float、Int、Long、Set<String>");
        }
        if (isAsync) {
            editor.apply();
            return true;
        }
        return editor.commit();
    }

    public static <T> boolean setPreference(String lightnessKey, T value) {
        return setPreference(lightnessKey, value, false);
    }
}
//
//import android.content.Context;
//import android.util.Log;
//
//
//import top.wifistar.bean.User;
//import top.wifistar.bean.UserBean;
//
//import java.util.List;
//
///**
// * Created by popoy on 16/2/18.
// */
//public class CacheUtils {
//    private static String TAG = "CacheUtils";
//    private volatile static CacheUtils mInstance = null;
//    private Context mContext;
//    private UserBean userBean;
////    private FilterBean filterBean;
//
//    public static CacheUtils getInstance() {
//        if (mInstance == null) {
//            synchronized (CacheUtils.class) {
//                if (mInstance == null) {
//                    mInstance = new CacheUtils();
//                }
//            }
//        }
//        return mInstance;
//    }
//
//    public void init(Context context) {
//        try {
//            mContext = context.getApplicationContext();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e(TAG, "Serials error,CacheUtils init failed!");
//        }
//    }
//
////    public void updateUserBean() {
////        UniversalDao dao = new UniversalDao(mContext);
////        dao.addUser(getUserBean());
////    }
////
////    public void setUserBean(UserBean userBean) {
////        this.userBean = userBean;
////        UniversalDao dao = new UniversalDao(mContext);
////        dao.addUser(userBean);
////    }
////
//    public User getUserBean() {
////        if (userBean == null) {
////            UniversalDao db = new UniversalDao(mContext);
////            userBean = db.queryUser();
////        }
//
//        return null;
//    }
////
////    public FilterBean getFilterBean() {
////        if (null == userBean) {
////            userBean = getUserBean();
////        }
////
////        if (this.filterBean == null) {
////            String filter = ACache.get(mContext, userBean.getUsr_id()).getAsString(ACacheKeyConfig.ACACHE_KEY_FILTERBEAN);
////            this.filterBean = JSON.parseObject(filter, FilterBean.class);
////            if (filterBean == null) {
////                this.filterBean = new FilterBean();
////                filterBean.setLookingfor(CacheUtils.getInstance().getUserBean().getMatch_gender() + "");
////                filterBean.setMinage(CacheUtils.getInstance().getUserBean().getMatch_age_min() + "");
////                filterBean.setMaxage(CacheUtils.getInstance().getUserBean().getMatch_age_max() + "");
////                filterBean.setLocationType(1);
////                filterBean.setCityItem(getUserBean().getCity());
////                filterBean.setCountry(getUserBean().getCountry());
////                filterBean.setCountryItem(getUserBean().getCountry_name());
////                filterBean.setState(getUserBean().getState());
////                filterBean.setStateItem(getUserBean().getState_name());
////            }
////        }
////        return filterBean;
////    }
////
////    public void putFilterBean(FilterBean filterBean) {
////        this.filterBean = filterBean;
////        if (null == userBean) {
////            userBean = getUserBean();
////        }
////
////        ACache.get(mContext, userBean.getUsr_id()).put(ACacheKeyConfig.ACACHE_KEY_FILTERBEAN, JSON.toJSONString(filterBean));
////    }
////
////    public void putReportState(String userId) {
////        ACache.get(mContext, userId).put(ACacheKeyConfig.ACACHE_KEY_USER_PROFILE_REPORT, userId, ACache.TIME_DAY);
////    }
////
////    /**
////     * return Report State, one member one report a day
////     **/
////    public boolean getReportState(String userId) {
////        String state = ACache.get(mContext, userId).getAsString(ACacheKeyConfig.ACACHE_KEY_USER_PROFILE_REPORT);
////        if (state != null)
////            if (state.equals(userId)) {
////                return false;
////            }
////        return true;
////    }
////
////    public void putBrowseData(List<UserProfileItemBean> browseBeans) {
////        if (browseBeans != null && browseBeans.size() > 0 && getUserBean() != null)
////            ACache.get(mContext, getUserBean().getUsr_id()).put(ACacheKeyConfig.ACACHE_KEY_BROWSE_LIST, JSON.toJSONString(browseBeans));
////    }
////
////    public List<UserProfileItemBean> getBrowseData() {
////        String state = ACache.get(mContext, getUserBean().getUsr_id()).getAsString(ACacheKeyConfig.ACACHE_KEY_BROWSE_LIST);
////        List<UserProfileItemBean> browseBeen = JSON.parseArray(state, UserProfileItemBean.class);
////        return browseBeen;
////    }
//}
