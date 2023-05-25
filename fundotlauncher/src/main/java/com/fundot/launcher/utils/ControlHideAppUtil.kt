package com.fundot.launcher.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.fundot.launcher.FundotLauncherHelper


class ControlHideAppUtil {

    private var observer: MyDataObserver? = null
    private var mPackageManager: PackageManager? = null
    var isObserved = false
    var appReloadCallback: FundotLauncherHelper.FundotAppReloadCallback? = null

    inner class MyDataObserver(val context: Context, handler: Handler) : ContentObserver(handler) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            //处理数据变化
            loadData(context)
        }
    }

    /**
     * 监听数据变化
     * context 要传Launcher
     */
    fun observe(context: Context) {
        mPackageManager = context.packageManager
        observer = MyDataObserver(context, Handler(Looper.getMainLooper())).also {
            isObserved = try {
                context.contentResolver.registerContentObserver(URI_PACKAGE_STATUS, false, it)
                true
            } catch (ignore: Exception) {
                false
            }
        }
    }

    fun removeObserve(context: Context) {
        observer?.let { observer ->
            context.contentResolver.unregisterContentObserver(observer)
            isObserved = false
        }
    }

    //读取数据
    fun loadData(context: Context):List<FdAppInfo> {
        try {
            val cr = context.contentResolver
            val cursor = cr.query(URI_PACKAGE_STATUS, arrayOf(ID, PACKAGE_NAME, ALLOW_SHOW), null, null, null)
            val hideList = arrayListOf<String>()
            if (cursor != null) {
                cursor.moveToFirst()
                val count = cursor.count
                Log.d(TAG, "count=$count")
                if (count > 0) {
                    do {
                        val packageNameIndex = cursor.getColumnIndex(PACKAGE_NAME)
                        val packageName = cursor.getString(packageNameIndex)

                        val allowShowIndex = cursor.getColumnIndex(ALLOW_SHOW)
                        val allowShow = cursor.getInt(allowShowIndex)
                        if (allowShow == 0) {
                            hideList.add(packageName)
                        }
                    } while (cursor.moveToNext())
                }
                cursor.close()
            } else {
                Log.w(TAG, "cursor is null.")
            }

            val showAppList:List<String> = FdApplicationUtils.instance.allPackageNameList - hideList.toSet()
            val showFdAppList = showAppList.mapNotNull { FdApplicationUtils.instance.getAppInfoByPackageName(it) }
            appReloadCallback?.needHidenApp(hideList)
            appReloadCallback?.needShowApp(showAppList,showFdAppList)
            return showFdAppList
        } catch (_: Exception) {
        }
        return arrayListOf();
    }
    companion object {
        //常量定义
        private const val AUTHORITY = "com.p4bu.packageprovider"
        private const val TABLE_NAME = "package_status"
        private const val ID = "id"
        private const val PACKAGE_NAME = "package_name" //应用包名
        private const val ALLOW_SHOW = "allow_show" // 1:显示 0:隐藏
        private val URI_PACKAGE_STATUS: Uri = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
        private const val TAG = "ControlHideAppUtil"

    }
}