package com.vice.bloodpressure.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vice.bloodpressure.R;


/**
 * 描述: 自定义加载进度的Dialog
 * 作者: LYD
 * 创建日期: 2018/6/6 14:40
 */

public class BleProgressDialog extends Dialog {
    private View mDialogView;
    private boolean cancelTouchOutside;

    private BleProgressDialog(Builder builder) {
        super(builder.context);
        //设置对话框边框背景,必须在代码中设置对话框背景，不然对话框背景是黑色的
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(0x00000000);
        getWindow().setBackgroundDrawable(gradientDrawable);
        mDialogView = builder.mDialogView;
        cancelTouchOutside = builder.cancelTouchOutside;
    }

    private BleProgressDialog(Builder builder, int themeResId) {
        super(builder.context, themeResId);
        //设置对话框边框背景,必须在代码中设置对话框背景，不然对话框背景是黑色的
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(0x00000000);
        getWindow().setBackgroundDrawable(gradientDrawable);
        mDialogView = builder.mDialogView;
        cancelTouchOutside = builder.cancelTouchOutside;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mDialogView);
        setCanceledOnTouchOutside(cancelTouchOutside);
    }

    //在Activity启动时会自动运行动画
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (mDialogView == null) {
            return;
        }
        //添加控件
        ImageView imageView = mDialogView.findViewById(R.id.img_loading);
        //执行帧动画
        //ImageView设置background和src的区别
        //https://blog.csdn.net/sdfdzx/article/details/50815358
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        //AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
    }

    @Override
    protected void onStop() {
        //animationDrawable.stop();
        super.onStop();
    }

    public static final class Builder {
        Context context;
        private int resStyle = -1;
        private View mDialogView;
        private boolean cancelTouchOutside;

        public Builder(Context context) {
            this.context = context;
            mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_progress_ble, null);
        }

        /**
         * 设置主题
         *
         * @param resStyle style id
         * @return CustomProgressDialog.Builder
         */
        public Builder setTheme(int resStyle) {
            this.resStyle = resStyle;
            return this;
        }

        public Builder setMessage(String message) {
            TextView tvMessage = mDialogView.findViewById(R.id.tv_loading_msg);
            if (tvMessage != null) {
                tvMessage.setText(message);
            }
            return this;
        }

        /**
         * 设置点击dialog外部是否取消dialog
         *
         * @param val 点击外部是否取消dialog
         * @return
         */
        public Builder cancelTouchOutside(boolean val) {
            cancelTouchOutside = val;
            return this;
        }

        public BleProgressDialog build() {
            if (resStyle != -1) {
                return new BleProgressDialog(this, resStyle);
            } else {
                return new BleProgressDialog(this);
            }
        }
    }
}
