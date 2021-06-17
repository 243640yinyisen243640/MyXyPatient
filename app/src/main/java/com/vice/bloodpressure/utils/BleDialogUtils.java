package com.vice.bloodpressure.utils;

import android.app.Activity;

import com.vice.bloodpressure.R;


/**
 * 描述: Ble加载
 * 作者: LYD
 * 创建日期: 2018/6/6 14:51
 */

public class BleDialogUtils {
    //加载进度的dialog
    public BleProgressDialog mProgressDialog;

    /**
     * 显示ProgressDialog
     */
    public void showProgress(Activity activity, String msg) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        if (mProgressDialog == null) {
            mProgressDialog = new BleProgressDialog.Builder(activity)
                    .setTheme(R.style.BleProgressDialogStyle)
                    .setMessage(msg)
                    .build();
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    /**
     * 显示ProgressDialog
     */
    public void showProgress(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        if (mProgressDialog == null) {
            mProgressDialog = new BleProgressDialog.Builder(activity)
                    .setTheme(R.style.BleProgressDialogStyle)
                    .build();
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    /**
     * 取消ProgressDialog
     */
    public void dismissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
