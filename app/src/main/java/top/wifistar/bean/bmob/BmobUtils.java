package top.wifistar.bean.bmob;

import android.util.Log;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteBatchListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import io.realm.RealmObject;
import top.wifistar.realm.BaseRealmDao;
import top.wifistar.realm.IMUserRealm;
import top.wifistar.realm.UserRealm;

/**
 * Created by boyla on 2017/9/29.
 */

public class BmobUtils {
    public static void updateUser(User user, String profileId, String nickName, Integer sex) {

        user.name = nickName;
        user.id = profileId;
        user.sex = sex;
        BaseRealmDao.insertOrUpdate(user.toRealmObject());

        user.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("ShortUser:", "更新成功");
                } else {
                    Log.i("ShortUser:", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    public static void updateUser(User user) {
        BaseRealmDao.insertOrUpdate(user.toRealmObject());
        user.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("ShortUser:", "更新成功");
                } else {
                    Log.i("ShortUser:", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    public static void deleteBmobFile(String[] urls){
        BmobFile.deleteBatch(urls, new DeleteBatchListener() {

            @Override
            public void done(String[] failUrls, BmobException e) {
                if (e == null) {
                    Log.i("删除moment关联图片：", "全部删除成功");
                } else {
                    if (failUrls != null) {
                        Log.i("删除moment关联图片失败个数：", failUrls.length + "," + e.toString());
                    } else {
                        Log.i("全部文件删除失败：", e.getErrorCode() + "," + e.toString());
                    }
                }
            }
        });
    }

    //查询单条数据
    public static void querySingleUser(String objId,BmobDoneListener listener){
        BmobQuery<User> query = new BmobQuery<>();
        query.getObject(objId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e==null){
                    listener.onSuccess(user);
                }else{
                    listener.onFailure(e.getMessage());
                }
            }
        });
    }

    public interface BmobDoneListener<T extends BmobObject>{
        void onSuccess(T res);
        void onFailure(String msg);
    }
}
