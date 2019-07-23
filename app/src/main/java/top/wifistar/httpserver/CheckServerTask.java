package top.wifistar.httpserver;

import android.os.AsyncTask;

/**
 * Created by boyla on 2019/7/17.
 */

public class CheckServerTask extends AsyncTask<Void, Void, Void>{

    String ip;

    public CheckServerTask(String ip) {
        this.ip = ip;
    }

    @Override
    protected Void doInBackground(Void... v) {
        if (NetUtils.isOnline(ip)) {
            System.out.println("发现WLAN用户：" + ip);
        }
        return null;
    }
}
