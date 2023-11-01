package com.vice.bloodpressure.ui.activity.injection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:添加设备
 */
public class InjectionProgramAddDeviceActivity extends XYSoftUIBaseActivity {
    //第几步
    private int step;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().titleTextView().setText("添加设备");
        containerView().addView(initView());
    }

    private View initView() {
        View view = null;
        step = getIntent().getIntExtra("step", 0);
        if (step == 0) {
            view = View.inflate(getPageContext(), R.layout._activity_device_add, null);
            TextView textView = view.findViewById(R.id.tv_add_device);
            textView.setOnClickListener(v -> {
                Intent intent = new Intent(getPageContext(), InjectionProgramAddDeviceActivity.class);
                this.step++;
                intent.putExtra("step", step);
                startActivity(intent);
                finish();
            });
        } else if (step == 1) {
            view = View.inflate(getPageContext(), R.layout._activity_device_add2, null);
            TextView textView = view.findViewById(R.id.tv_add_device);
            textView.setOnClickListener(v -> {
                Intent intent = new Intent(getPageContext(), InjectionProgramAddDeviceActivity.class);
                this.step++;
                intent.putExtra("step", step);
                startActivity(intent);
                finish();
            });
        } else if (step == 2) {
            view = View.inflate(getPageContext(), R.layout._activity_device_add3, null);
            TextView textView = view.findViewById(R.id.tv_add_device);
            textView.setOnClickListener(v -> {
                Intent intent = new Intent(getPageContext(), InjectionProgramSearchDeviceActivity.class);
                startActivity(intent);
                finish();
            });
        }
        return view;
    }
}
