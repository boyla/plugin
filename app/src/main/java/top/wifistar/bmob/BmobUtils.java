package top.wifistar.bmob;

import android.util.Log;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import top.wifistar.bean.bmob.User;

/**
 * Created by boyla on 2017/9/29.
 */

public class BmobUtils {
    public static void updateUser(String objId, String profileId, String nickName, String avatar) {
        User user = new User();
        user.setValue("name",""+nickName);
        user.setValue("id",""+profileId);
        user.setValue("headUrl",""+avatar);

        user.update(objId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i("BmobUtils:","更新成功");
                }else{
                    Log.i("BmobUtils:","更新失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
}
