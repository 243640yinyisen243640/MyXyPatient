package com.vice.bloodpressure.ui.activity.insulin;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
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
 * 描述:选择设备
 */
public class InsulinChooseDeviceActivity extends XYSoftUIBaseActivity implements View.OnClickListener {
    private final int BLUETOOTH_PERMISSIONS_REQUEST_CODE = 10;
    private FrameLayout flAllFirst;
    private CheckBox cbCheckFirst;
    private TextView tvContentFirst;
    private FrameLayout flAllSecond;
    private CheckBox cbCheckSecond;
    private TextView tvContentSecond;

    private TextView tvNext;
    private TextView bleTips;

    private String isCheck = "2";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().titleTextView().setText("选择品牌");
        containerView().addView(initView());
        initListener();
    }

    private void initListener() {
        flAllFirst.setOnClickListener(this);
        flAllSecond.setOnClickListener(this);
        tvNext.setOnClickListener(this);
    }

    private View initView() {
        View view = View.inflate(getPageContext(), R.layout.activity_insulin_choose_device, null);
        flAllFirst = view.findViewById(R.id.fl_insulin_choose_bg_first);
        cbCheckFirst = view.findViewById(R.id.cb_insulin_choose_check_first);
        tvContentFirst = view.findViewById(R.id.tv_insulin_choose_content_first);
        flAllSecond = view.findViewById(R.id.fl_insulin_choose_bg_second);
        cbCheckSecond = view.findViewById(R.id.cb_insulin_choose_check_second);
        tvContentSecond = view.findViewById(R.id.tv_insulin_choose_content_second);
        bleTips = view.findViewById(R.id.tv_insulin_ble_tips);
        tvNext = view.findViewById(R.id.tv_insulin_choose_next);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_insulin_choose_bg_first:
                isCheck = "2";
                flAllFirst.setBackground(getResources().getDrawable(R.drawable.shape_green_tran_12_5));
                cbCheckFirst.setVisibility(View.VISIBLE);
                tvContentFirst.setTextColor(getResources().getColor(R.color.main_green));

                flAllSecond.setBackground(getResources().getDrawable(R.drawable.shape_grey_f8_5));
                cbCheckSecond.setVisibility(View.GONE);
                tvContentSecond.setTextColor(getResources().getColor(R.color.black_text));
                break;
            case R.id.fl_insulin_choose_bg_second:
                isCheck = "1";
                flAllFirst.setBackground(getResources().getDrawable(R.drawable.shape_grey_f8_5));
                cbCheckFirst.setVisibility(View.GONE);
                tvContentFirst.setTextColor(getResources().getColor(R.color.black_text));

                flAllSecond.setBackground(getResources().getDrawable(R.drawable.shape_green_tran_12_5));
                cbCheckSecond.setVisibility(View.VISIBLE);
                tvContentSecond.setTextColor(getResources().getColor(R.color.main_green));
                break;
            case R.id.tv_insulin_choose_next:
                //                startActivity(new Intent(getPageContext(),InsulinAddDeviceActivity.class));

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
                            Intent intent = new Intent(getPageContext(), InsulinAddDeviceActivity.class);
                            intent.putExtra("keywords", isCheck);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Intent intent = new Intent(getPageContext(), InsulinAddDeviceActivity.class);
                        intent.putExtra("keywords", isCheck);
                        startActivity(intent);
                        finish();
                    }
                }
                break;
            default:
                break;
        }
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
            bleTips.setVisibility(View.VISIBLE);
            if (mAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                Toast.makeText(this, "蓝牙未开启", Toast.LENGTH_SHORT).show();
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
                Log.i("yys", "grantResults.length==" + grantResults.length);
                Log.i("yys", "grantResults[0]==" + grantResults[0]);
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(getPageContext(), InsulinAddDeviceActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //这里是没有开扫描权限
                    bleTips.setVisibility(View.VISIBLE);
                    ToastUtils.showShort("请先开启权限");
                }
                break;
            default:
                break;
        }
    }
}
