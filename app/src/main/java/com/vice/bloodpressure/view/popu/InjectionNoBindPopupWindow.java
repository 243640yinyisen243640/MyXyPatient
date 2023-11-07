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

public class InjectionNoBindPopupWindow extends PopupWindow {
    private TextView titleTv;
    private TextView contentTv;
    private TextView backTv;
    private TextView nextTv;


    public InjectionNoBindPopupWindow(Context context, View.OnClickListener backClickListener, View.OnClickListener nextClickListener) {
        LinearLayout view = (LinearLayout) View.inflate(context, R.layout.pop_device_no_bind, null);
        titleTv = view.findViewById(R.id.tv_device_no_bind_title);
        contentTv = view.findViewById(R.id.tv_device_no_bind_content);
        backTv = view.findViewById(R.id.tv_device_no_bind_back);
        nextTv = view.findViewById(R.id.tv_device_no_bind_next);

        backTv.setOnClickListener(v -> {
            if (backClickListener != null) {
                backClickListener.onClick(v);
            }
        });
        backTv.setOnClickListener(v -> {
            if (nextClickListener != null) {
                nextClickListener.onClick(v);
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

    public TextView showContent() {
        return contentTv;
    }
}
