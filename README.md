# fdlauncher

# 引入
```
implementation 'com.github.hjqiaho:fdlauncher:2.0'
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
   FundotLauncherHelper.setAppHiddenListener(this,object:FundotLauncherHelper.FundotAppHiddenCallback{
       //这里需要显示或者隐藏图标的时候调用 此时图标需要自己处理 查询所有图标减去需要隐藏的图标
       override fun needReloadApp(hideApps: List<String>) {
           Log.i("needReloadApp", "hideApps = ${hideApps.toString()}")
       }
   })
```
***监听 管控退出登录事件***
```
//监听 管控退出登录广播
FundotLauncherHelper.addFundotLoginStateCallback(object :FundotLauncherHelper.FundotLoginStateCallback{
    override fun logout(code: Int, message: String) {
        //需要退出到登录页面

    }
})
```
***一些操作***
```
//获取管控禁用应用列表
FundotLauncherHelper.getHiddenAppList(this)


//登录 登录成功后再跳转
val loginInfo = FdUserInfoModel()
loginInfo.userid = "123"
loginInfo.username = "li"
loginInfo.realname = "李"
loginInfo.channel = ""
loginInfo.timeStamp = System.currentTimeMillis()
loginInfo.sign = getCunwSign(loginInfo.userid,loginInfo.channel,loginInfo.timeStamp)
FundotLauncherHelper.login(this,GsonUtils.jsonCreate(loginInfo),object : FundotLauncherHelper.FundotLoginCallback{
    override fun loginSuccess(message: String) {
        Toast.makeText(this@LoginActivity,"登录成功",Toast.LENGTH_LONG).show()
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

/**
 * TODO
 * 打开管控后台入口页面 需要在登录页面和主页面集成一个 比如连点5次头像打开管控页面
 * 登录页面需要集成一个打开wifi按钮 网络认证页面
 * */
FundotLauncherHelper.sendOpenAdminBoardCast(this)

```

* 打开管控后台入口页面 需要在登录页面和主页面集成一个 比如连点5次头像打开管控页面

* 登录页面需要集成一个打开wifi按钮 网络认证页面
