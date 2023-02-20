package com.vice.bloodpressure.view.popu;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.vice.bloodpressure.R;

import razerdp.basepopup.BasePopupWindow;

public class XueTangFailedPopup extends BasePopupWindow {

    public XueTangFailedPopup(Context context) {
        super(context);
        setPopupGravity(Gravity.CENTER);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_xueya_fail);
    }
}
