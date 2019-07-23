package top.wifistar.httpserver;

import android.text.TextUtils;

import com.yanzhenjie.andserver.SimpleRequestHandler;
import com.yanzhenjie.andserver.util.HttpRequestParser;
import com.yanzhenjie.andserver.view.View;

import org.apache.httpcore.HttpRequest;

import java.io.File;
import java.net.URLDecoder;

import top.wifistar.activity.HomeActivity;
import top.wifistar.utils.Utils;

import org.apache.httpcore.HttpEntity;
import org.apache.httpcore.HttpException;
import java.io.IOException;
import java.util.Map;

import org.apache.httpcore.entity.FileEntity;

/**
 * Created by boyla on 2018/6/20.
 */

public class FileHandler extends SimpleRequestHandler {

    @Override
    public View handle(HttpRequest request) throws HttpException, IOException {
        Map<String, String> params = HttpRequestParser.parseParams(request);
        String imageUrl = URLDecoder.decode(params.get("image"), "utf-8");
        final boolean[] findFinish = {false};
        final File[] file = new File[1];
        if(!TextUtils.isEmpty(imageUrl)){
            System.out.println("请求本机图片：" + imageUrl);
            //这里应该使用线程池和队列处理
//            Executors.getInstance().submit(new Runnable() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    file[0] = Utils.getCacheGlideFile(imageUrl, HomeActivity.INSTANCE);
                    findFinish[0] = true;
                }
            }).start();
            while(!findFinish[0]){}
        }
        HttpEntity httpEntity = new FileEntity(file[0]);
        View view = new View(200, httpEntity);
        return view;
    }
}
