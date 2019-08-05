
package top.wifistar.httpserver;


import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.RequestMethod;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.util.HttpRequestParser;

import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.entity.StringEntity;
import org.apache.httpcore.protocol.HttpContext;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;

import cn.bmob.newim.bean.BmobIMMessage;
import io.realm.Realm;
import top.wifistar.app.App;
import top.wifistar.bean.bmob.User;
import top.wifistar.chain.user.NetUserRequest;
import top.wifistar.event.EurekaEvent;
import top.wifistar.realm.IMUserRealm;
import top.wifistar.utils.EventUtils;
import top.wifistar.utils.Utils;



public class MsgHandler implements RequestHandler {

    //WiFi内接收消息
    @RequestMapping(method = {RequestMethod.GET})
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {

        Map<String, String> params = HttpRequestParser.parseParams(request);
        if (!params.containsKey("msg")) {
            StringEntity stringEntity = new StringEntity("缺少参数", "utf-8");
            response.setStatusCode(400);
            response.setEntity(stringEntity);
            return;
        }
        String msgJson = URLDecoder.decode(params.get("msg"), "utf-8");
        BmobIMMessage msg = App.gson.fromJson(msgJson, BmobIMMessage.class);
        Utils.queryShortUser(msg.getFromId(), new NetUserRequest.NetRequestCallBack() {
            @Override
            public void onSuccess(User user) {
                IMUserRealm userToSave = user.toIMRealm();
                userToSave.lastMsg = msg.getContent();
                userToSave.sendSuccess = true;
                userToSave.updateTime = System.currentTimeMillis();
                Realm realm = Realm.getDefaultInstance();
                try {
                    realm.beginTransaction();
                    realm.insertOrUpdate(userToSave);
                    realm.commitTransaction();
                    EventUtils.post(new EurekaEvent(user));
                } catch (Exception e) {
                    e.printStackTrace();
                    realm.cancelTransaction();
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });

        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.isSuccess = true;

        StringEntity stringEntity = new StringEntity(App.gson.toJson(responseWrapper), "utf-8");
        response.setStatusCode(200);
        response.setEntity(stringEntity);

    }
}
