package com.fundot.notice

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.fundot.launcher.FundotLauncherHelper
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar

class MainActivity : AppCompatActivity(){

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
//        ImmersionBar.with(this)
//            .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR).init()
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//A
        //监听 管控退出登录广播
        FundotLauncherHelper.addFundotLoginStateCallback(loginCallback)
        FundotLauncherHelper.addFundotTopAppChangeCallback(topAppChangeCallback)


        findViewById<Button>(R.id.btn_login).text = "主页 去登录"
        findViewById<Button>(R.id.btn_login)?.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.btn_hidden_nav)?.setOnClickListener {
            window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
          FundotLauncherHelper.sendKeepAppBoardCast(this,"")
        }
        findViewById<Button>(R.id.btn_show_nav)?.setOnClickListener {
            window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            FundotLauncherHelper.sendRemoveKeepAppBoardCast(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FundotLauncherHelper.removeFundotLoginStateCallback(loginCallback)
        FundotLauncherHelper.removeFundotTopAppChangeCallback(topAppChangeCallback)
    }

}
