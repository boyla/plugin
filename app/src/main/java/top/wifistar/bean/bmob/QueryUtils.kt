package top.wifistar.bean.bmob

import cn.bmob.v3.BmobObject
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import top.wifistar.realm.BaseRealmDao
import top.wifistar.utils.LogUtils

/**
 * Created by boyla on 2017/9/22.
 */
class QueryUtils {

    companion object {
        fun  queryFollow(objId: String, callBack: QueryCallBack<Follow>) {
            val query = BmobQuery<Follow>()
            query.getObject(objId, object : QueryListener<Follow>() {
                override fun done(result: Follow, e: BmobException?) {
                    if (e == null) {
                        callBack.onSuccess(result)
                    } else {
                        LogUtils.i("失败：" + e.message + "," + e.errorCode)
                    }
                }
            })
        }
    }


    fun queryProfileById(profileId: String, listener: OnQueryBmobSuccess) {
        val query = BmobQuery<UserProfile>()
        query.getObject(profileId,
                object : QueryListener<UserProfile>() {
                    override fun done(userProfile: UserProfile, e: BmobException) {
                        BaseRealmDao.insertOrUpdate(userProfile.toRealmObject())
                        listener.onSuccess()
                    }
                })
    }

    interface QueryCallBack<T : BmobObject> {
        fun onSuccess(obj: T)
    }
}


