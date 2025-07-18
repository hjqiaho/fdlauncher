package com.fundot.launcher

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Base64
import android.util.Log
import com.fundot.launcher.utils.ControlHideAppUtil
import com.fundot.launcher.utils.FdAppInfo
import com.fundot.launcher.utils.FdApplicationUtils

class FundotLauncherHelper {

    companion object {
        private const val TAG = "FundotLauncherHelper"
        private val mControlHideAppUtil = ControlHideAppUtil()
        private var caller = ""
        fun getCaller():String{
            return caller;
        }

        //注册并监听数据变化
        @JvmStatic
        fun register(application: Application,caller:String) {
            FdApplicationUtils.instance.init(application)
            registerBroadCast(application)
            mControlHideAppUtil.observe(application)
            this.caller = caller
            Log.i(TAG, "register")
        }

        //解除注册
        @JvmStatic
        fun unregister(context:Context) {
           try {
               unregisterBroadCast(context)
               mControlHideAppUtil.removeObserve(context)
           }catch (e:Exception){
               e.printStackTrace()
           }
            Log.i(TAG, "unregister")
        }

        /***
         * 设置需要屏蔽的应用列表变化监听
         * **/
        @JvmStatic
        fun setAppReloadListener(context: Context, callback: FundotAppReloadCallback){
            mControlHideAppUtil.appReloadCallback = callback
            mControlHideAppUtil.loadData(context)
            Log.i(TAG, "setAppReloadListener")
        }

        private var loginCallback: FundotLoginCallback? = null
        private var logoutCallback: FundotLogoutCallback? = null
        private var fundotLoginStateCallbacks: MutableList<FundotLoginStateCallback> = arrayListOf()
        private var fundotTopAppChangeCallbacks: MutableList<FundotTopAppChangeCallback> = arrayListOf()
        private var fundotOpenAppChangeCallbacks: MutableList<FundotOpenAppChangeCallback> = arrayListOf()

        /**
         *  登录
         *
         *  点击登录 或后台登录
         *
         *  传入用户信息
         *
         *  此接口调用后需等待登录结果
         *
         *  在此回调中不要调用管控的登录或退出登录功能
         *
         * */
        @JvmStatic
        fun login(context: Context,info: String,loginCallback: FundotLoginCallback?){
            try{
                val intent = Intent("com.fundot.launcher.login")
                intent.putExtra("data", String(Base64.encode(info.toByteArray(),Base64.NO_WRAP)))
                intent.putExtra("caller", caller)
                context.sendBroadcast(intent)
                this.loginCallback = loginCallback;
                Log.i(TAG, "login")
            }catch (e:Throwable){
                Log.i(TAG, "login Throwable ="+e.localizedMessage)
                e.printStackTrace()
            }
        }
        /**
         *  退出登录
         *
         *  点击退出登录
         *
         *  此接口调用后需等待退出结果
         *  在此回调中不要调用管控的登录或退出登录功能
         *
         * */
        @JvmStatic
        fun logout(context: Context,logoutCallback: FundotLogoutCallback?) {
            try{
                val intent = Intent("com.fundot.launcher.logout")
                context.sendBroadcast(intent)
                this.logoutCallback = logoutCallback;
                Log.i(TAG, "logout")
            }catch (e:Throwable){
                Log.i(TAG, "logout Throwable ="+e.localizedMessage)
                e.printStackTrace()
            }
        }
        @JvmStatic
        fun addFundotLoginStateCallback(loginCallback: FundotLoginStateCallback?){
            loginCallback?.let { this.fundotLoginStateCallbacks.add(it) }
        }
        @JvmStatic
        fun removeFundotLoginStateCallback(loginCallback: FundotLoginStateCallback?){
            loginCallback?.let { this.fundotLoginStateCallbacks.remove(it) }
        }
        @JvmStatic
        fun addFundotTopAppChangeCallback(callback: FundotTopAppChangeCallback?){
            callback?.let { this.fundotTopAppChangeCallbacks.add(it) }
        }
        @JvmStatic
        fun removeFundotTopAppChangeCallback(callback: FundotTopAppChangeCallback?){
            callback?.let { this.fundotTopAppChangeCallbacks.remove(it) }
        }
        @JvmStatic
        fun addFundotOpenAppChangeCallback(callback: FundotOpenAppChangeCallback?){
            callback?.let { this.fundotOpenAppChangeCallbacks.add(it) }
        }
        @JvmStatic
        fun removeFundotOpenAppChangeCallback(callback: FundotOpenAppChangeCallback?){
            callback?.let { this.fundotOpenAppChangeCallbacks.remove(it) }
        }
        /**
         *  获取 需要显示的应用列表
         *
         * */
        @JvmStatic
        fun getShowAppList(context: Context):List<FdAppInfo>{
            return mControlHideAppUtil.loadData(context)
        }
        //打开管控设置页面  com.fundot.p4bu.admin
        @JvmStatic
        fun sendOpenAdminBoardCast(context: Context) {
            try{
                val intent = Intent("com.fundot.p4bu.admin")
                context.sendBroadcast(intent)
                Log.i(TAG, "sendOpenAdminBoardCast")
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        //打开管控应用商店页面  com.fundot.p4bu.store
        @JvmStatic
        fun sendOpenAppStoreBoardCast(context: Context) {
            try{
                val intent = Intent("com.fundot.p4bu.store")
                context.sendBroadcast(intent)
                Log.i(TAG, "sendOpenAppStoreBoardCast")
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        //打开管控应用商店页面  com.fundot.p4bu.store
        @JvmStatic
        fun sendDownLoadAppBoardCast(context: Context,packagename:String = "") {
            try{
                val intent = Intent("com.fundot.p4bu.store.app")
                intent.putExtra("packagename",packagename)
                context.sendBroadcast(intent)
                Log.i(TAG, "sendOpenAppStoreBoardCast")
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        //检查更新
        @JvmStatic
        fun sendCheckUpdateBoardCast(context: Context) {
            try{
                val intent = Intent("com.fundot.p4bu.check_update")
                context.sendBroadcast(intent)
                Log.i(TAG, "sendCheckUpdateBoardCast")
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        /**
         * 向管控发送桌面数据缓存
         * */
        @JvmStatic
        fun sendLauncherDataBoardCast(context: Context,data:String ,timeStamp:Long,sign:String) {
            try{
                val intent = Intent("com.fundot.p4bu.launcherdate")
                intent.putExtra("data",data)
                intent.putExtra("timeStamp",timeStamp)
                intent.putExtra("sign",sign)
                intent.putExtra("caller", caller)
                context.sendBroadcast(intent)
                Log.i(TAG, "sendLauncherDataBoardCast")
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        /**
         * 调用管控接口卸载应用
         * */
        @JvmStatic
        fun sendUninstallBoardCast(context: Context,packageName:String ,timeStamp:Long,sign:String) {
            try{
                val intent = Intent("com.fundot.p4bu.uninstall")
                intent.putExtra("packageName",packageName)
                intent.putExtra("timeStamp",timeStamp)
                intent.putExtra("sign",sign)
                intent.putExtra("caller", caller)
                context.sendBroadcast(intent)
                Log.i(TAG, "senUninstallBoardCast")
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        /**
         * 隐藏导航栏
         * 传入包名packagename 则为隐藏导航栏的同时保持前台应用
         * */
        @JvmStatic
        fun sendKeepAppBoardCast(context: Context,packagename: String) {
            try{
                val intent = Intent("com.fundot.p4bu.nav-hide")
                intent.putExtra("packagename",packagename)
                context.sendBroadcast(intent)
                Log.i(TAG, "sendKeepAppBoardCast")
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        //显示导航栏
        @JvmStatic
        fun sendRemoveKeepAppBoardCast(context: Context) {
            try{
                val intent = Intent("com.fundot.p4bu.nav-show")
                context.sendBroadcast(intent)
                Log.i(TAG, "sendRemoveKeepAppBoardCast")
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        //显示导航键home
        @JvmStatic
        fun sendShowNavBarHomeBoardCast(context: Context) {
            try{
                val intent = Intent("com.fundot.p4bu.home-show")
                context.sendBroadcast(intent)
                Log.i(TAG, "sendShowNavBarHomeBoardCast")
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        /**
         * 隐藏导航键home
         * */
        @JvmStatic
        fun sendHiddenNavBarHomeBoardCast(context: Context) {
            try{
                val intent = Intent("com.fundot.p4bu.home-hide")
                context.sendBroadcast(intent)
                Log.i(TAG, "sendHiddenNavBarHomeBoardCast")
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        //添加安装白名单应用
        @JvmStatic
        fun addInstallAppWhite(context: Context,packageName: String,channel:String,userid:String,timeStamp:Long,sign:String) {
            try{
                val intent = Intent("com.fundot.launcher.install_whitelist")
                intent.putExtra("addPackageName",packageName)
                intent.putExtra("channel",channel)
                intent.putExtra("userid",userid)
                intent.putExtra("timeStamp",timeStamp)
                intent.putExtra("caller", caller)
                intent.putExtra("sign",sign)
                context.sendBroadcast(intent)
                Log.i(TAG, "addInstallAppWhite")
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        //删除安装白名单应用
        @JvmStatic
        fun removeInstallAppWhite(context: Context,packageName: String,channel:String,userid:String,timeStamp:Long,sign:String) {
            try{
                val intent = Intent("com.fundot.launcher.install_whitelist")
                intent.putExtra("removePackageName",packageName)
                intent.putExtra("channel",channel)
                intent.putExtra("userid",userid)
                intent.putExtra("timeStamp",timeStamp)
                intent.putExtra("caller", caller)
                intent.putExtra("sign",sign)
                context.sendBroadcast(intent)
                Log.i(TAG, "removeInstallAppWhite")
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        //设置安装白名单应用 重置列表为传入数据
        @JvmStatic
        fun setInstallAppWhiteList(context: Context,packageNames: ArrayList<String>,channel:String,userid:String,timeStamp:Long,sign:String) {
            try{
                val intent = Intent("com.fundot.launcher.install_whitelist")
                intent.putStringArrayListExtra("packageNames",packageNames)
                intent.putExtra("channel",channel)
                intent.putExtra("userid",userid)
                intent.putExtra("timeStamp",timeStamp)
                intent.putExtra("caller", caller)
                intent.putExtra("sign",sign)
                context.sendBroadcast(intent)
                Log.i(TAG, "setInstallAppWhiteList")
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        //恢复出厂
        @JvmStatic
        fun factoryReset(context: Context,channel:String,userid:String,timeStamp:Long,sign:String) {
            try{
                val intent = Intent("com.fundot.launcher.factoryReset")
                intent.putExtra("channel",channel)
                intent.putExtra("userid",userid)
                intent.putExtra("timeStamp",timeStamp)
                intent.putExtra("caller", caller)
                intent.putExtra("sign",sign)
                context.sendBroadcast(intent)
                Log.i(TAG, "factoryReset")
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        @JvmStatic
        private fun registerBroadCast(context:Context) {
            try {
                val filter = IntentFilter()
                filter.addAction("com.fundot.p4bu.login-result")
                filter.addAction("com.fundot.p4bu.logout")
                filter.addAction("com.fundot.p4bu.logout-result")
                filter.addAction("com.fundot.p4bu.packagename_change")
                filter.addAction("com.fundot.p4bu.openapp")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    context.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
                }else{
                    context.registerReceiver(receiver, filter)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        @JvmStatic
        private fun unregisterBroadCast(context:Context) {
            try {
                context.unregisterReceiver(receiver)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.action
                if ("com.fundot.p4bu.login-result" == action) {
                    val result = intent.getBooleanExtra("result", false)
                    val message = intent.getStringExtra("message")
                    val code = intent.getIntExtra("code",-1)
                    Log.i(TAG,"action = $action, result = $result , message = $message ,code = $code")
                    if (result){
                        loginCallback?.loginSuccess(message ?: "")
                    }else{
                        loginCallback?.loginFail(code,message ?: "登录失败，请重试。")
                    }
                    loginCallback = null
                }else if ("com.fundot.p4bu.logout" == action) {
                    val message = intent.getStringExtra("message")
                    Log.i(TAG,"action = $action,  message = $message")
                    try {
                        synchronized(FundotLauncherHelper::class.java){
                            val iterator: MutableIterator<FundotLoginStateCallback> = fundotLoginStateCallbacks.iterator()
                            while (iterator.hasNext()) {
                                iterator.next().logout(-1,message ?: "你已退出登录。")
                            }
                        }
                    }catch (throwable:Throwable){
                        throwable.printStackTrace()
                    }
                }else if ("com.fundot.p4bu.logout-result"==action){
                    val result = intent.getBooleanExtra("result", false)
                    val message = intent.getStringExtra("message")
                    val code = intent.getIntExtra("code",-1)
                    Log.i(TAG,"action = $action, result = $result , message = $message ")
                    if (result){
                        logoutCallback?.logoutSuccess(message ?: "")
                    }else{
                        logoutCallback?.logoutFail(code,message ?: "退出登录失败。")
                    }
                    logoutCallback = null
                }else if ("com.fundot.p4bu.packagename_change"==action){
                    val PackageName = intent.getStringExtra("PackageName") ?: ""
                    val ActivityName = intent.getStringExtra("ActivityName") ?: ""
                    val StartTime = intent.getLongExtra("StartTime",0L)
                    fundotTopAppChangeCallbacks.forEach {
                        it.topApp(PackageName,ActivityName,StartTime)
                    }
                }else if ("com.fundot.p4bu.openapp"==action){
                    val PackageName = intent.getStringExtra("PackageName") ?: ""
                    val From = intent.getStringExtra("From") ?: ""
                    fundotOpenAppChangeCallbacks.forEach {
                        it.openApp(PackageName,From)
                    }
                }
            }
        }

    }


    interface FundotLoginCallback {
        /**
         *  登录结果 成功回调
         *
         *  桌面调用管控登录后
         *
         *  需要做的事
         *  1、重置登录按钮状态(若有)，关闭登录页面(若有)，进入到桌面页面
         *  2、在此回调中不要调用管控的登录或退出登录功能
         *
         * */
        fun loginSuccess(message:String)
        /**
         *  登录结果 失败回调
         *
         *  桌面调用管控登录后
         *
         *  需要做的事
         *  1、重置登录按钮状态(若有)，提示登录失败原因
         *  2、在此回调中不要调用管控的登录或退出登录功能
         *
         * */
        fun loginFail(code:Int,message:String)

    }

    interface FundotLogoutCallback {
        /**
         *  退出登录 成功回调
         *
         *  桌面调用管控退出登录后
         *
         *  需要做的事
         *  1、重置登录按钮状态(若有)，关闭登录页面(若有)，进入到桌面页面
         *  2、在此回调中不要调用管控的登录或退出登录功能
         *
         * */
        fun logoutSuccess(message:String)
        /**
         *  退出登录 失败回调
         *
         *  桌面调用管控退出登录后 管控禁止退出等
         *
         *  需要做的事
         *  1、提示错误信息， 不要退出登录，
         *  2、在此回调中不要调用管控的登录或退出登录功能
         *
         * */
        fun logoutFail(code:Int,message:String)

    }
    interface FundotLoginStateCallback {
        /**
         * 退出登录回调
         *
         * 在桌面任何时刻可能会收到
         * 原因 包括正在使用中 管控强制退出登录 管控后台账号数据异常登
         *
         *  需要做的事情有2条
         *  1、打开登录页面
         *  2、在此回调中不要调用管控的登录或退出登录功能
         *
         */
        fun logout(code:Int,message:String)

    }
    interface FundotTopAppChangeCallback {
        /**
         * 应用切换到前台回调
         *
         * 在桌面任何时刻可能会收到
         *
         *
         *  当前台应用变化之后 调用更新
         *  锁屏的时候 会穿空值
         *
         */
        fun topApp(packageName:String,activityName:String,startTime:Long)

    }
    interface FundotOpenAppChangeCallback {
        /**
         * 管控打开应用失败 需要桌面打开
         *
         * 在桌面任何时刻可能会收到
         *
         *
         *  管控打开应用失败 需要桌面打开
         * @param from:打开来源 FdAppStore:应用商店点击打开失败  FdAppKeep:应用保持打开失败
         *
         */
        fun openApp(packageName:String,from:String)

    }
    interface FundotAppReloadCallback {
        /**
         *  隐藏应用列表更新
         *
         *  登录账号 或 策略更新后 等
         *
         *  需要做的事
         *  1、隐藏应用
         *
         *
         * */
        fun needHidenApp(hidenApps: List<String>)

        /**
         *  显示应用列表
         *
         *  登录账号 或 策略更新后 等
         *
         *  需要做的事
         *  1、显示应用列表
         *
         *
         * */
        fun needShowApp(allowApps: List<String>,allowFdApps: List<FdAppInfo>)

    }
}