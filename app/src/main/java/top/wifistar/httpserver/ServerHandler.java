/*
 * Copyright © 2016 Yan Zhenjie.
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

import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.RequestMethod;
import com.yanzhenjie.andserver.annotation.RequestMapping;

import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.entity.StringEntity;
import org.apache.httpcore.protocol.HttpContext;

import java.io.IOException;

/**
 * <p>Login Handler.</p>
 * Created by Yan Zhenjie on 2016/6/13.
 */
public class ServerHandler implements RequestHandler {

    @RequestMapping(method = {RequestMethod.GET})
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {

        if (WiFiServerService.isServerRunning()) {
            StringEntity stringEntity = new StringEntity("OK, at your service", "utf-8");
            response.setStatusCode(200);
            response.setEntity(stringEntity);
        } else {
            StringEntity stringEntity = new StringEntity("Not in service", "utf-8");
            response.setStatusCode(400);
            response.setEntity(stringEntity);
        }
    }
}
