package com.vice.bloodpressure.ui.activity.healthrecordadd;

import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.BaseHandlerActivity;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class BloodSugarAddUnNormalActivity extends BaseHandlerActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("预警提示");
    }

    @Override
    protected View addContentLayout() {
        return getLayoutInflater().inflate(R.layout.popup_center, contentLayout, false);


    }

    @Override
    public void processHandlerMsg(Message msg) {

    }
}
