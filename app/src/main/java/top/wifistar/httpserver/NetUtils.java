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
import java.util.regex.Pattern;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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
        if (locAddress.equals("")) {
            Toast.makeText(ctx, "扫描失败，请检查wifi网络", Toast.LENGTH_LONG).show();
            return;
        }
        for (int i = 0; i < 256; i++) {//创建256个线程分别去ping
            j = i;
            final int currentIndex = i;
            new Thread(new Runnable() {

                public void run() {

                    String p = ping + locAddress + currentIndex;

                    String current_ip = locAddress + currentIndex;

                    try {
                        proc = run.exec(p);

                        int result = proc.waitFor();
                        if (result == 0) {
                            System.out.println("发现主机：" + current_ip);
                            scanedIPs.add(current_ip);
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    } finally {
                        proc.destroy();
                    }
                }
            }).start();
        }

        if(j==255){
            try {
                Thread.sleep(1555);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String[] strs = scanedIPs.toArray(new String[scanedIPs.size()]);
            Arrays.sort(strs);
            checkServer(strs);
        }
    }

    private static void checkServer(String[] scanedIPs) {
        usersInWiFi.clear();
        for(final String ip : scanedIPs){
            final String host = "http://" + ip + ":9595";
            //如果验证通过...
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (isOnline(host)) {
                        System.out.println("发现WLAN用户："+ ip);
                    }
                }
            }).start();
        }
        try {
            Thread.sleep(5555);
            if(TextUtils.isEmpty(App.WIFI_HOST)){
                System.out.println("没有发现服务器");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
    public static Map<String,String> userHostMap = new HashMap<>();
    private static boolean isOnline(String host) {
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
                    ResponseWrapper response = App.gson.fromJson(res,ResponseWrapper.class);
                    User user = App.gson.fromJson(response.data,User.class);
                    if(user.isHost){
                        App.WIFI_HOST = host;
                    }
                    if(!Utils.getCurrentShortUser().getObjectId().equals(user.getObjectId())){
                        App.getHandler().post(() -> Utils.updateUser(user));
                        usersInWiFi.add(user);
                        userHostMap.put(user.getObjectId(),host);
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
}  
