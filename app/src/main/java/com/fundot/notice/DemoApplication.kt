package com.fundot.notice

import android.app.Application
import android.util.Log
import com.fundot.launcher.FundotLauncherHelper
import com.fundot.launcher.utils.FdAppInfo
import com.fundot.openinfo.FundotOpenInfoHelper


class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        /**
         * 初始化
         * application：在Application中初始化
         * caller：对象 固定不用修改
         *
         * */
        FundotLauncherHelper.register(this,"com.cunw")

        //监听 管控退出登录广播
        FundotLauncherHelper.addFundotLoginStateCallback(object :FundotLauncherHelper.FundotLoginStateCallback{
            override fun logout(code: Int, message: String) {
                //需要退出到登录页面

            }
        })

        //监听应用禁用启用应用后刷新桌面图标
        FundotLauncherHelper.setAppReloadListener(this,object:FundotLauncherHelper.FundotAppReloadCallback{
            //这里需要显示或者隐藏图标
            override fun needHidenApp(hidenApps: List<String>) {
            }

            override fun needShowApp(allowApps: List<String>, allowFdApps: List<FdAppInfo>) {
            }
        })

        //注册监听管控用户数据变化
        FundotOpenInfoHelper.register(this,object : FundotOpenInfoHelper.FunDotInfoCallback{
            override fun fundotInfoChange() {
                //获取用户信息
                var fundotOpenModel =  FundotOpenInfoHelper.getData(this@DemoApplication)
            }
        })
        //解除注册监听管控用户数据变化
        FundotOpenInfoHelper.unregister(this)

        //获取用户信息
        var fundotOpenModel =  FundotOpenInfoHelper.getData(this@DemoApplication)
        //获取登录保存的用户信息
        var loginUserInfoModel = FundotOpenInfoHelper.getData(this@DemoApplication).loginUserInfoModel

        /*
                //获取管控禁用应用列表
               FundotLauncherHelper.getShowAppList(this)


                      //登录 登录成功后再跳转
                      FundotLauncherHelper.login(this,"",object : FundotLauncherHelper.FundotLoginCallback{
                          override fun loginSuccess(message: String) {
                          }

                          override fun loginFail(code: Int, message: String) {

                          }
                      });

                      //退出 退出成功后再跳转
                      FundotLauncherHelper.logout(this,object : FundotLauncherHelper.FundotLogoutCallback{

                          override fun logoutSuccess(message: String) {
                          }

                          override fun logoutFail(code: Int, message: String) {
                          }
                      });

                      //检查更新
                      FundotLauncherHelper.sendCheckUpdateBoardCast(this)

                      //打开应用商店
                      FundotLauncherHelper.sendOpenAppStoreBoardCast(this)

                      /**
                       * TODO
                       * 打开管控后台入口页面 需要在登录页面和主页面集成一个 比如连点5次头像打开管控页面
                       * 登录页面需要集成一个打开wifi按钮 网络认证页面
                       * */
                      FundotLauncherHelper.sendOpenAdminBoardCast(this)
                  */

    }

}