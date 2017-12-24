package cn.edu.gdmec.android.mobileguard.m4appmanager.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by hitma on 2017/11/12.
 */

public class AppInfo {

    public String packageName;

    public Drawable icon;

    public  String appName;

    public String apkPath;

    public long appSize;

    public boolean isInRoom;

    public boolean isUerApp;

    public boolean isSelected = false;

    public String appVersion;

    public String installTime;

    public String certificateIssuer;

    public String appPermissions;

    public  String getAppLocation(boolean isInRoom){
        if (isInRoom){
            return "手机内存";
        }else{
            return "外部存储";
        }
    }
    public boolean isLock;
}
