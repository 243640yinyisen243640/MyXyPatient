package com.vice.bloodpressure.view.popu;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.vice.bloodpressure.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * 描述:  弹出广告
 * 作者: YYS
 * 创建日期: 2019/10/9 16:31
 */
public class AdvertisementPop extends BasePopupWindow {
    public AdvertisementPop(Context context) {
        super(context);
        setPopupGravity(Gravity.CENTER);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.dialog_advertisement);
    }
}
