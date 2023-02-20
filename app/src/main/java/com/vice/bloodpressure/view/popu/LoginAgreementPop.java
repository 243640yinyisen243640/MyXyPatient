package com.vice.bloodpressure.view.popu;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.vice.bloodpressure.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * 描述:  登录弹出隐私政策
 * 作者: YYS
 * 创建日期: 2019/10/9 16:31
 */
public class LoginAgreementPop extends BasePopupWindow {
    public LoginAgreementPop(Context context) {
        super(context);
        setPopupGravity(Gravity.CENTER);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_login_agreement);
    }
}
