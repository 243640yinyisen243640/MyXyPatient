package com.vice.bloodpressure.view.popu;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.vice.bloodpressure.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * 描述:  更新弹窗
 * 作者: LYD
 * 创建日期: 2019/10/9 16:31
 */
public class OutLoginPopup extends BasePopupWindow {
    public OutLoginPopup(Context context) {
        super(context);
        setPopupGravity(Gravity.CENTER);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.dialog_oper);
    }
}
