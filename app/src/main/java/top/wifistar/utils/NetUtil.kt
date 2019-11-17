package top.wifistar.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import android.util.Log
import top.wifistar.BuildConfig
import top.wifistar.app.AppExecutor
import top.wifistar.constant.NetConstant
import top.wifistar.event.NetStateEvent
import java.util.concurrent.Callable
import java.util.concurrent.Executors

/**
 * Created by boyla on 2019/8/5.
 */
/**
 * 网络工具类
 * 参考{@url https://developer.android.com/training/basics/network-ops/managing.html}
 * 需要权限:<br>
 * <p>{@link Manifest.permission#ACCESS_NETWORK_STATE}</p>
 * ConnectivityManager: Answers queries about the state of network connectivity. It also notifies applications when network connectivity changes.
 * NetworkInfo: Describes the status of a network interface of a given type (currently either Mobile or Wi-Fi).
 */
class NetUtil {
    companion object {
        private const val TAG = "NetUtil"
        private val D = BuildConfig.DEBUG
        private val sNetConnChangedReceiver = NetConnChangedReceiver()
        private val sNetConnChangedListeners = ArrayList<NetConnChangedListener>()

        interface NetConnChangedListener {
            public fun onNetConnChanged(connectStatus: ConnectStatus)
        }

        enum class ConnectStatus {
            NO_NETWORK,
            WIFI,
            MOBILE,
            MOBILE_2G,
            MOBILE_3G,
            MOBILE_4G,
            MOBILE_UNKNOWN,
            OTHER,
            NO_CONNECTED
        }

        /**
         * 网络接口是否可用（即网络连接是否可行）和/或连接（即是否存在网络连接，是否可以建立套接字并传递数据）
         *
         * @param context 上下文
         * @return {@code true} 网络可用
         */
        public fun isNetConnected(context: Context): Boolean {
            val activeInfo = getActiveNetworkInfo(context)
            return activeInfo?.isConnected ?: false
        }

        /**
         * 是否移动数据连接
         *
         * @param context 上下文
         * @return {@code true} 移动数据连接
         */
        public fun isMobileConnected(context: Context): Boolean {
            val activeInfo = getActiveNetworkInfo(context)
            return activeInfo?.run { isConnected && type == ConnectivityManager.TYPE_MOBILE }
                    ?: false
        }

        /**
         * 是否2G网络连接
         *
         * @param context 上下文
         * @return {@code true} 2G网络连接
         */
        public fun is2GConnected(context: Context): Boolean {
            if (!isNetConnected(context)) {
                return false
            }
            val activeInfo = getActiveNetworkInfo(context)
            val subtype = activeInfo?.subtype
            return when (subtype) {
                TelephonyManager.NETWORK_TYPE_GPRS,
                TelephonyManager.NETWORK_TYPE_GSM,
                TelephonyManager.NETWORK_TYPE_EDGE,
                TelephonyManager.NETWORK_TYPE_CDMA,
                TelephonyManager.NETWORK_TYPE_1xRTT,
                TelephonyManager.NETWORK_TYPE_IDEN -> true
                else -> false
            }
        }

        /**
         * 是否3G网络连接
         *
         * @param context 上下文
         * @return {@code true} 3G网络连接
         */
        public fun is3GConnected(context: Context): Boolean {
            if (!isNetConnected(context)) {
                return false
            }
            val activeInfo = getActiveNetworkInfo(context)
            val subtype = activeInfo?.subtype
            return when (subtype) {
                TelephonyManager.NETWORK_TYPE_UMTS,
                TelephonyManager.NETWORK_TYPE_EVDO_0,
                TelephonyManager.NETWORK_TYPE_EVDO_A,
                TelephonyManager.NETWORK_TYPE_HSDPA,
                TelephonyManager.NETWORK_TYPE_HSUPA,
                TelephonyManager.NETWORK_TYPE_HSPA,
                TelephonyManager.NETWORK_TYPE_EVDO_B,
                TelephonyManager.NETWORK_TYPE_EHRPD,
                TelephonyManager.NETWORK_TYPE_HSPAP,
                TelephonyManager.NETWORK_TYPE_TD_SCDMA -> true
                else -> false
            }
        }

        /**
         * 是否4G网络连接
         *
         * @param context 上下文
         * @return {@code true} 4G网络连接
         */
        public fun is4GConnected(context: Context): Boolean {
            if (!isNetConnected(context)) {
                return false
            }
            val activeInfo = getActiveNetworkInfo(context)
            val subtype = activeInfo?.subtype
            return when (subtype) {
                TelephonyManager.NETWORK_TYPE_LTE,
                TelephonyManager.NETWORK_TYPE_IWLAN -> true
                else -> false
            }
        }

        /**
         * 获取移动网络运营商名称
         * <lu>
         * <li>中国联通</li>
         * <li>中国移动</li>
         * <li>中国电信</li>
         * </lu>
         *
         * @param context 上下文
         * @return 移动网络运营商名称
         */
        public fun getNetworkOperatorName(context: Context): String {
            val tm = context
                    .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return tm.networkOperatorName
        }

        /**
         * 获取移动终端类型
         *
         * @param context 上下文
         * @return 手机制式
         * <ul>
         * <li>{@link TelephonyManager#PHONE_TYPE_NONE } : 0 手机制式未知</li>
         * <li>{@link TelephonyManager#PHONE_TYPE_GSM  } : 1 手机制式为GSM，移动和联通</li>
         * <li>{@link TelephonyManager#PHONE_TYPE_CDMA } : 2 手机制式为CDMA，电信</li>
         * <li>{@link TelephonyManager#PHONE_TYPE_SIP  } : 3</li>
         * </ul>
         */
        public fun getPhoneType(context: Context): Int {
            val tm = context
                    .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return tm.phoneType
        }

        /**
         * 判断是否Wifi连接
         *
         * @param context 上下文
         * @return true 如果是wifi连接
         */
        public fun isWifiConnected(context: Context): Boolean {
            val activeInfo = getActiveNetworkInfo(context)
            return activeInfo?.run { isConnected && type == ConnectivityManager.TYPE_WIFI } ?: false
        }

        private class NetConnChangedReceiver : BroadcastReceiver() {

            override public fun onReceive(context: Context, intent: Intent) {
                log("onReceive")
                val activeInfo = getActiveNetworkInfo(context)
                if (activeInfo == null) {
                    broadcastConnStatus(ConnectStatus.NO_NETWORK)
                } else if (activeInfo!!.isConnected) {
                    val networkType = activeInfo!!.type
                    if (ConnectivityManager.TYPE_WIFI == networkType) {
                        broadcastConnStatus(ConnectStatus.WIFI)
                    } else if (ConnectivityManager.TYPE_MOBILE == networkType) {
                        broadcastConnStatus(ConnectStatus.MOBILE)
                        val subtype = activeInfo!!.subtype
                        if (TelephonyManager.NETWORK_TYPE_GPRS == subtype
                                || TelephonyManager.NETWORK_TYPE_GSM == subtype
                                || TelephonyManager.NETWORK_TYPE_EDGE == subtype
                                || TelephonyManager.NETWORK_TYPE_CDMA == subtype
                                || TelephonyManager.NETWORK_TYPE_1xRTT == subtype
                                || TelephonyManager.NETWORK_TYPE_IDEN == subtype
                        ) {
                            broadcastConnStatus(ConnectStatus.MOBILE_2G)
                        } else if (TelephonyManager.NETWORK_TYPE_UMTS == subtype
                                || TelephonyManager.NETWORK_TYPE_EVDO_0 == subtype
                                || TelephonyManager.NETWORK_TYPE_EVDO_A == subtype
                                || TelephonyManager.NETWORK_TYPE_HSDPA == subtype
                                || TelephonyManager.NETWORK_TYPE_HSUPA == subtype
                                || TelephonyManager.NETWORK_TYPE_HSPA == subtype
                                || TelephonyManager.NETWORK_TYPE_EVDO_B == subtype
                                || TelephonyManager.NETWORK_TYPE_EHRPD == subtype
                                || TelephonyManager.NETWORK_TYPE_HSPAP == subtype
                                || TelephonyManager.NETWORK_TYPE_TD_SCDMA == subtype
                        ) {
                            broadcastConnStatus(ConnectStatus.MOBILE_3G)
                        } else if (TelephonyManager.NETWORK_TYPE_LTE == subtype || TelephonyManager.NETWORK_TYPE_IWLAN == subtype) {
                            broadcastConnStatus(ConnectStatus.MOBILE_4G)
                        } else {
                            broadcastConnStatus(ConnectStatus.MOBILE_UNKNOWN)
                        }
                    } else {
                        broadcastConnStatus(ConnectStatus.OTHER)
                    }
                } else {
                    broadcastConnStatus(ConnectStatus.NO_CONNECTED)
                }
            }
        }

        /**
         * 注册网络接收者
         * @param context 上下文
         */
        public fun registerNetConnChangedReceiver(context: Context) {
            val filter = IntentFilter()
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            context.registerReceiver(sNetConnChangedReceiver, filter)
        }

        /**
         * 取消注册网络接收者
         * * @param context 上下文
         */
        public fun unregisterNetConnChangedReceiver(context: Context) {
            context.unregisterReceiver(sNetConnChangedReceiver)
            sNetConnChangedListeners.clear()
        }

        /**
         * 添加网络状态变化监听
         *
         * @param listener 网络连接状态改变监听
         */
        public fun addNetConnChangedListener(listener: NetConnChangedListener) {
            val result = sNetConnChangedListeners.add(listener)
            log("addNetConnChangedListener: $result")
        }

        /**
         * 移除指定网络变化监听
         *
         * @param listener 网络连接状态改变监听
         */
        public fun removeNetConnChangedListener(listener: NetConnChangedListener) {
            val result = sNetConnChangedListeners.remove(listener)
            log("removeNetConnChangedListener: $result")
        }

        public fun broadcastConnStatus(connectStatus: ConnectStatus) {
            val size = sNetConnChangedListeners.size
            if (size == 0) {
                return
            }
            for (i in 0 until size) {
                sNetConnChangedListeners[i].onNetConnChanged(connectStatus)
            }
        }

        public fun getActiveNetworkInfo(context: Context): NetworkInfo? {
            val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connMgr.activeNetworkInfo
        }

        public fun log(msg: String) {
            if (D) {
                Log.e(TAG, msg)
            }
        }

        fun isNetworkOnline(): Boolean {
            val executor = Executors.newCachedThreadPool()
            val task = TaskIsNetworkOnline()
            val result = executor.submit(task)
            executor.shutdown()
            return result.get()
        }

        internal class TaskIsNetworkOnline : Callable<Boolean> {
            @Throws(Exception::class)
            override fun call(): Boolean {
                val runtime = Runtime.getRuntime()
                try {
                    val ipProcess = runtime.exec("ping -c 3 www.baidu.com")
                    val exitValue = ipProcess.waitFor()
                    Log.i("Avalible", "Process:$exitValue")
                    return exitValue == 0
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return false
            }
        }

        fun refreshNetState(ctx: Context) {
            AppExecutor.getInstance().postWork {
                val hasNet = isNetConnected(ctx)
                if (hasNet && isNetworkOnline()) {
                    EventUtils.post(NetStateEvent(NetConstant.WAN))
                    return@postWork
                }
                val hasWiFi = isWifiConnected(ctx)
                if (hasWiFi) {
                    EventUtils.post(NetStateEvent(NetConstant.WIFI))
                    return@postWork
                }
                EventUtils.post(NetStateEvent(NetConstant.NONET))
            }
        }
    }
}