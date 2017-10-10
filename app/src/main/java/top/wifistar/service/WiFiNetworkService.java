package top.wifistar.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import top.wifistar.R;
import top.wifistar.activity.HomeActivity;

/**
 * Created by hasee on 2017/1/5.
 */

public class WiFiNetworkService extends IntentService {

    public WiFiNetworkService() {
        super(WiFiNetworkService.class.getName());
    }

    public WiFiNetworkService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    static volatile boolean showNotify = true;
    @Override
    protected void onHandleIntent(Intent intent) {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this,HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis()).setAutoCancel(true)
                .setContentTitle(getString(R.string.app_name)).setContentText("请保持程序在后台运行");

        if(showNotify){
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.notify(0,builder.build());
            showNotify = false;
        }



        startForeground(0x111, builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, Service.START_FLAG_REDELIVERY, startId);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent sevice = new Intent(WiFiNetworkService.this, WiFiNetworkService.class);
                WiFiNetworkService.this.startService(sevice);
            }
        },555);
        super.onDestroy();
    }
}
