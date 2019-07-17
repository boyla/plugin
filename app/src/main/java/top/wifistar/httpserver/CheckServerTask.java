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
        final String host = "http://" + ip + ":9595";
        if (NetUtils.isOnline(host)) {
            System.out.println("发现WLAN用户：" + ip);
        }
        return null;
    }
}
