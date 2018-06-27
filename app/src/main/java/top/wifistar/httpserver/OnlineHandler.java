
package top.wifistar.httpserver;

import android.text.TextUtils;

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

import top.wifistar.app.App;
import top.wifistar.bean.bmob.User;
import top.wifistar.event.EurekaEvent;
import top.wifistar.utils.EventUtils;
import top.wifistar.utils.Utils;


public class OnlineHandler implements RequestHandler {

    //交换用户信息
    @RequestMapping(method = {RequestMethod.GET})
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {

        App.getHandler().post(new Runnable() {
            @Override
            public void run() {
                NetUtils.userJson = App.gson.toJson(Utils.getCurrentShortUser());
            }
        });
        Map<String, String> params = HttpRequestParser.parseParams(request);

        if (!params.containsKey("user")) {
            StringEntity stringEntity = new StringEntity("缺少参数", "utf-8");
            response.setStatusCode(400);
            response.setEntity(stringEntity);
            return;
        }

        String userJson = URLDecoder.decode(params.get("user"), "utf-8");
        User user = App.gson.fromJson(userJson, User.class);
        App.getHandler().post(() ->{
            if(Utils.getCurrentShortUser()!=null && !Utils.getCurrentShortUser().getObjectId().equals(user.getObjectId())){
                Utils.updateUser(user);
                NetUtils.usersInWiFi.add(user);
                EventUtils.post(new EurekaEvent(user));
            }
        });



        ResponseWrapper responseWrapper = new ResponseWrapper();
        String data = NetUtils.userJson;
        if (!TextUtils.isEmpty(data)) {
            responseWrapper.isSuccess = true;
            responseWrapper.data = data;
            responseWrapper.msg = "OK, I am online";
        } else {
            responseWrapper.isSuccess = false;
            responseWrapper.msg = "Failed to get user data";
        }

        StringEntity stringEntity = new StringEntity(App.gson.toJson(responseWrapper), "utf-8");
        response.setStatusCode(200);
        response.setEntity(stringEntity);

    }
}
