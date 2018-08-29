//package top.wifistar.utils;
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
