package com.vice.bloodpressure.ui.activity.injection;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

    private TextView tvBleTips;
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
            tvBleTips  = view.findViewById(R.id.tv_device_add_ble_tip);
            textView.setOnClickListener(v -> {
                if (initBlueBooth()) {
                    Intent intent = new Intent(getPageContext(), InjectionProgramSearchDeviceActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        return view;
    }

    private BluetoothAdapter mAdapter;
    private boolean initBlueBooth() {
        if (mAdapter == null) {
            mAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if (mAdapter == null) {
            Toast.makeText(this, "当前设备不支持蓝牙功能", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!checkBle()) {
            Toast.makeText(this, "当前设备不支持ble蓝牙功能", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!mAdapter.isEnabled()) {
            //没有在开启中也没有打开
            if (mAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                tvBleTips.setVisibility(View.VISIBLE);
//                Toast.makeText(this, "蓝牙未开启", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private boolean checkBle() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return false;
        }

        return true;
    }
}
