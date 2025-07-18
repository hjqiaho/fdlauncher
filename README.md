# fdlauncher

# 引入
```
implementation 'com.github.hjqiaho:fdlauncher:2.21'
```


# 初始化
*******在Application 的onCreate方法中*******

```
/**
 * 初始化
 * application：在Application中初始化
 * caller：对象 固定不用修改
 *
 * */
FundotLauncherHelper.register(this,"com.cunw")


```

***监听隐藏应用事件***
```
//监听应用禁用启用应用后刷新桌面图标
FundotLauncherHelper.setAppReloadListener(this,object:FundotLauncherHelper.FundotAppReloadCallback{
    //这里需要显示或者隐藏图标
    override fun needHidenApp(hidenApps: List<String>) {
    }

    override fun needShowApp(allowApps: List<String>, allowFdApps: List<FdAppInfo>) {
    }
})
```

***监听 管控退出登录事件***
```
//监听 管控退出登录广播
  private var loginCallback: FundotLauncherHelper.FundotLoginStateCallback =
        object : FundotLauncherHelper.FundotLoginStateCallback{
        override fun logout(code: Int, message: String) {
            //需要退出到登录页面

        }
    }
FundotLauncherHelper.addFundotLoginStateCallback(loginCallback)
```

***监听 前台应用切换事件***
```
//监听前台应用切换
private var topAppChangeCallback: FundotLauncherHelper.FundotTopAppChangeCallback =
        object : FundotLauncherHelper.FundotTopAppChangeCallback{
            override fun topApp(packageName: String, activityName: String, startTime: Long) {

            }

        }
FundotLauncherHelper.addFundotTopAppChangeCallback(topAppChangeCallback)
```

***监听和获取管控信息***
```
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

```

***一些操作***
```
//获取需要显示的应用列表
FundotLauncherHelper.getShowAppList(this)


//登录 登录成功后再跳转
val loginInfo = FdUserInfoModel()
loginInfo.userid = "123"
loginInfo.username = "li"
loginInfo.realname = "李"
loginInfo.channel = ""
loginInfo.timeStamp = System.currentTimeMillis()
loginInfo.sign = SignCheckHelper.getLoginSign("xyw_fbf28fa",loginInfo.channel,loginInfo.userid,loginInfo.timeStamp)
FundotLauncherHelper.login(this,GsonUtils.jsonCreate(loginInfo),object : FundotLauncherHelper.FundotLoginCallback{
    override fun loginSuccess(message: String) {
        Toast.makeText(this@LoginActivity,"登录成功！",Toast.LENGTH_LONG).show()
        finish()
    }

    override fun loginFail(code: Int, message: String) {
        Toast.makeText(this@LoginActivity,"登录失败:($code)$message",Toast.LENGTH_LONG).show()

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

//添加安装白名单应用
FundotLauncherHelper.addInstallAppWhite(this,packageName,channel,userid,timeStamp,sign)
//删除安装白名单应用
FundotLauncherHelper.removeInstallAppWhite(this,packageName,channel,userid,timeStamp,sign)
//设置安装白名单应用
FundotLauncherHelper.setInstallAppWhiteList(this,packageNames,channel,userid,timeStamp,sign)

//恢复出厂
FundotLauncherHelper.factoryReset(this,channel,userid,timeStamp,sign)

//桌面发送数据到管控
FundotLauncherHelper.sendUpdateLauncherBoardCast(this,data,timeStamp,sign)

//卸载应用接口
sign = SignCheckHelper.getDataSign(key,packageName,timeStamp)
FundotLauncherHelper.sendUninstallBoardCast(this,packageName,timeStamp,sign)      

/**
 * TODO
 * 打开管控后台入口页面 需要在登录页面和主页面集成一个 比如连点5次头像打开管控页面
 * 登录页面需要集成一个打开wifi按钮 网络认证页面
 * */
FundotLauncherHelper.sendOpenAdminBoardCast(this)

```

* 打开管控后台入口页面 需要在登录页面和主页面集成一个 比如连点5次头像打开管控页面

* 登录页面需要集成一个打开wifi按钮 网络认证页面
