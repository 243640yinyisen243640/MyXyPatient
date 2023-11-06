package com.vice.bloodpressure.ui.activity.injection;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
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
    private static final int LOCATION_PERMISSIONS_REQUEST_CODE = 10;
    private static final int REQUEST_ENABLE_BT = 11;
    private ImageView ivGif;
    private TextView  tvNoSearch;
    private BluetoothAdapter mAdapter;
    private BluetoothLeScanner bluetoothLeScanner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().titleTextView().setText("添加设备");
        containerView().addView(initView());
        // 注册BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothStateReceiver, filter);
        EventBusUtils.register(this);
    }

    private View initView() {
        View view = View.inflate(getPageContext(), R.layout._activity_device_search, null);
        ivGif = view.findViewById(R.id.iv_search_device_gif);
        tvNoSearch = view.findViewById(R.id.tv_search_device_no_tips);
        tvNoSearch.setOnClickListener(v -> {
            tvNoSearch.setVisibility(View.GONE);
            startScan();
        });
        ivGif.setVisibility(View.GONE);
        Glide.with(getPageContext()).asGif().load(R.drawable.injection_device_search).into(ivGif);
        return view;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPermission();
    }

    @SuppressLint("CheckResult")
    private void initPermission() {
        if (!blueIsOn) {
            ToastUtils.showShort("请打开蓝牙再试");
            return;
        }
        if (initBlueBooth()) {
            //申请位置权限  扫描蓝牙需要
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSIONS_REQUEST_CODE);
            } else {
                //有权限
                startScan();
            }
        }
    }

    boolean scanSuccess = false;

    private void startScan() {
        Log.i("yys", "开始扫描");
        if (mAdapter.isDiscovering()){
            bluetoothLeScanner.stopScan(scanCallback);
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showPop();
            }
        }, 15_000);
        bluetoothLeScanner = mAdapter.getBluetoothLeScanner();
        bluetoothLeScanner.startScan(scanCallback);
        ivGif.setVisibility(View.VISIBLE);
    }


    private void showPop() {
        if (!scanSuccess) {
            //没有扫描到设备
            ivGif.setVisibility(View.GONE);
            bluetoothLeScanner.stopScan(scanCallback);
            tvNoSearch.setVisibility(View.VISIBLE);
            //弹出弹窗
            //再次扫描
            scanSuccess = false;
            initPermission();
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
                                SPUtils.putBean("BlueDeviceMac", deviceAddress);
                                BleTransfer.getInstance().connect(deviceAddress);
                            }
                        });
                    }
                }
            }
        }
    };

    private boolean initBlueBooth() {
        if (mAdapter == null) {
            this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if (mAdapter == null) {
            Toast.makeText(this, "当前设备不支持蓝牙功能", Toast.LENGTH_SHORT).show();
            finish();
            return false;
        }

        if (!checkBle()) {
            Toast.makeText(this, "当前设备不支持ble蓝牙功能", Toast.LENGTH_SHORT).show();
            finish();
            return false;
        }

        if (!mAdapter.isEnabled()) {
            //没有在开启中也没有打开
            if (mAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                //蓝牙被关闭时强制打开
                blueIsOn = false;
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                initPermission();
            } else {
                finish();
            }
        }

    }

    private boolean checkBle() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return false;
        }
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BlueConnectEvent event) {
        Log.i("yys", "onMessageEvent: ");
        boolean bind = event.isConnect();
        if (bind) {
            startActivity(new Intent(getPageContext(), InjectionProgramBindDeviceActivity.class));
            BleTransfer.getInstance().bindDevice();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //调用系统相机申请拍照权限回调
            case LOCATION_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initPermission();
                } else {
                    finish();
                }
                break;
            }
        }
    }

    private boolean blueIsOn = true;

    private final BroadcastReceiver bluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        blueIsOn = false;
                        // 蓝牙已关闭
                        // 在这里可以对蓝牙关闭时的情况进行处理
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        // 蓝牙正在关闭
                        // 在这里可以对蓝牙正在关闭的情况进行处理
                        break;
                    case BluetoothAdapter.STATE_ON:
                        blueIsOn = true;
                        // 蓝牙已打开
                        // 在这里可以对蓝牙打开时的情况进行处理
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        // 蓝牙正在打开
                        // 在这里可以对蓝牙正在打开的情况进行处理
                        break;
                    default:
                        break;
                }
            }
        }
    };
}
