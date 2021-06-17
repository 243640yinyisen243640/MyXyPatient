package com.vice.bloodpressure.view.popu;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.vice.bloodpressure.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * 描述:  显示温度弹窗
 * 作者: LYD
 * 创建日期: 2019/10/9 16:31
 */
public class ShowTemperaturePopup extends BasePopupWindow {
    public ShowTemperaturePopup(Context context) {
        super(context);
        setPopupGravity(Gravity.CENTER);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.show_temperature);
    }
}
