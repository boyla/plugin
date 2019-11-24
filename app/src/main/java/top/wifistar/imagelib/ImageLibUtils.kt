package top.wifistar.imagelib

import android.text.TextUtils

import top.wifistar.app.App
import top.wifistar.http.OkhttpUtils
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.listener.FindListener


/**
 * Created by boyla on 2019/9/8.
 */

class ImageLibUtils {

    companion object {
        private val url = "https://api.unsplash.com/photos/random?count=20&client_id=b857113bf0cbc6f62db5b4a5eba82ae4f67cec6a1427a9572d919cd14bea1c6b"
        var response = ""
        fun getUnsplashImages(): UnsplashResult? {
            var res: UnsplashResult? = null
            if (TextUtils.isEmpty(response)) {
                response = OkhttpUtils.getResponseStr(url)
            }
            if (TextUtils.isEmpty(response)) {
                return res
            }
            println("Unsplash imgs: " + response)
            res = App.gson.fromJson("{list:$response}", UnsplashResult::class.java)
            return res
        }

        fun getUpsplashUrls(): List<String>? {
            var unsplashData = getUnsplashImages()
            return unsplashData?.list?.map {
                it.urls.regular
            }
        }

        fun getBmobImageUrls(list: MutableList<String>?, action: LoadedNext) {
            val query = BmobQuery<ImageUrl>()
            query.setLimit(20)
            query.findObjects(object : FindListener<ImageUrl>() {
                override fun done(res: List<ImageUrl>?, e: BmobException?) {
                    if (e == null && res != null && res.isNotEmpty()) {
                        list?.addAll(res.map { it.url })
                        action.onNext()
                    }
                }
            })
        }
    }

    interface LoadedNext {
        fun onNext()
    }

}
