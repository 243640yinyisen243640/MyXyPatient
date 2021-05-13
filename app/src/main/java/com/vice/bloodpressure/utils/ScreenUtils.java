package com.vice.bloodpressure.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenUtils {
    /**
     * 获取屏幕的宽（单位px）
     *
     * @param context
     * @return
     */
    public static int screenWidth(Context context) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
}
