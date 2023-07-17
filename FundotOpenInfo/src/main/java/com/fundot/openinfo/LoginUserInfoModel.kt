package com.fundot.openinfo

import androidx.annotation.Keep
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

@Keep
class LoginUserInfoModel {

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

}

