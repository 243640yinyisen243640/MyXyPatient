package com.vice.bloodpressure.ui.activity.injection;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.permissions.RxPermissions;
import com.quinovaresdk.bletransfer.BleTransfer;
import com.quinovaresdk.bletransfer.Injection;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;

import java.util.ArrayList;

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
        containerView().addView(initView());
        initBlueBooth();
        String ally_key="df44d3a6fc6a57e8f4b1d3b6f8e5b290";
        BleTransfer.getInstance().init(getPageContext(),ally_key);
        BleTransfer.getInstance().realInit();
        blueCallBack();
        initPermission();
        //解绑设备
//        BleTransfer.getInstance().unBindDevice();
        //取消连接
//        BleTransfer.getInstance().disconnect();
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

    private void startScan() {
        Log.i("yys", "开始扫描");
        bluetoothLeScanner = mAdapter.getBluetoothLeScanner();
        bluetoothLeScanner.startScan(scanCallback);
    }

    //没有扫描到设备
    private void showPop() {

    }

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            if (result != null) {
                Log.i("yys", "onScanResult: ");
                if (!TextUtils.isEmpty(result.getDevice().getName())) {
                    Log.i("yys", "onScanResult: "+result.getDevice().getName());
                    if (result.getDevice().getName().trim().equals("QLINK2")){
                        //扫描到了
                        bluetoothLeScanner.stopScan(scanCallback);

                        //连接蓝牙
                        String deviceAddress = result.getDevice().getAddress();
                        Log.i("yys", "deviceAddress=="+deviceAddress);
                        if (TextUtils.isEmpty(deviceAddress)) {
                            return;
                        }

                        boolean connect = BleTransfer.getInstance().realConnect(deviceAddress);
                        Log.i("yys", "connect=="+connect);
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

    private void blueCallBack() {
        BleTransfer.getInstance().setOnDataCallBackListetner(new BleTransfer.OnDataCallListener() {
            @Override
            public void onSdkAuthcation(final int code, final String message) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        //SDK鉴权
                    }
                });
            }

            @Override
            public void onDeviceAuthcation(final int code, final String message) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        //SDK鉴权
                    }
                });
            }

            @Override
            public void onDeviceConnected() {
                Log.i("yys", "链接成功");
                runOnUiThread(new Runnable() {
                    public void run() {
                        //连接成功
                        //绑定设备
                        BleTransfer.getInstance().bindDevice();
                    }
                });


            }

            @Override
            public void onDeviceDisConnected() {
                Log.i("yys", "链接失败");
                runOnUiThread(new Runnable() {
                    public void run() {
                        //取消连接成功

                    }
                });


            }

            @Override
            public void onServicesDisCovered() {

            }

            @Override
            public void onDeviceNotSupport() {
                BleTransfer.getInstance().disconnect();
            }

            @Override
            public void onBindDeviceStatus(final String code, final String message) {
                Log.i("yys", "绑定状态=="+code + "," + message);
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.i("yys", "绑定状态=="+code + "," + message);
                        if ("00".equals(code)){
                            //绑定成功

                        }else {

                        }
                        ToastUtils.showShort(message);
                    }
                });


            }

            @Override
            public void onUnBindDeviceStatus(final String code, final String message) {

                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.i("yys", "解绑状态=="+code + "," + message);
                    }
                });

            }

            @Override
            public void onSetBuzzerParamStatus(final String code, final String message) {
                runOnUiThread(new Runnable() {
                    public void run() {
//                        tv_sendButton003.setText(code + "," + message);
                    }
                });

            }

            @Override
            public void onSetMedicinalType(final String code, final String message) {
                runOnUiThread(new Runnable() {
                    public void run() {
//                        tv_sendButton004.setText(code + "," + message);
                    }
                });

            }

            @Override
            public void onGetPressedStatus(final String code, final String message) {
                runOnUiThread(new Runnable() {
                    public void run() {
//                        tv_sendButton01.setText(code + "," + message);
                    }
                });
            }

            @Override
            public void onBatteryVolStatus(final byte vol) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        //获取设备电量
                    }
                });
            }

            @Override
            public void onGetBuzzerParamStatus(final byte kaiguan, final byte jiange) {
                runOnUiThread(new Runnable() {
                    public void run() {
//                        tv_sendButton03.setText(kaiguan + "," + jiange);
                    }
                });
            }

            @Override
            public void onGetDeviceInfo(final String vender, final String hardware, final float ver) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        //获取设备信息

                    }
                });
            }

            @Override
            public void onProductInfo(final byte vol, final short productType, final String productName) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        //获取产品信息
//                        tv_sendButton02_1.setText(vol + "," + productType + "," + productName);
                    }
                });

            }

            @Override
            public void onGetDeviceTime(final short year, final byte month, final byte day, final byte hour, final byte minute, final byte second) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        //获取设备本地时间
//                        tv_sendButton05.setText(year + "年" + month + "月" + day + "日"
                        //                        + hour + "时" + minute + "分" + second + "秒");
                    }
                });
            }

            @Override
            public void onSetDeviceTime() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        //设置设备本地时间
//                        tv_sendButton06.setText("成功");
                    }
                });
            }

            @Override
            public void onHistoryInjection(final ArrayList<Injection> datas) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        //获取历史注射记录
                        StringBuffer stringBuffer = new StringBuffer();
                        for (int i = 0; i < datas.size(); i++) {
                            Injection injection = datas.get(i);
                            String text = injection.getYear() + ","
                                    + injection.getMonth() + ","
                                    + injection.getDay() + ","
                                    + injection.getHour() + ","
                                    + injection.getMinute() + ","
                                    + injection.getSecond() + ","
                                    + injection.getDosage() + ","
                                    + injection.getType();

                            stringBuffer.append(text + "\n");
                        }
//                        tv_sendButton08.setText("" + stringBuffer.toString());
                    }
                });
            }


            @Override
            public void onInjection(final Injection injection) {

                runOnUiThread(new Runnable() {
                    public void run() {
                        //注射记录通知
                        String text = injection.getYear() + ","
                                + injection.getMonth() + ","
                                + injection.getDay() + ","
                                + injection.getHour() + ","
                                + injection.getMinute() + ","
                                + injection.getSecond() + ","
                                + injection.getDosage() + ","
                                + injection.getType();
//                        tv_nofity_result.setText(text);
                    }
                });

            }
        });
    }
}
