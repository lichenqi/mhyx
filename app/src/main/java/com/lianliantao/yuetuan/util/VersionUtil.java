package com.lianliantao.yuetuan.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class VersionUtil {

//    /*接口请求版本信息*/
//    public static String getVersionCode(Context context) {
//        try {
//            PackageManager manager = context.getPackageManager();
//            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
//            String version = info.versionName;
//            return "JellyBox/" + version + " (Android, " + PhoneVersionUtil.getMode() + ", " + PhoneVersionUtil.getSystemVersion() + ")";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "JellyBox/" + "1.0.0" + "(Android, " + PhoneVersionUtil.getMode() + ", " + PhoneVersionUtil.getSystemVersion() + ")";
//        }
//    }
//
//    /*webview请求头版本信息*/
//    public static String getH5VersionCode(Context context) {
//        try {
//            PackageManager manager = context.getPackageManager();
//            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
//            String version = info.versionName;
//            return "JellyBox/" + version + " (Android, " + PhoneVersionUtil.getMode() + ", " + PhoneVersionUtil.getSystemVersion() + ")";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "JellyBox/" + "1.0.0" + "(Android, " + PhoneVersionUtil.getMode() + ", " + PhoneVersionUtil.getSystemVersion() + ")";
//        }
//    }

    /*android纯数字版本号*/
    public static String getAndroidNumVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0.0";
        }
    }

}
