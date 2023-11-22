package com.vice.bloodpressure.ui.activity.injection;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ToastUtils;
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
    private final int BLUETOOTH_PERMISSIONS_REQUEST_CODE = 10;

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
            tvBleTips = view.findViewById(R.id.tv_device_add_ble_tip);
            textView.setOnClickListener(v -> {
                //这个走了，因为我关掉蓝牙，他提示我开了
                if (initBlueBooth()) {
                    //Android12以上获取权限
                    if (Build.VERSION.SDK_INT > 30) {
                        Log.i("yys", "Build.VERSION.SDK_INT==" + Build.VERSION.SDK_INT);
                        if (ContextCompat.checkSelfPermission(this,
                                "android.permission.BLUETOOTH_SCAN")
                                != PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(this,
                                "android.permission.BLUETOOTH_ADVERTISE")
                                != PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(this,
                                "android.permission.BLUETOOTH_CONNECT")
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, new String[]{
                                    "android.permission.BLUETOOTH_SCAN",
                                    "android.permission.BLUETOOTH_ADVERTISE",
                                    "android.permission.BLUETOOTH_CONNECT"}, BLUETOOTH_PERMISSIONS_REQUEST_CODE);
                        } else {
                            Log.i("yys", "有权限");
                            Intent intent = new Intent(getPageContext(), InjectionProgramSearchDeviceActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Intent intent = new Intent(getPageContext(), InjectionProgramSearchDeviceActivity.class);
                        startActivity(intent);
                        finish();
                    }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //调用系统相机申请拍照权限回调
            case BLUETOOTH_PERMISSIONS_REQUEST_CODE:
                Log.i("yys", "grantResults.length=="+grantResults.length);
                Log.i("yys", "grantResults[0]=="+grantResults[0]);
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(getPageContext(), InjectionProgramSearchDeviceActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    ToastUtils.showShort("请先开启权限");
                }
                break;
            default:
                break;
        }
    }
}
