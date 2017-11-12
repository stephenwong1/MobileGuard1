package cn.edu.gdmec.android.mobileguard.m4appmanager.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import cn.edu.gdmec.android.mobileguard.m4appmanager.entity.AppInfo;

/**
 * Created by hitma on 2017/11/12.
 */

public class AppInfoParser {

    public static List<AppInfo> getAppInfos(Context context) {

        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packInfos = pm.getInstalledPackages(0);
        List<AppInfo> appinfos = new ArrayList<AppInfo>();
        for (PackageInfo packInfo : packInfos) {
            AppInfo appinfo = new AppInfo();
            String packname = packInfo.packageName;
            appinfo.packageName = packname;
            Drawable icon = packInfo.applicationInfo.loadIcon(pm);
            appinfo.icon = icon;
            String appname = packInfo.applicationInfo.loadLabel(pm).toString();
            appinfo.appName = appname;

            String apkpath = packInfo.applicationInfo.sourceDir;
            appinfo.apkPath = apkpath;
            File file = new File(apkpath);
            long appSize = file.length();
            appinfo.appSize = appSize;

            int flags = packInfo.applicationInfo.flags;
            if ((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flags) != 0) {
                appinfo.isInRoom = false;
            } else {
                appinfo.isInRoom = true;
            }
            if ((ApplicationInfo.FLAG_SYSTEM & flags) != 0) {
                appinfo.isUerApp = false;
            } else {
                appinfo.isUerApp = true;
            }

            String appVersion = packInfo.versionName;
            appinfo.appVersion = appVersion;

            long time = packInfo.firstInstallTime;

            int timezone = 8;
            long totalSeconds = time / 1000;
            totalSeconds += 60 * 60 * timezone;
            int second = (int) (totalSeconds % 60);
            long totalMinutes = totalSeconds / 60;
            int minute = (int) (totalMinutes % 60);
            long totalHours = totalMinutes / 60;
            int hour = (int) (totalHours % 24);
            int totalDays = (int) (totalHours / 24);
            int _year = 1970;
            int year = _year + totalDays / 366;
            int month = 1;
            int day = 1;
            int diffDays;
            boolean leapYear;
            String str;
            while (true) {
                int diff = (year - _year) * 365;
                diff += (year - 1) / 4 - (_year - 1) / 4;
                diff -= ((year - 1) / 100 - (_year - 1) / 100);
                diff += (year - 1) / 400 - (_year - 1) / 400;
                diffDays = totalDays - diff;
                leapYear = (year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0);
                if (!leapYear && diffDays < 365 || leapYear && diffDays < 366) {
                    break;
                } else {
                    year++;
                }
            }
            int[] monthDays;
            if (diffDays >= 59 && leapYear) {
                monthDays = new int[]{-1, 0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 385, 335};

            } else {
                monthDays = new int[]{-1, 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};
            }
            for (int i = monthDays.length - 1; i >= 1; i--) {
                if (diffDays >= monthDays[i]) {
                    month = i;
                    day = diffDays - monthDays[i] + 1;
                    break;
                }
            }
            if (hour < 12) {
                str = year + "年" + month + "月" + day + "日" + "上午" + hour + ":" + minute + ":" + second;
            } else if (hour < 18) {
                str = year + "年" + month + "月" + day + "日" + "下午" + hour + ":" + minute + ":" + second;
            } else {
                str = year + "年" + month + "月" + day + "日" + "晚上" + hour + ":" + minute + ":" + second;
            }

            appinfo.installTime = str;

            try {
                packInfo = pm.getPackageInfo(packname, PackageManager.GET_SIGNATURES);
                byte[] b = packInfo.signatures[0].toByteArray();
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                X509Certificate cert = (X509Certificate) cf.generateCertificate(
                        new ByteArrayInputStream(b));
                appinfo.certificateIssuer = cert.getIssuerDN().toString() + "\n";
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            }

            try {
                packInfo = pm.getPackageInfo(packname, PackageManager.GET_PERMISSIONS);
                if (packInfo.requestedPermissions != null) {
                    for (String pio : packInfo.requestedPermissions) {
                        appinfo.appPermissions = appinfo.appPermissions + pio + "\n";
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            appinfos.add(appinfo);
            appinfo = null;
        }

        return appinfos;
    }
}