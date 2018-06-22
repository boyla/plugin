package top.wifistar.httpserver;

import com.yanzhenjie.andserver.SimpleRequestHandler;
import com.yanzhenjie.andserver.view.View;

import org.apache.commons.io.IOUtils;
import org.apache.httpcore.HttpRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import top.wifistar.app.App;
import org.apache.httpcore.HttpEntity;
import org.apache.httpcore.HttpException;
import java.io.IOException;
import org.apache.httpcore.entity.FileEntity;

/**
 * Created by boyla on 2018/6/20.
 */

public class FileHandler extends SimpleRequestHandler {

    @Override
    public View handle(HttpRequest request) throws HttpException, IOException {
        // 为了示例，创建一个临时文件。
        File file = File.createTempFile("AndServer", ".txt", new File("/dfdf"));
        OutputStream outputStream = new FileOutputStream(file);
        IOUtils.write("天上掉下个林妹妹。", outputStream, Charset.defaultCharset());

        HttpEntity httpEntity = new FileEntity(file);
        View view = new View(200, httpEntity);
        view.addHeader("Content-Disposition", "attachment;filename=AndServer.txt");
        return view;
    }
}
