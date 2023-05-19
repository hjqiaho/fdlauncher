package com.fundot.notice

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.fundot.launcher.FdUserInfoModel
import com.fundot.launcher.FundotLauncherHelper
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class LoginActivity :Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<Button>(R.id.btn_login).text = "登录页面 登录"
        findViewById<Button>(R.id.btn_login)?.setOnClickListener {
            //登录 登录成功后再跳转
            val loginInfo = FdUserInfoModel()
            loginInfo.userid = "123"
            loginInfo.username = "li"
            loginInfo.realname = "李"
            loginInfo.channel = ""
            loginInfo.timeStamp = System.currentTimeMillis()
            loginInfo.sign = getCunwSign(loginInfo.channel,loginInfo.userid,loginInfo.timeStamp)
            FundotLauncherHelper.login(this,GsonUtils.jsonCreate(loginInfo),object : FundotLauncherHelper.FundotLoginCallback{
                override fun loginSuccess(message: String) {
                    Toast.makeText(this@LoginActivity,"登录成功！",Toast.LENGTH_LONG).show()
                    finish()
                }

                override fun loginFail(code: Int, message: String) {
                    Toast.makeText(this@LoginActivity,"登录失败:($code)$message",Toast.LENGTH_LONG).show()

                }
            });

        }
    }


    fun getCunwSign(channel: String = "", userid: String, timeStamp: Long): String {
        val plain = String.format("xyw_fbf28fa_%s_%s_%s_%s", "com.cunw", channel, userid, timeStamp)
        return md5(plain)
    }
    fun md5(md5: String): String {
        try {
            val md = MessageDigest.getInstance("MD5")
            val array = md.digest(md5.toByteArray(StandardCharsets.UTF_8))
            val sb = StringBuilder()
            for (anArray in array) {
                sb.append(Integer.toHexString(anArray.toInt() and 0xFF or 0x100).substring(1, 3))
            }
            return sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}
