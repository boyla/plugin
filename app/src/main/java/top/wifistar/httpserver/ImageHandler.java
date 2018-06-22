package top.wifistar.httpserver;

import android.os.Environment;

import com.yanzhenjie.andserver.SimpleRequestHandler;
import com.yanzhenjie.andserver.util.HttpRequestParser;
import com.yanzhenjie.andserver.view.View;

import org.apache.httpcore.HttpEntity;
import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.entity.FileEntity;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * Created by boyla on 2018/6/20.
 */

public class ImageHandler extends SimpleRequestHandler {

    private File mFile = new File(Environment.getExternalStorageDirectory(), "xxx.jpg");

    @Override
    protected View handle(HttpRequest request) throws HttpException, IOException {
        Map<String, String> params = HttpRequestParser.parseParams(request);

        if (!params.containsKey("username") || !params.containsKey("password")) {
            return new View(400, "缺少参数");
        }

        String userName = URLDecoder.decode(params.get("username"), "utf-8");
        HttpEntity httpEntity = new FileEntity(mFile);
        return new View(200, httpEntity);
    }
}
