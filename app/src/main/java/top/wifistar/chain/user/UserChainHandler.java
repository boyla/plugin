package top.wifistar.chain.user;

import java.util.ArrayList;
import java.util.List;

import top.wifistar.bean.bmob.User;

/**
 * Created by boyla on 2018/6/2.
 */

public class UserChainHandler {

    List<UserRequest> allNodes = new ArrayList<>();

    UserRequest memRequest = new MemoryUserRequest();

    UserRequest dbRequest = new DBUserRequest();

    UserRequest netRequest = new NetUserRequest();

    public UserChainHandler() {
        allNodes.add(memRequest);
        allNodes.add(dbRequest);
        allNodes.add(netRequest);
    }

    public User getUserByLocal(String userId) {
        return getUserByLocal(userId,null);
    }

    public User getUserByLocal(String userId,NetUserRequest.NetRequestCallBack callBack) {
        for (UserRequest request : allNodes) {
            User temp = request.handleRequest(userId);
            if (temp != null) {
                if (request instanceof DBUserRequest) {
                    memRequest.insertOrUpdate(temp);
                }
                if (callBack != null){
                    callBack.onSuccess(temp);
                }
                return temp;
            } else {
                if (request instanceof NetUserRequest) {
                    getUserByNet(userId,callBack);
                }
            }
        }
        return null;
    }

    public void getUserByNet(String userId, NetUserRequest.NetRequestCallBack callBack) {
        ((NetUserRequest) netRequest).handleNetRequest(userId, new NetUserRequest.NetRequestCallBack() {
            @Override
            public void onSuccess(User user) {
                dbRequest.insertOrUpdate(user);
                memRequest.insertOrUpdate(user);
                if (callBack != null)
                    callBack.onSuccess(user);
            }

            @Override
            public void onFailure(String msg) {
                if (callBack != null)
                    callBack.onFailure(msg);
            }
        });
    }
}
