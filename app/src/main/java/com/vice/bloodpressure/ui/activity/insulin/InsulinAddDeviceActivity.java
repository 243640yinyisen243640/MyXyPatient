package com.vice.bloodpressure.ui.activity.insulin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ToastUtils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.lyd.baselib.utils.eventbus.EventBusUtils;
import com.quinovaresdk.bletransfer.BleTransfer;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.insulin.InsulinAddDeviceListAdapter;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.bean.BlueInfo;
import com.vice.bloodpressure.event.BlueConnectEvent;
import com.vice.bloodpressure.ui.activity.injection.InjectionProgramBindDeviceActivity;
import com.vice.bloodpressure.utils.MySPUtils;
import com.vice.bloodpressure.view.LoadingImageView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:添加设备
 */
public class InsulinAddDeviceActivity extends XYSoftUIBaseActivity {
    private static final int LOCATION_PERMISSIONS_REQUEST_CODE = 10;
    private static final int REQUEST_ENABLE_BT = 11;
    private BluetoothAdapter mAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private LinearLayout llNoData;
    private LinearLayout llSearch;
    private TextView tvSearch;
    private TextView tvBleTips;
    private LoadingImageView loadingImageView;
    private ListView lvDevice;

    private String keywords;
    private final List<BlueInfo> list = new ArrayList<>();
    private InsulinAddDeviceListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().titleTextView().setText("添加设备");
        topViewManager().moreTextView().setText("确定");
        topViewManager().moreTextView().setOnClickListener(v -> {
            save();
        });
        keywords = getIntent().getStringExtra("keywords");
        containerView().addView(initView());
        // 注册BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothStateReceiver, filter);
        EventBusUtils.register(this);
        initValues();
    }

    private void initValues() {
        adapter = new InsulinAddDeviceListAdapter(getPageContext(), list);
        lvDevice.setAdapter(adapter);
    }


    private View initView() {
        View view = View.inflate(getPageContext(), R.layout.activity_insulin_add_device, null);
        llNoData = view.findViewById(R.id.ll_insulin_add_device_no_data);
        lvDevice = view.findViewById(R.id.lv_insulin_add_device);
        lvDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //保存本地  并掉接口
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setCheck(false);
                }
                list.get(position).setCheck(true);
                adapter.notifyDataSetChanged();
            }
        });
        llSearch = view.findViewById(R.id.ll_insulin_add_device_search);
        tvSearch = view.findViewById(R.id.tv_insulin_add_device_search);
        loadingImageView = view.findViewById(R.id.lim_insulin_add_device_search);
        tvBleTips = view.findViewById(R.id.tv_insulin_add_ble_tips);

        llSearch.setOnClickListener(v -> {
            loadingImageView.setBackgroundResource(R.drawable.loading_progress_bar);
            llSearch.setClickable(false);
            loadingImageView.setVisibility(View.VISIBLE);
            loadingImageView.startLoadingAnim();
            llSearch.setBackground(getResources().getDrawable(R.drawable.shape_bg_low_green_90));
            tvSearch.setText("搜索中");
            startScan();
        });
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
            tvBleTips.setVisibility(View.VISIBLE);
            //            ToastUtils.showShort("请打开蓝牙再试");
            return;
        }
        if (initBlueBooth()) {
            //申请位置权限  扫描蓝牙需要
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSIONS_REQUEST_CODE);
            } else {
                //有权限  这里注释了
                //                startScan();
            }
        }
    }

    boolean scanSuccess = false;

    private void startScan() {
        Log.i("yys", "开始扫描");
        //变成扫描中
        if (mAdapter.isDiscovering()) {
            bluetoothLeScanner.stopScan(scanCallback);
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showPop();
            }
        }, 30_000);
        bluetoothLeScanner = mAdapter.getBluetoothLeScanner();
        bluetoothLeScanner.startScan(scanCallback);
        loadingImageView.startLoadingAnim();
    }


    private void showPop() {
        //可以再次扫描
        if (!scanSuccess) {
            llSearch.setClickable(true);
            loadingImageView.setVisibility(View.GONE);
            llSearch.setBackground(getResources().getDrawable(R.drawable.shape_bg_main_green_90));
            tvSearch.setText("搜索");
            //没有扫描到设备
            loadingImageView.stopLoaddingAnim();
            bluetoothLeScanner.stopScan(scanCallback);
            //弹出弹窗
            llNoData.setVisibility(View.VISIBLE);
            //再次扫描
            //            llNoData.setOnClickListener(v -> {
            //                //是否搜索到了设备
            //                scanSuccess = false;
            //                startScan();
            //            });
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
                    if (result.getDevice().getName().trim().contains("AI") || result.getDevice().getName().trim().contains("Ai")) {
                        addDevice(result.getDevice());
                    }

                }
            }
        }
    };

    private void addDevice(BluetoothDevice device) {
        runOnUiThread(() -> {
            //扫描到了
            bluetoothLeScanner.stopScan(scanCallback);
            scanSuccess = true;
            llSearch.setClickable(true);
            loadingImageView.setVisibility(View.GONE);
            loadingImageView.stopLoaddingAnim();
            llSearch.setBackground(getResources().getDrawable(R.drawable.shape_bg_main_green_90));
            tvSearch.setText("搜索");
            llNoData.setVisibility(View.GONE);
            lvDevice.setVisibility(View.VISIBLE);
            //连接蓝牙
            String deviceAddress = device.getAddress();
            Log.i("yys", "deviceAddress==" + deviceAddress);

            boolean isExist = false;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getDevice().getAddress().equals(deviceAddress)) {
                    isExist = true;
                }
            }
            if (!isExist) {
                list.add(new BlueInfo(device));
                adapter.notifyDataSetChanged();
            }
        });
    }

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

    private boolean isClick = true;

    private void save() {
        if (!isClick) {
            return;
        }
        if (list.size() == 0) {
            return;
        }
        int pos = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isCheck()) {
                pos = i;
            }
        }
        if (pos == -1) {
            return;
        }
        getSerialNum(pos);
    }

    private void getSerialNum(int pos) {
        isClick = false;
        String mac = list.get(pos).getDevice().getAddress();
        String deviceName = list.get(pos).getDevice().getName();

        bindDevice(mac, deviceName);

//        BleUtils.getInstance().connect(getPageContext(), mac, new BleUtils.OnDataCallBackImpl() {
//            @Override
//            public void connect() {
//                runOnUiThread(() -> {
//                    new Handler().postDelayed(() -> BleUtils.getInstance().sendData("0577A00348"), 1_000);
//                });
//            }
//
//            @Override
//            public void onSerialNum(String serialNum) {
//                //获取设备号
//                runOnUiThread(() -> {
//                    isClick = true;
//                    bindDevice(mac, serialNum);
//                    BleUtils.getInstance().disConnect();
//                });
//            }
//        });
    }

    private void bindDevice(String mac, String eqcode) {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        String token = loginBean.getToken();
        Call<String> requestCall = DataManager.bindeqinsulin(eqcode, token, (call, response) -> {
            ToastUtils.showShort(response.msg);
            if (response.code == 200) {
                MySPUtils.putString(getPageContext(), MySPUtils.BLUE_MAC, mac);
//                MySPUtils.putString(getPageContext(), MySPUtils.SERIAL_NUMBER, eqcode);
                MySPUtils.putString(getPageContext(), MySPUtils.DEVICE_NAME, eqcode);
                MySPUtils.putString(getPageContext(), MySPUtils.BLUE_TYPE, keywords);
                finish();
            }
        }, (call, t) -> {
            ToastUtils.showShort("网络连接异常");
        });
    }
}
