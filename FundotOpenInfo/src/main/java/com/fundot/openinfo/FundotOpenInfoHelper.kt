package com.fundot.openinfo

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.Keep
import com.fundot.openinfo.utils.DesCyUtils

import com.fundot.openinfo.utils.GsonUtil

@Keep
class FundotOpenInfoHelper {

    companion object {
        private val AUTHORITY = "com.p4bu.packageprovider"
        private val FUNDOT_OPEN_INFO = "fundot.open.info"
        private val URI_FUNDOT_OPEN_INFO = Uri.parse("content://$AUTHORITY/$FUNDOT_OPEN_INFO")

        private var fundInfoCallback: FunDotInfoCallback? = null

        @JvmStatic
        fun getData(context: Context): FundotOpenModel {
            var dataModel:FundotOpenModel? = null
            try {
                var data = context.contentResolver.getType(URI_FUNDOT_OPEN_INFO)
                Log.i("FundotOpenInfoHelper", "data = $data")
                if (data != null) {
                    data = DesCyUtils("fundotopeninfo20220803").decrypt(data)
                }
                data?.let {
                    dataModel = GsonUtil.json2Bean(it, FundotOpenModel::class.java)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return dataModel!!
        }

        //注册并监听数据变化
        @JvmStatic
        fun register(context: Context, callBack: FunDotInfoCallback? = null) {
            fundInfoCallback = callBack;
            try {
                context.contentResolver?.registerContentObserver(
                    URI_FUNDOT_OPEN_INFO,
                    true,
                    mFundotObserver
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

        }

        //解除注册
        @JvmStatic
        fun unregister(context: Context) {
            try {
                context.contentResolver.unregisterContentObserver(mFundotObserver)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            fundInfoCallback = null
        }

        private val mFundotObserver: ContentObserver =
            object : ContentObserver(Handler(Looper.getMainLooper())) {
                override fun onChange(selfChange: Boolean, uri: Uri?) {
                    fundInfoCallback?.fundotInfoChange()
                }
            }
    }

    interface FunDotInfoCallback {
        fun fundotInfoChange()
    }
}