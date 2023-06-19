package com.fundot.launcher

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
        }

        private var loginCallback: FundotLoginCallback? = null
        private var logoutCallback: FundotLogoutCallback? = null
        private var fundotLoginStateCallbacks: MutableList<FundotLoginStateCallback> = arrayListOf()
        private var fundotTopAppChangeCallbacks: MutableList<FundotTopAppChangeCallback> = arrayListOf()

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
        //保持应用在前台
        @JvmStatic
        fun sendKeepAppBoardCast(context: Context,packagename: String) {
            try{
                val intent = Intent("com.fundot.p4bu.nav-hide")
                intent.putExtra("packagename",packagename)
                context.sendBroadcast(intent)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        //取消保持应用在前台
        @JvmStatic
        fun sendRemoveKeepAppBoardCast(context: Context) {
            try{
                val intent = Intent("com.fundot.p4bu.nav-show")
                context.sendBroadcast(intent)
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
                intent.putExtra("sign",sign)
                context.sendBroadcast(intent)
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
                intent.putExtra("sign",sign)
                context.sendBroadcast(intent)
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
                intent.putExtra("sign",sign)
                context.sendBroadcast(intent)
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
                intent.putExtra("sign",sign)
                context.sendBroadcast(intent)
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
                context.registerReceiver(receiver, filter)
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
                    if (result){
                        loginCallback?.loginSuccess(message ?: "")
                    }else{
                        loginCallback?.loginFail(-1,message ?: "登录失败，请重试。")
                    }
                    loginCallback = null
                }else if ("com.fundot.p4bu.logout" == action) {
                    val message = intent.getStringExtra("message")
                    fundotLoginStateCallbacks.forEach {
                        it.logout(-1,message ?: "你已退出登录。")
                    }
                    logoutCallback?.logoutSuccess(message ?: "")
                    logoutCallback = null
                }else if ("com.fundot.p4bu.logout-result"==action){
                    val result = intent.getBooleanExtra("result", false)
                    val message = intent.getStringExtra("message")
                    if (result){
                        logoutCallback?.logoutSuccess(message ?: "")
                    }else{
                        logoutCallback?.logoutFail(-1,message ?: "退出登录失败。")
                    }
                    logoutCallback = null
                }else if ("com.fundot.p4bu.packagename_change"==action){
                    val PackageName = intent.getStringExtra("PackageName") ?: ""
                    val ActivityName = intent.getStringExtra("ActivityName") ?: ""
                    val StartTime = intent.getLongExtra("StartTime",0L)
                    fundotTopAppChangeCallbacks.forEach {
                        it.topApp(PackageName,ActivityName,StartTime)
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