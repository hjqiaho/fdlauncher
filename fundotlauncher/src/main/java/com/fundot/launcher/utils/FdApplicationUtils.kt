package com.fundot.launcher.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build


class FdApplicationUtils{

    private object ApplicationUtilsHolder{
        val holder = FdApplicationUtils()
    }
    companion object {
        @JvmField
        val instance: FdApplicationUtils = ApplicationUtilsHolder.holder
    }
    private var mPackageManager: PackageManager? = null

    fun init(context:Context){
        mPackageManager = context.packageManager
    }

    /**
     * 获取所有应用包名集合
     *
     * @return
     */
    @get:Synchronized
    val allPackageNameList: List<String>
        get() {
            val mAllPackages: MutableList<String> = arrayListOf()
            mPackageManager?.let {packageManager ->
                try {
                    val mainIntent = Intent(Intent.ACTION_MAIN, null)
                    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                    val resolveInfoList = packageManager.queryIntentActivities(mainIntent, PackageManager.MATCH_ALL)
                    resolveInfoList.forEach { info->
                        try {
                            val packageName = info.activityInfo.packageName
                            val packageInfo = packageManager.getPackageInfo(packageName, 0)
                            if (packageInfo != null) {
                                if (!mAllPackages.contains(packageName)){
                                    mAllPackages.add(packageName)
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
            return mAllPackages
        }


    /**
     * 根据包名获取PackageInfo
     *
     * @param packageName
     * @return
     */
    fun getPackageInfo(packageName: String?): PackageInfo? {
        try {
            return packageName?.let { mPackageManager?.getPackageInfo(it, 0) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    /**
     * 根据包名获取应用基本信息
     *
     * @param packageName
     * @return
     */
    fun getAppInfoByPackageName(packageName: String?): FdAppInfo? {
        if (packageName.isNullOrEmpty()){
            return null
        }
        val appInfo = FdAppInfo()
        appInfo.packageName = packageName
       try {
           val mPackageInfo = getPackageInfo(packageName)
           if (mPackageInfo != null) {
               appInfo.packageLabel = mPackageManager?.let { mPackageInfo.applicationInfo.loadLabel(it) } as String
               appInfo.appIcon = mPackageManager?.let { mPackageInfo.applicationInfo.loadIcon(it) }
               appInfo.versionName = mPackageInfo.versionName
               appInfo.isSystemApp = mPackageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                   appInfo.versionCode = mPackageInfo.longVersionCode
               } else {
                   appInfo.versionCode = mPackageInfo.versionCode.toLong()
               }
           }
       }catch (e:Exception) {
           e.printStackTrace()
       }
        return appInfo
    }

    /**
     * 根据包名获取应用基本信息 版本号
     *
     * @param packageName
     * @return versionCode
     */
    fun getVersionCode(packageName: String?): Long {
        val mPackageInfo = getPackageInfo(packageName)
        return if (mPackageInfo != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                mPackageInfo.longVersionCode
            } else {
                mPackageInfo.versionCode.toLong()
            }
        } else 0L
    }

    /**
     * 根据包名获取应用基本信息 版本名称
     *
     * @param packageName
     * @return versionName
     */
    fun getVersionName(packageName: String?): String {
        val mPackageInfo = getPackageInfo(packageName)
        return if (mPackageInfo != null) {
            mPackageInfo.versionName
        } else ""
    }

    /**
     * 根据包名获取应用名称
     *
     * @param packageName
     * @return AppName
     */
    fun getAppName(packageName: String?): String? {
        try {
            val mPackageInfo = getPackageInfo(packageName)
            if (mPackageInfo != null) {
                return mPackageManager?.let { mPackageInfo.applicationInfo.loadLabel(it).toString() }
            }
        } catch (th: Throwable) {
            th.printStackTrace()
        }
        return packageName
    }
    /**
     * 根据包名获取应用图标
     *
     * @param packageName
     * @return AppIcon
     */
    fun getAppIcon(packageName: String?): Drawable? {
        try {
            val mPackageInfo = getPackageInfo(packageName)
            if (mPackageInfo != null) {
                return mPackageManager?.let { mPackageInfo.applicationInfo.loadIcon(it) }
            }
        } catch (th: Throwable) {
            th.printStackTrace()
        }
        return null
    }

    /**
     *  根据包名判断是否为系统应用
     *
     * @param pi
     * @return
     */
    fun isSystemApp(packageName: String?): Boolean {
        val packageInfo = packageName?.let { mPackageManager?.getPackageInfo(it, 0) } ?: return false
        return packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

}