package com.foxconn.rfid.theowner.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;


public class ToolsUtils {


	public static int getAppVersionCode(Context context) {
		// 获取packagemanager的实
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名代表是获取版本信
		try {
			PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			int version = packInfo.versionCode;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 1;
		}
	}

	public static String getAppVersionName(Context context) {
		// 获取packagemanager的实
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名代表是获取版本信
		try {
			PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			String version = packInfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "1.0";
		}
	}


	//获取apk大小
	public static String getAppSize(Context context) {
		String apkSize = "";
		PackageManager packageManager = context.getPackageManager();
		List<ApplicationInfo> installList=packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (int i = 0; i < installList.size(); i++) {
			ApplicationInfo info=installList.get(i);
			try {
				if (info.packageName.equals(packageManager.getPackageInfo(context.getPackageName(), 0).packageName)) {//如果这个packagename和当前apk的packagename一样，你就可以获取它的大小了。
					String dir = info.publicSourceDir;
					long size = Long.valueOf((long) new File(dir).length());
					apkSize = parseApkSize(size);
				}
			}catch (NameNotFoundException e) {
				e.printStackTrace();
				return "12.3M";
			}
		}
		return apkSize;
	}


	// 解析apk大小（保留两位小数）
	public static String parseApkSize(long bytes) {
		BigDecimal filesize = new BigDecimal(bytes);
		BigDecimal megabyte = new BigDecimal(1024 * 1024);
		double returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
				.doubleValue();
		if (returnValue > 1)
			return (returnValue + "MB");
		BigDecimal kilobyte = new BigDecimal(1024);
		returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
				.doubleValue();
		return (returnValue + "KB");
	}



}
