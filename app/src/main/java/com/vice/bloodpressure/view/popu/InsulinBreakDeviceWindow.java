package com.vice.bloodpressure.view.popu;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.utils.MySPUtils;

public class InsulinBreakDeviceWindow extends PopupWindow {

    public InsulinBreakDeviceWindow(Context context, View.OnClickListener selfClickListener) {
        LinearLayout view = (LinearLayout) View.inflate(context, R.layout.pop_insulin_break_device, null);
        TextView numTextView = view.findViewById(R.id.tv_insulin_break_device_num);
        TextView tvCancle = view.findViewById(R.id.tv_insulin_break_device_cancle);
        TextView tvSure = view.findViewById(R.id.tv_insulin_break_device_sure);
        numTextView.setText(MySPUtils.getString(context, MySPUtils.DEVICE_NAME));
        tvCancle.setOnClickListener(v -> {
            dismiss();
        });
        tvSure.setOnClickListener(v -> {
            if (selfClickListener != null) {
                selfClickListener.onClick(v);
            }
        });
        //解决PopupWindow无法覆盖状态栏
        this.setClippingEnabled(false);
        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(FrameLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //        this.setAnimationStyle(R.style.HuaHanSoft_Window_Fade_Anim);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(ContextCompat.getColor(context, R.color.transparent));
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //因为某些机型是虚拟按键的,所以要加上以下设置防止挡住按键.
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        view.setOnClickListener(v -> dismiss());
    }
}
