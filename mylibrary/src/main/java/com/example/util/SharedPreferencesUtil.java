package com.example.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.example.mylibrary2.R;


public class SharedPreferencesUtil {
	

	public static void clearCouponActivityPicture(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(
				mContext.getString(R.string.my_app_name), 0);
		Editor editor = preferences.edit();
		editor.putString("CouponActivityPicture", "");
		editor.commit();
	}
	

	
	public static SharedPreferences initSharedPreferences(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				mContext.getString(R.string.my_app_name), 0);
		return preferences;
	}

	public static int getInt(Context mContext, int nResID, int nDefault) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				mContext.getString(R.string.my_app_name), 0);
		int result = preferences.getInt(mContext.getString(nResID), nDefault);
		return result;
	}

	public static int getInt(Context mContext, int nResID) {
		return getInt(mContext, nResID, 0);
	}

	public static boolean putString(Context mContext, int nResID,
                                    String strVaule) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				mContext.getString(R.string.my_app_name), 0);

		return preferences.edit()
				.putString(mContext.getString(nResID), strVaule).commit();

	}
	public static boolean putString(Context mContext,String key,
                                    String strVaule) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				mContext.getString(R.string.my_app_name), 0);

		return preferences.edit()
				.putString(key, strVaule).commit();

	}

	public static boolean putInt(Context mContext, int nResID, int nVaule) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				mContext.getString(R.string.my_app_name), 0);
		return preferences.edit().putInt(mContext.getString(nResID), nVaule)
				.commit();
	}
	
	public static boolean putBoolean(Context mContext, int nResID, boolean nVaule) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				mContext.getString(R.string.my_app_name), 0);
		return preferences.edit().putBoolean(mContext.getString(nResID), nVaule)
				.commit();
	}
	
	public static Boolean getBoolean(Context mContext, int nResID) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				mContext.getString(R.string.my_app_name), 0);
		Boolean result = preferences.getBoolean(mContext.getString(nResID),false);
		return result;

	}

	public static String getString(Context mContext, int nResID) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				mContext.getString(R.string.my_app_name), 0);
		String result = preferences.getString(mContext.getString(nResID), "");
		return result;
	}
	public static String getString(Context mContext, String key) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				mContext.getString(R.string.my_app_name), 0);
		String result = preferences.getString(key, "");
		return result;
	}

	public static boolean putLong(Context mContext, int nResID, long nVaule) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				mContext.getString(R.string.my_app_name), 0);
		return preferences.edit().putLong(mContext.getString(nResID), nVaule)
				.commit();
	}
	
	public static long getLong(Context mContext, int nResID) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				mContext.getString(R.string.my_app_name), 0);
		long result = preferences.getLong(mContext.getString(nResID), 0);
		return result;
	}

	/**
	 * 清除资料
	 * 
	 * @param mContext
	 */
	public static void cleanPreferences(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				mContext.getString(R.string.my_app_name), 0);
		preferences.edit().clear().commit();
	}

	public static String getVersionName(Context mContext) {
		// 获取packagemanager的实例
		PackageManager packageManager = mContext.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		String version = "";
		try {
			packInfo = packageManager.getPackageInfo(mContext.getPackageName(),
					0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}
}
