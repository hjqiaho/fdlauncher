package com.fundot.launcher

import android.text.TextUtils
import androidx.annotation.Keep

@Keep
class FdUserInfoModel {

    //渠道 后台策略中，账号会按渠道区分 可分渠道配置策略 空为默认渠道
    var channel = ""
    var channelid = ""
    var userid = ""
    var username = ""
    var realname = ""
    var mobile = ""
    var email = ""
    var sex = 0
    var headphoto = ""
    var classid = ""
    var classname = ""
    var gradeid = ""
    var gradename = ""
    var schoolid = ""
    var schoolname = ""
    var timeStamp:Long = 0L
    var sign = ""
}

