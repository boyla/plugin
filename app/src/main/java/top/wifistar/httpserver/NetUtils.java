package top.wifistar.httpserver;

/**
 * Created by boyla on 2018/6/20.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import top.wifistar.app.App;
import top.wifistar.bean.bmob.User;
import top.wifistar.event.EurekaEvent;
import top.wifistar.utils.EventUtils;
import top.wifistar.utils.Utils;


public class NetUtils {

    private static String locAddress;//存储本机ip，例：本地ip ：192.168.1.

    private static Runtime run = Runtime.getRuntime();//获取当前运行环境，来执行ping，相当于windows的cmd

    private static Process proc = null;

    private static String ping = "ping -c 1 -w 0.5 ";//其中 -c 1为发送的次数，-w 表示发送后等待响应的时间

    private volatile static int j;//存放ip最后一位地址 0-255

    private static Context ctx;//上下文

    public NetUtils(Context ctx) {
        this.ctx = ctx;
    }


    /**
     * 扫描局域网内ip，找到对应服务器
     */
    public static String userJson;

    public static void scan() {
        locAddress = getLocAddrIndex();//获取本地ip前缀  
        final List<String> scanedIPs = new CopyOnWriteArrayList<>();
        if (TextUtils.isEmpty(locAddress)) {
            System.out.println("扫描 WIFI IP 失败，请检查wifi网络");
            return;
        }
        new Thread(new Runnable() {
            public void run() {
                //去ping 0-255
                for (int i = 0; i < 256; i++) {
                    j = i;
                    final int currentIndex = i;
                    String p = ping + locAddress + currentIndex;
                    String current_ip = locAddress + currentIndex;
                    try {
                        proc = run.exec(p);
                        int result = proc.waitFor();
                        if (result == 0) {
                            System.out.println("发现主机：" + current_ip);
                            scanedIPs.add(current_ip);
                            checkServer(current_ip);
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    } finally {
                        proc.destroy();
                    }
                }
            }
        }).start();
    }

    private static void checkServer(String ip) {
        new CheckServerTask(ip).execute();
    }


    //获取本地ip地址  
    public static String getLocAddress() {

        String ipaddress = "";

        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            // 遍历所用的网络接口  
            while (en.hasMoreElements()) {
                NetworkInterface networks = en.nextElement();
                // 得到每一个网络接口绑定的所有ip  
                Enumeration<InetAddress> address = networks.getInetAddresses();
                // 遍历每一个接口绑定的所有ip  
                while (address.hasMoreElements()) {
                    InetAddress ip = address.nextElement();
                    if (!ip.isLoopbackAddress()
                            && InetAddressUtils.isIPv4Address(ip.getHostAddress())) {
                        ipaddress = ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("", "获取本地ip地址失败");
            e.printStackTrace();
        }

        System.out.println("本机IP:" + ipaddress);

        return ipaddress;

    }

    //获取IP前缀  
    public static String getLocAddrIndex() {

        String str = getLocAddress();

        if (!str.equals("")) {
            return str.substring(0, str.lastIndexOf(".") + 1);
        }

        return null;
    }

    //获取本机设备名称  
    public String getLocDeviceName() {

        return android.os.Build.MODEL;

    }

    public static List<User> usersInWiFi = new ArrayList<>();
    public static Map<String, String> userHostMap = new HashMap<>();

    public static boolean isOnline(String host) {
        final String urlStr = host + "/isOnline?user=" + userJson;
        boolean serviceAvaliable = false;
        String encodeType = "utf-8";
        URL infoUrl;
        InputStream inStream = null;
        HttpURLConnection httpConnection = null;
        try {
            infoUrl = new URL(urlStr);
            URLConnection connection = infoUrl.openConnection();
            httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.setReadTimeout(2000);
            httpConnection.setConnectTimeout(2000);
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inStream, encodeType));
                StringBuilder strber = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    strber.append(line + "\n");
                }
                String res = strber.toString();
                if (!TextUtils.isEmpty(res) && res.contains("OK")) {
                    serviceAvaliable = true;
                    ResponseWrapper response = App.gson.fromJson(res, ResponseWrapper.class);
                    User user = App.gson.fromJson(response.data, User.class);
                    if (user.isHost) {
                        App.WIFI_HOST = host;
                    }
                    if (!Utils.getCurrentShortUserId().equals(user.getObjectId())) {
                        App.getHandler().post(() -> Utils.updateUser(user));
                        usersInWiFi.add(user);
                        userHostMap.put(user.getObjectId(), host);
                        EventUtils.post(new EurekaEvent(user));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("访问失败:" + host);
        } finally {
            try {
                if (inStream != null)
                    inStream.close();
                if (httpConnection != null)
                    httpConnection.disconnect();
            } catch (Exception ex) {
                System.out.println("关闭连接异常");
            }
        }
        return serviceAvaliable;
    }

    public static InetAddress getLocalIPAddress() {
        Enumeration<NetworkInterface> enumeration = null;
        try {
            enumeration = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                NetworkInterface nif = enumeration.nextElement();
                Enumeration<InetAddress> inetAddresses = nif.getInetAddresses();
                if (inetAddresses != null)
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();
                        if (!inetAddress.isLoopbackAddress() && isIPv4Address(inetAddress.getHostAddress())) {
                            return inetAddress;
                        }
                    }
            }
        }
        return null;
    }

    private static final Pattern IPV4_PATTERN = Pattern.compile("^(" +
            "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" +
            "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");

    public static boolean isIPv4Address(String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

    /**
     * 获取外网的IP(要访问Url，要放到后台线程里处理)
     *
     * @param @return
     * @return String
     * @throws
     * @Title: GetNetIp
     * @Description:
     */
    public static String getNetIp() {
        URL infoUrl = null;
        InputStream inStream = null;
        String ipLine = "";
        HttpURLConnection httpConnection = null;
        try {
//            infoUrl = new URL("http://ip168.com/");
            infoUrl = new URL("http://pv.sohu.com/cityjson?ie=utf-8");
            URLConnection connection = infoUrl.openConnection();
            httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inStream, "utf-8"));
                StringBuilder strber = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    strber.append(line + "\n");
                }
                Pattern pattern = Pattern
                        .compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
                Matcher matcher = pattern.matcher(strber.toString());
                if (matcher.find()) {
                    ipLine = matcher.group();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inStream.close();
                httpConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        Log.e("getNetIp", ipLine);
        return ipLine;
    }

    public static final int MOBILE = 1;
    public static final int WIFI = 0;
    public static final int NONET = -1;
    private static ConnectivityManager sConnectivityManager;

    /**
     * 获得网络连接是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }
        if (sConnectivityManager == null) {
            ConnectivityManager con = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (con == null) {
                return false;
            }
            sConnectivityManager = con;
        }

        NetworkInfo workinfo = sConnectivityManager.getActiveNetworkInfo();
        return workinfo != null && workinfo.isAvailable();
    }


    /**
     * 获取当前的网络状态 -1：没有网络 0：WIFI网络1：mobile网络
     *
     * @param context
     * @return
     */
    public static int getNetConnectType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                return WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                return MOBILE;
            }
        }
        return NONET;
    }

    /**
     * 检测wifi是否连接
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测3G是否连接
     */
    public static boolean is3gConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断网址是否有效
     */
    public static boolean isLinkAvailable(String link) {
        Pattern pattern = Pattern.compile("^(http://|https://)?((?:[A-Za-z0-9]+-[A-Za-z0-9]+|[A-Za-z0-9]+)\\.)+([A-Za-z]+)[/\\?\\:]?.*$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(link);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }
}  
