package top.wifistar.im;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import cn.bmob.push.PushConstants;
import top.wifistar.R;
import top.wifistar.app.App;

/**
 * Created by boyla on 2018/5/14.
 */

//TODO 集成：1.3、创建自定义的推送消息接收器，并在清单文件中注册
public class PushMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String msg = intent.getStringExtra("msg");
        if(msg.contains("LOGIN ON OTHER DEVICE")){
            App.getApp().showReloginDialog("提示","你的账号在另一设备登陆，你已被迫下线");
            return;
        }
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            Log.i("收到推送：", msg);
            Notify notify = new Notify(context);
            notify.setId(msg.hashCode());
            notify.setTitle(msg);
            notify.setAutoCancel(true);
            notify.setSmallIcon(R.mipmap.ic_launcher);
            notify.notification();
            notify.show();
        }


    }
}
