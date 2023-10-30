package com.vice.bloodpressure.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

/**
 * Author: LYD
 * Date: 2021/8/10 18:12
 * Description:
 */
public class StatusBarUtils {
    /**
     * 全屏及状态栏设置
     * 注意：
     * 一、该方法仅支持6.0及以上版本
     * 二、该方法设置后，布局占用状态栏高度,
     *
     * @param activity
     * @param colorResID 状态栏高度
     * @param isWhite    状态栏字体是否白色
     * @return
     */
    public static boolean fullScreenWithStatusBarColor(Activity activity, int colorResID, boolean isWhite) {
        if (Build.VERSION.SDK_INT >= 23) {
            Window window = activity.getWindow();
            window.addFlags(-2147483648);
            window.clearFlags(67108864);
            window.setStatusBarColor(ContextCompat.getColor(activity, colorResID));
            View decorView = window.getDecorView();
            short option;
            if (isWhite) {
                option = 1280;
                decorView.setSystemUiVisibility(option);
            } else {
                option = 9472;
                decorView.setSystemUiVisibility(option);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 改变状态栏的背景颜色以及根据颜色值改变状态栏的字体及图标
     * 注意：
     * 一、该方法仅支持6.0及以上版本
     * 二、该方法布局不占用状态来区域，与getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);不同时使用
     *
     * @param activity
     * @param color
     */
    public static void statusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = activity.getWindow();
            // 设置状态栏底色颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
            // 如果亮色，设置状态栏文字为黑色
            View decorView = window.getDecorView();
            if (isLightColor(color)) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }
    /**
     * 判断颜色是不是亮色
     *
     * @param color
     * @return
     */
    public static boolean isLightColor(int color) {
        double calculate = ColorUtils.calculateLuminance(color);
        return calculate >= 0.5 || calculate == 0.0;
    }


}
