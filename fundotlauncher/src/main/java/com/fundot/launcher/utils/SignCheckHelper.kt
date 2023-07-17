package com.fundot.launcher.utils

import com.fundot.launcher.FundotLauncherHelper
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object SignCheckHelper {

    //登录及安装应用白名单签名
    @JvmStatic
    fun getLoginSign(key:String,channel: String = "", userid: String, timeStamp: Long): String {
        val plain = String.format("${key}_%s_%s_%s_%s", FundotLauncherHelper.getCaller(), channel ?: "", userid ?: "", timeStamp)
        return md5(plain)
    }
    //桌面保存数据前面
    @JvmStatic
    fun getDataSign(key:String,data: String?, timeStamp: Long): String {
        val plain = String.format("${key}_%s_%s_%s",FundotLauncherHelper.getCaller(), data ?: "", timeStamp)
        return md5(plain)
    }
    private fun md5(md5: String): String {
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