package top.wifistar.bean.bmob

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import top.wifistar.bean.bmob.User
import top.wifistar.realm.BaseRealmDao
import top.wifistar.utils.Utils

/**
 * Created by boyla on 2017/9/22.
 */
class QueryUtils{
     fun queryUserById(uId: String) {
        val query = BmobQuery<User>()
        query.getObject(uId, object : QueryListener<User>() {
            override fun done(result: User, e: BmobException?) {
                if (e == null) {

                } else {
                    Utils.showToast("失败：" + e.message + "," + e.errorCode)
                }
            }

        })
    }

    fun queryProfileById(profileId:String,listener: OnQueryBmobSuccess){
        val query = BmobQuery<UserProfile>()
        query.getObject(profileId,
                object : QueryListener<UserProfile>() {
                    override fun done(userProfile: UserProfile, e: BmobException) {
                        BaseRealmDao.insertOrUpdate(userProfile.toRealmObject())
                        listener.onSuccess()
                    }
                })
    }


}