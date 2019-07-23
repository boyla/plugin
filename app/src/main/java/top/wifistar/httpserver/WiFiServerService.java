/*
 * Copyright © 2017 Yan Zhenjie.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package top.wifistar.httpserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.filter.HttpCacheFilter;
import com.yanzhenjie.andserver.website.AssetsWebsite;
import java.util.concurrent.TimeUnit;


/**
 * <p>Server service.</p>
 * Created by Yan Zhenjie on 2017/3/16.
 */
public class WiFiServerService extends Service {

    /**
     * AndServer.
     */
    private static Server mServer;

    public static String hostAddress;

    @Override
    public void onCreate() {
            initServer();
    }

    private void initServer() {
        // More usage documentation: http://yanzhenjie.github.io/AndServer
        mServer = AndServer.serverBuilder()
                .inetAddress(NetUtils.getLocalIPAddress()) // Bind IP address.
                .port(9595)
                .timeout(55, TimeUnit.SECONDS)
                .website(new AssetsWebsite(getAssets(), "web"))
                .registerHandler("/download", new FileHandler())
                .registerHandler("/image", new ImageHandler())
                .registerHandler("/atService", new ServerHandler())
                .registerHandler("/isOnline", new OnlineHandler())
                .filter(new HttpCacheFilter())
                .listener(mListener)
                .build();
    }

    /**
     * Server listener.
     */
    private Server.ServerListener mListener = new Server.ServerListener() {
        @Override
        public void onStarted() {
            if(mServer==null || mServer.getInetAddress()==null){
                return;
            }
            hostAddress = mServer.getInetAddress().getHostAddress();
            if(TextUtils.isEmpty(hostAddress)){
                hostAddress = "127.0.0.1";
            }
            ServerManager.serverStart(WiFiServerService.this, hostAddress);
        }

        @Override
        public void onStopped() {
            ServerManager.serverStop(WiFiServerService.this);
        }

        @Override
        public void onError(Exception e) {
            ServerManager.serverError(WiFiServerService.this, e.getMessage());
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startServer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopServer(); // Stop server.
    }

    /**
     * Start server.
     */
    private void startServer() {
        if (mServer != null) {
            if (mServer.isRunning()) {
                if(mServer.getInetAddress()==null){
                    System.out.println("getInetAddress null, failed to start WLAN server");
                    return;
                }
                String hostAddress = mServer.getInetAddress().getHostAddress();
                ServerManager.serverStart(WiFiServerService.this, hostAddress);
            } else {
                mServer.startup();
            }
        }
    }

    /**
     * Stop server.
     */
    private void stopServer() {
        if (mServer != null && mServer.isRunning()) {
            mServer.shutdown();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static boolean isServerRunning(){
        if (mServer != null && mServer.isRunning()) {
            return true;
        }else{
            return false;
        }
    }
}
