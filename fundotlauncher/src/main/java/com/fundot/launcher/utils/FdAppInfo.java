package com.fundot.launcher.utils;

import android.graphics.drawable.Drawable;

import androidx.annotation.Keep;

@Keep
public class FdAppInfo {
    public String packageLabel;
    public String packageName;

    public String versionName;

    public long versionCode;

    public boolean isSystemApp;

    public Drawable appIcon;
}
