package top.wifistar.bean;

import org.apache.http.cookie.Cookie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UserBean implements Serializable {

    private static final long serialVersionUID = -8708310750333793601L;
//    private UserBean userBean;

    private String country;
    private String country_name;
    private String state;
    private String state_name;
    private String state_short_name;
    private String city;
    private String city_name;
    private String password;
    private String message_notice_on;
    private String session_id;
    private String income_verify;
    private String username;
    private String usr_id;
    private String age_day;
    private int isGuest;//1:guest， 0:gold
    private String gender;//1:woman 2:man
    private String age_year;
    private String age_month;
    private String age;
    private String match_gender;
    private String jid;
    private int match_age_min;
    private int match_age_max;
    private int has_blog_setting;
//    private LinkedList<PictureBean> pictures = new LinkedList<PictureBean>();
    private ViewCountBean viewed_count;
    private InterestedCountBean interested_count;
    private List<Cookie> cookies;
    private boolean isConflict = false;
    boolean isAgreeServiceAndPrivacy = true;
//    private UserProfileBean userProfileBean;
    private String email;
//    private UserProfileAccountBean userProfileAccountBean;

    public boolean isAgreeServiceAndPrivacy() {
        return isAgreeServiceAndPrivacy;
    }

    public void setAgreeServiceAndPrivacy(boolean isAgreeServiceAndPrivacy) {
        this.isAgreeServiceAndPrivacy = isAgreeServiceAndPrivacy;
    }

    public boolean isConflict() {
        return isConflict;
    }

    public void setConflict(boolean isConflict) {
        this.isConflict = isConflict;
    }

    public void setUserBean(UserBean userBean) {
        userBean = userBean;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage_notice_on() {
        return message_notice_on;
    }

    public void setMessage_notice_on(String message_notice_on) {
        this.message_notice_on = message_notice_on;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getIncome_verify() {
        if (null == income_verify) {
            income_verify = "";
        }
        return income_verify;
    }

    public void setIncome_verify(String income_verify) {
        this.income_verify = income_verify;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(String usr_id) {
        this.usr_id = usr_id;
    }

    public String getAge_day() {
        return age_day;
    }

    public void setAge_day(String age_day) {
        this.age_day = age_day;
    }

    public int getIsGuest() {
        return isGuest;
    }

    public void setIsGuest(int isGuest) {
        this.isGuest = isGuest;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge_year() {
        return age_year;
    }

    public void setAge_year(String age_year) {
        this.age_year = age_year;
    }

    public String getAge_month() {
        return age_month;
    }

    public void setAge_month(String age_month) {
        this.age_month = age_month;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMatch_gender() {
        return match_gender;
    }

    public void setMatch_gender(String match_gender) {
        this.match_gender = match_gender;
    }

    public int getMatch_age_min() {
        return match_age_min;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public void setMatch_age_min(int match_age_min) {
        this.match_age_min = match_age_min;
    }

    public int getMatch_age_max() {
        return match_age_max;
    }

    public void setMatch_age_max(int match_age_max) {
        this.match_age_max = match_age_max;
    }


//    public LinkedList<PictureBean> getPictures() {
//        return pictures;
//    }
//
//    public void setPictures(LinkedList<PictureBean> pictures) {
//        this.pictures = pictures;
//    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getState_short_name() {
        return state_short_name;
    }

    public void setState_short_name(String state_short_name) {
        this.state_short_name = state_short_name;
    }

//    public void addPictures(int i, PictureBean pictureBean) {
//        if (pictureBean == null) return;
//        if (pictures == null) pictures = new LinkedList<>();
//        if (!pictures.contains(pictureBean))
//            pictures.add(i, pictureBean);
//    }
//
//    public void addPictures(PictureBean pictureBean) {
//        if (pictureBean == null) return;
//        if (pictures == null) pictures = new LinkedList<>();
//        if (!pictures.contains(pictureBean))
//            pictures.add(pictureBean);
//    }
//
//    public void addPictures(List<PictureBean> listPictureBean) {
//        if (listPictureBean == null) return;
//        if (pictures == null) pictures = new LinkedList<PictureBean>();
//        pictures.addAll(listPictureBean);
//    }

    public ViewCountBean getViewed_count() {
        return viewed_count;
    }

    public void setViewed_count(ViewCountBean viewed_count) {
        this.viewed_count = viewed_count;
    }

    public InterestedCountBean getInterested_count() {
        return interested_count;
    }

    public void setInterested_count(InterestedCountBean interested_count) {
        this.interested_count = interested_count;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }


//    public void setUserProfileBean(UserProfileBean userProfileBean) {
//        this.userProfileBean = userProfileBean;
//    }
//
//    public UserProfileBean getUserProfileBean() {
//        return userProfileBean;
//    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }

    public boolean isGolden() {
        if (1 == this.getIsGuest()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isBlogHasTheme() {
        return 1 == this.getHas_blog_setting();
    }

    public void setBlogHasTheme() {
        this.setHas_blog_setting(1);
    }

    public void setUnBlogHasTheme() {
        this.setHas_blog_setting(0);
    }

    public int getHas_blog_setting() {
        return has_blog_setting;
    }

    public void setHas_blog_setting(int has_blog_setting) {
        this.has_blog_setting = has_blog_setting;
    }

    public class ViewCountBean implements Serializable {
        private static final long serialVersionUID = 151848910148140418L;
        private String new_count;
        private String total;

        public String getNew_count() {
            return new_count;
        }

        public void setNew_count(String new_count) {
            this.new_count = new_count;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }
    }

    public class InterestedCountBean implements Serializable {
        private static final long serialVersionUID = -664403061313366891L;
        private String new_count;
        private String total;

        public String getNew_count() {
            return new_count;
        }

        public void setNew_count(String new_count) {
            this.new_count = new_count;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private static List<Runnable> upgradeListenerList = new ArrayList<Runnable>();

    public static void addUpgradeListener(Runnable runnable) {
        upgradeListenerList.add(runnable);
    }

    public static void removeUpgradeListener(Runnable runnable) {
        upgradeListenerList.remove(runnable);
    }

    public static void notifyUpgradeListener() {
        for (int i = 0; i < upgradeListenerList.size(); i++) {
            Runnable runnable = upgradeListenerList.get(i);
            if (runnable != null) {
                runnable.run();
            }
        }
    }

//    public UserProfileAccountBean getUserProfileAccountBean() {
//        return userProfileAccountBean;
//    }
//
//    public void setUserProfileAccountBean(UserProfileAccountBean userProfileAccountBean) {
//        this.userProfileAccountBean = userProfileAccountBean;
//    }
}
