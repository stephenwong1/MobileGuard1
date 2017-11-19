package cn.edu.gdmec.android.mobileguard.m4appmanager.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import java.util.Arrays;

import cn.edu.gdmec.android.mobileguard.m4appmanager.entity.AppInfo;

/**
 * Created by hitma on 2017/11/12.
 */

public class EngineUtils {

    public static void shareApplication(Context context , AppInfo appInfo){
        Intent intent = new Intent("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,
                "推荐您使用一款软件，名称叫:"+ appInfo.appName
                       + "下载路径:https://play.google.com/store/apps/details?id="
                       + appInfo.packageName);
        context.startActivity(intent);
    }

    public static  void startApplication(Context context,AppInfo appInfo){

        PackageManager pm = context .getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(appInfo.packageName);
        if (intent != null){
            context.startActivity(intent);
        }else{
            Toast.makeText(context,"应该用没有启动界面",Toast.LENGTH_SHORT).show();
        }
    }

    public static void SettingAppDetail(Context context,AppInfo appInfo){
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:"+ appInfo.packageName));
        context.startActivity(intent);
    }

    public  static  void uninstallApplication(Context context,AppInfo appInfo){
        if (appInfo.isUerApp){
            Intent intent  = new Intent();
            intent.setAction(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + appInfo.packageName));
            context.startActivity(intent);
        }else{
            Toast.makeText(context,"系统应用无法卸载",Toast.LENGTH_LONG).show();
        }
    }

    public static  void showAboutDialog(Context context,AppInfo appInfo) {
        AlertDialog.Builder bulider = new AlertDialog.Builder(context);
        bulider.setTitle(appInfo.appName);
        bulider.setMessage("verSion:" + appInfo.appVersion + "\nInstall time:" + appInfo.installTime
                + "\nCertificate issuer:" + appInfo.certificateIssuer
                + "\nPermissions:" + appInfo.appPermissions);

        bulider.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        break;
                }
            }
        });


        AlertDialog dialog = bulider.create();
        dialog.show();
    }

    public static  void showActivityDialog(Context context,AppInfo appInfo){
        try {
            PackageManager packageManager = context . getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(appInfo.packageName,PackageManager.GET_ACTIVITIES);

            AlertDialog builder = new AlertDialog.Builder(context).setTitle(appInfo.appName).
                    setMessage(appInfo.appName+"\n"+"Activities:"+"\n"+ Arrays.toString(packageInfo.activities))
                    .setNegativeButton("返回",new DialogInterface.OnClickListener(){

                        public void onClick(DialogInterface dialog , int which){

                        }
                    }).show();

        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
    }
}

