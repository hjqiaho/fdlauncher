package com.fundot.notice

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import com.fundot.launcher.FundotLauncherHelper

class MainActivity :Activity(){

    private var loginCallback: FundotLauncherHelper.FundotLoginStateCallback =
        object : FundotLauncherHelper.FundotLoginStateCallback{
        override fun logout(code: Int, message: String) {
            //需要退出到登录页面

        }
    }

    private var topAppChangeCallback: FundotLauncherHelper.FundotTopAppChangeCallback =
        object : FundotLauncherHelper.FundotTopAppChangeCallback{
            override fun topApp(packageName: String, activityName: String, startTime: Long) {

            }

        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //监听 管控退出登录广播
        FundotLauncherHelper.addFundotLoginStateCallback(loginCallback)
        FundotLauncherHelper.addFundotTopAppChangeCallback(topAppChangeCallback)


        findViewById<Button>(R.id.btn_login).text = "主页 去登录"
        findViewById<Button>(R.id.btn_login)?.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FundotLauncherHelper.removeFundotLoginStateCallback(loginCallback)
        FundotLauncherHelper.removeFundotTopAppChangeCallback(topAppChangeCallback)
    }

}
