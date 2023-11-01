package com.vice.bloodpressure.ui.activity.injection;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.permissions.RxPermissions;
import com.lyd.baselib.utils.eventbus.EventBusUtils;
import com.quinovaresdk.bletransfer.BleTransfer;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.event.BlueConnectEvent;
import com.vice.bloodpressure.utils.SPUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:搜索设备
 */
public class InjectionProgramSearchDeviceActivity extends XYSoftUIBaseActivity {
    private ImageView ivGif;
    private BluetoothAdapter mAdapter;
    private BluetoothLeScanner bluetoothLeScanner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().titleTextView().setText("添加设备");
        containerView().addView(initView());
        EventBusUtils.register(this);
        initBlueBooth();
        initPermission();
    }

    private View initView() {
        View view = View.inflate(getPageContext(), R.layout._activity_device_search, null);
        ivGif = view.findViewById(R.id.iv_search_device_gif);
        Glide.with(getPageContext()).asGif().load(R.drawable.injection_device_search).into(ivGif);
        return view;
    }

    @SuppressLint("CheckResult")
    private void initPermission() {
        //申请位置权限  扫描蓝牙需要
        final RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        // 用户允许权限开始扫描
                        startScan();
                    }
                });
    }

    boolean scanSuccess = false;

    private void startScan() {
        Log.i("yys", "开始扫描");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showPop();
            }
        }, 15_000);
        bluetoothLeScanner = mAdapter.getBluetoothLeScanner();
        bluetoothLeScanner.startScan(scanCallback);
    }


    private void showPop() {
        if (!scanSuccess) {
            //没有扫描到设备
            bluetoothLeScanner.stopScan(scanCallback);

            //弹出弹窗
            //再次扫描
            scanSuccess = false;
            startScan();
        }
    }

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            if (result != null) {
                Log.i("yys", "onScanResult: ");
                if (!TextUtils.isEmpty(result.getDevice().getName())) {
                    Log.i("yys", "onScanResult: " + result.getDevice().getName());
                    if (result.getDevice().getName().trim().equals("QLINK2")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //扫描到了
                                scanSuccess = true;
                                bluetoothLeScanner.stopScan(scanCallback);

                                //连接蓝牙
                                String deviceAddress = result.getDevice().getAddress();
                                Log.i("yys", "deviceAddress==" + deviceAddress);
                                if (TextUtils.isEmpty(deviceAddress)) {
                                    return;
                                }
                                SPUtils.putBean("BlueDeviceMac",deviceAddress);
                                BleTransfer.getInstance().realConnect(deviceAddress);
                            }
                        });
                    }
                }
            }
        }
    };

    private void initBlueBooth() {
        if (mAdapter == null) {
            this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if (mAdapter == null) {
            Toast.makeText(this, "当前设备不支持蓝牙功能", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!checkBle()) {
            Toast.makeText(this, "当前设备不支持ble蓝牙功能", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!mAdapter.isEnabled()) {
            //没有在开启中也没有打开
            if (mAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                //蓝牙被关闭时强制打开
                mAdapter.enable();
            }
        }
    }

    private boolean checkBle() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mAdapter == null) {
            return false;
        }

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return false;
        }
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BlueConnectEvent event) {
        Log.i("yys", "onMessageEvent: ");
        boolean bind = event.isConnect();
        if (bind){
            startActivity(new Intent(getPageContext(),InjectionProgramBindDeviceActivity.class));
            BleTransfer.getInstance().bindDevice();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }
}
