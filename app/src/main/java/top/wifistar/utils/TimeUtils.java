package top.wifistar.utils;

/**
 * Created by hasee on 2017/4/13.
 */

import android.net.ParseException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import top.wifistar.R;
import top.wifistar.app.App;

/**
 * @packagename: com.masonsoft.base.utils
 * @filename: TimeUtils.java
 * @author: VicentLiu
 * @description:TODO
 * @time: Jan 29, 2015 2:36:18 PM
 **/
public class TimeUtils {

    public static String time_format_time_zone = "yyyy-MM-dd'T'HH:mm:ss";

    public static DateFormat format = new SimpleDateFormat(time_format_time_zone);
    private static String time_format1 = "h:mm a";
    private static SimpleDateFormat sdf1 = new SimpleDateFormat(time_format1);
    private static String time_format2 = "h:mm a";
    private static SimpleDateFormat sdf2 = new SimpleDateFormat(time_format2);
    private static String time_format3 = "h:mm a";
    private static SimpleDateFormat sdf3 = new SimpleDateFormat(time_format3);
    private static String time_format4 = "M/d h:mm a";
    private static SimpleDateFormat sdf4 = new SimpleDateFormat(time_format4);
    private static String time_format5 = "M/d,yy";
    private static SimpleDateFormat sdf5 = new SimpleDateFormat(time_format5);

    private static String time_format6 = "M/d";
    private static SimpleDateFormat sdf6 = new SimpleDateFormat(time_format6);
    private static String time_format7 = "M/d/yyyy";
    private static SimpleDateFormat sdf7 = new SimpleDateFormat(time_format7);
    private static String time_format8 = "MMM d, yyyy";
    private static SimpleDateFormat sdf8 = new SimpleDateFormat(time_format8, Locale.US);
    private static String time_format9 = "MMM d";
    private static SimpleDateFormat sdf9 = new SimpleDateFormat(time_format9, Locale.US);

    private static String time_format10 = "M/d,yyyy";
    private static SimpleDateFormat sdf10 = new SimpleDateFormat(time_format10);
    public static String time_format11 = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat sdf11 = new SimpleDateFormat(time_format11);
    public static final int SECONDS_IN_DAY = 60 * 60 * 24;

    public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

    public static long getTimeMillis(String time) {
        Date date;
        try {
            if (time != null && !"".equals(time)) {
                date = format.parse(time);
            } else {
                date = new Date();
            }
            return date.getTime();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long getTimeMillis(Date date) {
        return date.getTime();
    }

    public static String getTimeStr(long beginDate) {
        return format.format(new Date(beginDate));
    }

    public static String LocalToGTM(String LocalDate) {


        SimpleDateFormat format;
        format = new SimpleDateFormat(time_format11, Locale.ENGLISH);
        Date result_date;
        long result_time = 0;


        if (null == LocalDate) {
            return LocalDate;
        } else {
            try {
                format.setTimeZone(TimeZone.getDefault());
                result_date = format.parse(LocalDate);
                result_time = result_date.getTime();
                format.setTimeZone(TimeZone.getTimeZone("GMT00:00"));
                return format.format(result_time);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return LocalDate;


    }

    public static String GTMToLocal(String GTMDate) {
        int tIndex = GTMDate.indexOf("T");
        String dateTemp = GTMDate.substring(0, tIndex);
        String timeTemp = GTMDate.substring(tIndex + 1, GTMDate.length() - 6);
        String convertString = dateTemp + " " + timeTemp;
        SimpleDateFormat format;
        format = new SimpleDateFormat(time_format_time_zone, Locale.ENGLISH);
        Date result_date;
        long result_time = 0;


        if (null == GTMDate) {
            return GTMDate;
        } else {
            try {
                format.setTimeZone(TimeZone.getTimeZone("GMT00:00"));
                result_date = format.parse(convertString);
                result_time = result_date.getTime();
                format.setTimeZone(TimeZone.getDefault());
                return format.format(result_time);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return GTMDate;


    }


    public static String transformTimeInside(String time) {
        if (time == null || "".equals(time)) return "";
        try {
            Date date = format.parse(time);
            String[] ids = TimeZone.getAvailableIDs();
            date = DateUtils.changeTimeZone(date, TimeZone.getTimeZone("PDT"), TimeZone.getDefault());
            String ss = "";
            long tempTime = System.currentTimeMillis();
            long difference = tempTime - date.getTime();
            if (difference / (24 * 60 * 60 * 1000) == 0) {
                if (difference < 3 * 60 * 1000) {
                    if (difference < 2 * 60 * 1000) {
                        ss = App.getInstance().getResources().getString(R.string.Message_Just_Now);
                    } else {
                        int mun = (int) difference / (60 * 1000);
                        ss = App.getInstance().getResources().getString(R.string.Message_Few_Minutes);
                        ss = String.format(ss, mun + "");
                    }
                } else {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    int day1 = calendar.get(Calendar.DAY_OF_MONTH);
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    int day2 = calendar.get(Calendar.DAY_OF_MONTH);
                    if (day2 - day1 > 0) {
                        ss = App.getInstance().getResources().getString(R.string.Yesterday) + ", " + sdf2.format(date).toLowerCase();
                    } else {
                        ss = sdf1.format(date).toLowerCase();
                    }
                }
            } else if (difference / (24 * 60 * 60 * 1000) == 1) {
                ss = App.getInstance().getResources().getString(R.string.Yesterday) + ", " + sdf2.format(date).toLowerCase();
            } else if (difference / (24 * 60 * 60 * 1000) <= 6) {
                ss = getWeekOfDate(date) + ", " + sdf3.format(date).toLowerCase();
            } else {
                Date today = new Date();
                if (date.getYear() == today.getYear()) {
                    ss = sdf4.format(date).toLowerCase();
                    String months = ss.split("/")[1];
                    ss = getMonthOfDate(date) + ", " + months;
                } else {
                    ss = sdf5.format(date).toLowerCase();
                    String months = ss.split("/")[1];
                    ss = getMonthOfDate(date) + ", " + months;
                }
            }
            return ss;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String transformTimeInside(Date date) {
        if (date == null || "".equals(date)) return "";
        try {
//            Date date = format.parse(time);
            date = DateUtils.changeTimeZone(date, TimeZone.getTimeZone("PDT"), TimeZone.getDefault());
            String ss = "";
            long tempTime = System.currentTimeMillis();
            long difference = tempTime - date.getTime();
            if (difference / (24 * 60 * 60 * 1000) == 0) {
                if (difference < 3 * 60 * 1000) {
                    if (difference < 2 * 60 * 1000) {
                        ss = App.getInstance().getResources().getString(R.string.Message_Just_Now);
                    } else {
                        int mun = (int) difference / (60 * 1000);
                        ss = App.getInstance().getResources().getString(R.string.Message_Few_Minutes);
                        ss = String.format(ss, mun + "");
                    }
                } else {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    int day1 = calendar.get(Calendar.DAY_OF_MONTH);
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    int day2 = calendar.get(Calendar.DAY_OF_MONTH);
                    if (day2 - day1 > 0) {
                        ss = App.getInstance().getResources().getString(R.string.Yesterday) + ", " + sdf2.format(date).toLowerCase();
                    } else {
                        ss = sdf1.format(date).toLowerCase();
                    }
                }
            } else if (difference / (24 * 60 * 60 * 1000) == 1) {
                ss = App.getInstance().getResources().getString(R.string.Yesterday) + ", " + sdf2.format(date).toLowerCase();
            } else if (difference / (24 * 60 * 60 * 1000) <= 6) {
                ss = getWeekOfDate(date) + ", " + sdf3.format(date).toLowerCase();
            } else {
                Date today = new Date();
                if (date.getYear() == today.getYear()) {
                    ss = sdf4.format(date).toLowerCase();
                    String months = ss.split("/")[1];
                    ss = getMonthOfDate(date) + ", " + months;
                } else {
                    ss = sdf5.format(date).toLowerCase();
                    String months = ss.split("/")[1];
                    ss = getMonthOfDate(date) + ", " + months;
                }
            }
            return ss;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String transformTimeOutSide(String time) {
        if (time == null || "".equals(time)) return "";
        try {
            Date date = format.parse(time);
            date = DateUtils.changeTimeZone(date, TimeZone.getTimeZone("PDT"), TimeZone.getDefault());
            String ss = "";
            long tempTime = System.currentTimeMillis();
            long difference = tempTime - date.getTime();
            if (difference / (24 * 60 * 60 * 1000) == 0) {
                if (difference < 3 * 60 * 1000) {
                    if (difference < 2 * 60 * 1000) {
                        ss = App.getInstance().getResources().getString(R.string.Message_Just_Now);
                    } else {
                        int mun = (int) difference / (60 * 1000);
                        ss = App.getInstance().getResources().getString(R.string.Message_Few_Minutes);
                        ss = String.format(ss, mun + "");
                    }
                } else {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    int day1 = calendar.get(Calendar.DAY_OF_MONTH);
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    int day2 = calendar.get(Calendar.DAY_OF_MONTH);
                    if (day2 - day1 > 0) {
                        ss = App.getInstance().getResources().getString(R.string.Yesterday);
                    } else {
                        ss = sdf1.format(date).toLowerCase();
                    }
                }
            } else if (difference / (24 * 60 * 60 * 1000) == 1) {
                ss = App.getInstance().getResources().getString(R.string.Yesterday);
            } else if (difference / (24 * 60 * 60 * 1000) <= 6) {
                ss = getWeekOfDate(date);
            } else {
                Date today = new Date();
                if (date.getYear() == today.getYear()) {
                    ss = sdf6.format(date).toLowerCase();
                    String months = ss.split("/")[1];
                    ss = getMonthOfDate(date) + " " + months;
                } else {
                    ss = sdf10.format(date).toLowerCase();
                    String months = ss.split("/")[1];
                    ss = getMonthOfDate(date) + " " + months;
                }
            }
            return ss;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getWeekOfDate(Date dt) {
        String[] weekDays = App.getInstance().getResources().getStringArray(R.array.week);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static String getMonthOfDate(Date dt) {
        String[] monthDays = App.getInstance().getResources().getStringArray(R.array.month);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int m = cal.get(Calendar.MONTH);
        if (m < 0)
            m = 0;
        return monthDays[m];
    }

    /**
     * @param
     * @param
     * @return if bDate is early,true; or false
     */
    public static boolean dateCompare(String bDateStr, String eDateStr) {
        boolean flag = false;
        Date bDate, eDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            bDate = dateFormat.parse(bDateStr);
            eDate = dateFormat.parse(eDateStr);
            if (bDate.before(eDate)) {
                flag = true;
            }
        } catch (java.text.ParseException e1) {
            e1.printStackTrace();
            flag = false;
        }
        return flag;
    }

    public static boolean isToday(final long askTime) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);

        if (askTime > mCalendar.getTime().getTime() && askTime < System.currentTimeMillis()) {
            return true;
        }

        return false;
    }

    /**
     * format "dd-MMM-yy"
     */
    public static String getDateFormat8(String originDateStr) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        try {
            date = format.parse(originDateStr);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            if (isCurrentYear(date)) {
                return sdf9.format(date);
            } else {
                return sdf8.format(date);
            }
        } else {
            return "";
        }
    }


    private static boolean isCurrentYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return year == currentYear;
    }

    public static Date stringToDate(String time) {
        Date date;
        try {
            if (time != null && !"".equals(time)) {
                date = format.parse(time);
            } else {
                date = new Date();
            }
            return date;
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String dateToString(Date time) {
        if (time == null) return null;
        SimpleDateFormat formatter = new SimpleDateFormat(time_format_time_zone);
        String dateString = formatter.format(time);
        return dateString;
    }

    public static int daysMoreThanServer() {
        int days = 0;

        Date d = new Date();
        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        String now = format.format(d);
        format.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        String server = format.format(d);

        try {
            Date date_local = format.parse(now);
            Date date_server = format.parse(server);
            long diff = date_local.getTime() - date_server.getTime();
            if (diff > 0) {
                days = 1;
            } else if (diff < 0) {
                days = -1;
            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return days;
    }
}

