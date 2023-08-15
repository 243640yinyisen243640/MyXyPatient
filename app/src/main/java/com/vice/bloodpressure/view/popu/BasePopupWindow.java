package com.vice.bloodpressure.view.popu;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.core.content.ContextCompat;

import com.vice.bloodpressure.R;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class BasePopupWindow extends PopupWindow {
    private LinearLayout linearLayout;
    private Context context;

    public BasePopupWindow(Context context) {
        super(context);
        this.context = context;

        LinearLayout view = (LinearLayout) View.inflate(context, R.layout.pop_base, null);
        linearLayout = view.findViewById(R.id.ll_base_pop);
        onCreateContentView();
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
        //         设置SelectPicPopupWindow弹出窗体动画效果
        //        this.setAnimationStyle(R.style.pop_anim_style);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(ContextCompat.getColor(context, R.color.transparent));
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //因为某些机型是虚拟按键的,所以要加上以下设置防止挡住按键.
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        view.setOnClickListener(v -> {
            dismiss();
        });
    }

    protected void setPopupGravity(int gravity) {
        linearLayout.setGravity(gravity);
    }

    public View onCreateContentView() {
        return createPopupById(R.layout.dialog_advertisement);
    }

    protected View createPopupById(int layoutId) {
        ViewGroup view = (ViewGroup) View.inflate(context, layoutId, null);
        linearLayout.addView(view);
        return linearLayout;
    }

    public <T extends View> T findViewById(int id) {
        return (T) linearLayout.findViewById(id);
    }
}
