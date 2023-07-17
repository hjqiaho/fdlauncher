package com.fundot.openinfo

import androidx.annotation.Keep
import com.fundot.openinfo.utils.GsonUtil

@Keep
class FundotOpenModel {
    @JvmField
    var UserId: String = ""

    @JvmField
    var UserName: String = ""
    @JvmField
    var NickName: String = ""
    @JvmField
    var Avatar: String = ""
    @JvmField
    var Channel: String = ""

    @JvmField
    var SerialNumber: String = ""
    @JvmField
    var MacAddress: String = ""
    @JvmField
    var HostAddress: String = ""

    @JvmField
    var CardNo: String = ""

    @JvmField
    var LauncherDate: String = ""

    @JvmField
    var LoginUserInfo:String = ""
    val loginUserInfoModel:LoginUserInfoModel
        get() {
            return GsonUtil.json2Bean(LoginUserInfo,LoginUserInfoModel::class.java) ?: LoginUserInfoModel()
        }
}