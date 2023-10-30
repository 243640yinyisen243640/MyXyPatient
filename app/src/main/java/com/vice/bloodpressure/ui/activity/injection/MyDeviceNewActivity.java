package com.vice.bloodpressure.ui.activity.injection;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class MyDeviceNewActivity extends XYSoftUIBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        containerView().addView(initView());
    }

    private View initView() {

        return null;
    }
}
