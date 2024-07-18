package com.vice.bloodpressure.ui.activity.injection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.ui.activity.insulin.InsulinChooseDeviceActivity;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class InjectionAddDeviceNoActivity extends XYSoftUIBaseActivity {

    private TextView tvSure;

    private TextView tipsTv;


    /**
     * 1:快舒尔
     * 2：胰岛素泵
     */
    private String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getStringExtra("type");
        if (type.equals("1")){
            topViewManager().titleTextView().setText("注射设备");
        }else {
            topViewManager().titleTextView().setText("胰岛素泵");
        }
        containerView().addView(initView());
    }

    private View initView() {
        View view = View.inflate(getPageContext(), R.layout.avtivity_device_add_no, null);
        tvSure = view.findViewById(R.id.tv_device_add_sure);
        tipsTv = view.findViewById(R.id.tips_tv);
       if (type.equals("1")){
           tipsTv.setText("暂无注射设备");
       }else {
           tipsTv.setText("暂无胰岛素泵设备");
       }
        tvSure.setOnClickListener(v -> {
            if ("1".equals(type)){
                startActivity(new Intent(getPageContext(), InjectionProgramAddDeviceActivity.class));
                finish();
            }else {
                startActivity(new Intent(getPageContext(), InsulinChooseDeviceActivity.class));
                finish();
            }

        });
        return view;

    }
}
