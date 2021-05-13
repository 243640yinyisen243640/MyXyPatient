package com.vice.bloodpressure.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.vice.bloodpressure.constant.ConstantParam;

import java.util.Map;

public class SharedPreferencesUtilsApp {
    /**
     * 获取SharedPreferences
     *
     * @param context
     * @return
     */
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(ConstantParam.APK_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 获取Editor
     *
     * @param context
     * @return
     */
    private static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    public static void saveInfo(Context context, String key, String value) {
        SharedPreferences.Editor edit = getEditor(context);
        edit.putString(key, value);
        edit.commit();
    }

    /**
     * 保存信息
     */
    public static void saveInfo(Context context, Map<String, String> map) {
        SharedPreferences.Editor edit = getEditor(context);
        for (Map.Entry<String, String> entry : map.entrySet()) {

            edit.putString(entry.getKey(), entry.getValue());
        }
        edit.commit();
    }

    public static String getInfo(Context context, String key) {
        return getInfo(context, key, "");
    }

    public static String getInfo(Context context, String key, String defaultValue) {
        SharedPreferences sp = getSharedPreferences(context);
        String value = sp.getString(key, "");
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        return value;
    }

    /**
     * 获取信息
     *
     * @param context
     * @param map
     */
    public static Map<String, String> getInfo(Context context, Map<String, String> map) {
        SharedPreferences sp = getSharedPreferences(context);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String value = sp.getString(entry.getKey(), "");
            if (!TextUtils.isEmpty(value)) {
                map.put(entry.getKey(), value);
            }
        }
        return map;
    }
}
