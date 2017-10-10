package top.wifistar.http;

import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

public class AndServerTestHandler implements HttpRequestHandler {
    @Override
    public void handle(HttpRequest rq, HttpResponse response, HttpContext ct) throws HttpException, IOException {
        response.setEntity(new StringEntity("请求成功。", "utf-8"));
    }
}