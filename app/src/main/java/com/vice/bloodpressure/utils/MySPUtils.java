package com.vice.bloodpressure.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class MySPUtils {
    private static final String SP_NAME = "sp_name_blue";
    //两个蓝牙设备的sp
    public static final String BLUE_MAC = "blue_mac";
    //1凯联  2迈士通
    public static final String BLUE_TYPE = "blue_type";
    //序列号  设备名称
    public static final String DEVICE_NAME = "device_name";
    //设备编号  输入  用于迈士通蓝牙获取发送数据
    public static final String DEVICE_NUM = "device_num";


    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getSharedPreferencesEditor(Context context) {
        SharedPreferences.Editor edit = getSharedPreferences(context).edit();
        return edit;
    }


    public static String getString(Context context, String key) {
        return getSharedPreferences(context).getString(key, "");
    }

    public static void putString(Context context, String key, String value) {
        getSharedPreferencesEditor(context).putString(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean value) {
        return getSharedPreferences(context).getBoolean(key, value);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        getSharedPreferencesEditor(context).putBoolean(key, value).commit();
    }
}
