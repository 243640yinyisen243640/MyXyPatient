package com.vice.bloodpressure.ui.activity.healthrecordlist;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleScanAndConnectCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.lyd.baselib.utils.ble.BleUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.HealthAdapter;
import com.vice.bloodpressure.base.activity.BaseActivity;
import com.vice.bloodpressure.ui.activity.healthrecordadd.BloodOxygenAddActivity;
import com.vice.bloodpressure.ui.activity.healthrecordadd.BloodPressureAddActivity;
import com.vice.bloodpressure.ui.activity.healthrecordadd.BloodSugarAddActivity;
import com.vice.bloodpressure.ui.activity.healthrecordadd.BmiAddActivity;
import com.vice.bloodpressure.ui.activity.healthrecordadd.CheckAddActivity;
import com.vice.bloodpressure.ui.activity.healthrecordadd.FoodAddActivity;
import com.vice.bloodpressure.ui.activity.healthrecordadd.HemoglobinAddActivity;
import com.vice.bloodpressure.ui.activity.healthrecordadd.HepatopathyPabulumAddActivity;
import com.vice.bloodpressure.ui.activity.healthrecordadd.PharmacyAddActivity;
import com.vice.bloodpressure.ui.activity.healthrecordadd.SportAddActivity;
import com.vice.bloodpressure.ui.activity.healthrecordadd.TemperatureAddActivity;
import com.vice.bloodpressure.ui.activity.healthrecordadd.WeightAddActivity;
import com.vice.bloodpressure.utils.BleDialogUtils;
import com.vice.bloodpressure.utils.DataConvert;
import com.vice.bloodpressure.view.popu.ShowBleWaitingPopup;
import com.vice.bloodpressure.view.popu.ShowTemperaturePopup;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;

/**
 * ??????: ??????????????????
 * ??????: LYD
 * ????????????: 2019/3/18 13:55
 */

public class HealthActivity extends BaseActivity {
    private static final int OPEN_BLE = 10086;
    private Intent intent;
    private String bDeviceBName = "My Thermometer";
    //?????????
    private int scanTime = 10 * 1000;

    private TypedArray healthImages = Utils.getApp().getResources().obtainTypedArray(R.array.health_item_img);
    private String[] healthNames = Utils.getApp().getResources().getStringArray(R.array.health_item_name);
    private String[] healthSlogans = Utils.getApp().getResources().getStringArray(R.array.health_item_slogan);

    private BluetoothStateBroadcastReceiver mReceiver;

    private BleDialogUtils bleDialogUtils;

    private UUID uuid_service;
    private UUID uuid_chara;

    private ShowTemperaturePopup temperaturePopup;
    private ShowBleWaitingPopup showBleWaitingPopup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("????????????");
        bleDialogUtils = new BleDialogUtils();
        showBleWaitingPopup = new ShowBleWaitingPopup(this);
        showBleWaitingPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        showBleWaitingPopup.setAllowDismissWhenTouchOutside(false);
        setScanRule();
        registerBluetoothReceiver();
        getTvSave().setVisibility(View.VISIBLE);
        getTvSave().setText(getString(R.string.healthy_right));
        getTvSave().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLocation();
            }
        });
        initView();
    }

    private void requestLocation() {
        PermissionUtils
                .permission(PermissionConstants.LOCATION)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        BleStart();
                    }

                    @Override
                    public void onDenied() {
                        ToastUtils.showShort(R.string.please_open_gps);
                        Log.e("yys", "accessFineLocationDenied");
                    }
                }).request();
    }

    /**
     * ??????????????????
     */
    private void BleStart() {
        boolean bleState = BleUtils.getBleState();
        if (bleState) {
            dialogStartConnect();
            startScanBpAndAio();
        } else {
            BleUtils.openBle(this, OPEN_BLE);
        }
    }


    /**
     * ??????????????????
     */
    private void setScanRule() {
        BleScanRuleConfig scanRuleConfig =
                new BleScanRuleConfig.Builder()
                        //??????????????????????????????????????????
                        .setDeviceName(true, bDeviceBName)
                        //????????????autoConnect????????????????????????false
                        .setAutoConnect(false)
                        //????????????????????????????????????10???
                        .setScanTimeOut(scanTime)
                        .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

    /**
     * ??????
     */
    private void registerBluetoothReceiver() {
        if (mReceiver == null) {
            mReceiver = new BluetoothStateBroadcastReceiver();
        }
        IntentFilter stateChangeFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        IntentFilter connectedFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter disConnectedFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(mReceiver, stateChangeFilter);
        registerReceiver(mReceiver, connectedFilter);
        registerReceiver(mReceiver, disConnectedFilter);
    }

    /**
     * ????????????
     */
    private void unregisterBluetoothReceiver() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    /**
     * ????????????
     */
    private void startScanBpAndAio() {
        BleManager.getInstance().scanAndConnect(new BleScanAndConnectCallback() {
            @Override
            public void onScanStarted(boolean success) {
                Log.e("yys", "onScanStarted");
            }

            @Override
            public void onScanFinished(BleDevice scanResult) {
                Log.e("yys", "onScanFinished");
                //?????????????????????????????????????????????????????????????????????BLE??????????????????????????????????????????????????????
                if (scanResult == null) {
                    Log.e("yys", "onScanFinished:" + "????????????");
                } else {
                    Log.e("yys", "onScanFinished:" + "?????????");

                }
            }

            @Override
            public void onStartConnect() {
                Log.e("yys", "onStartConnect");
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                //????????????
                Log.e("yys", "onConnectFail");
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                String name = bleDevice.getName();
                Log.e("yys", "???????????????????????????==" + name);
                //                showBleWaitingPopup.dismiss();
                dialogConnectSuccess();
                //                Log.i("yys", "mac===" + bleDevice.getMac());
                if (bDeviceBName.equals(name)) {
                    for (BluetoothGattService service : gatt.getServices()) {
                        uuid_service = service.getUuid();
                        Log.i("yys", "uuid_service==" + uuid_service.toString());
                        List<BluetoothGattCharacteristic> characteristicList = service.getCharacteristics();
                        for (BluetoothGattCharacteristic characteristic : characteristicList) {
                            uuid_chara = characteristic.getUuid();
                        }
                    }
                    BleManager.getInstance().notify(
                            bleDevice,
                            uuid_service.toString(),
                            uuid_chara.toString(),
                            new BleNotifyCallback() {
                                @Override
                                public void onNotifySuccess() {
                                    // ????????????????????????
                                    Log.i("yys", "??????????????????");
                                    BleManager.getInstance().read(
                                            bleDevice,
                                            uuid_service.toString(),
                                            uuid_chara.toString(),
                                            new BleReadCallback() {
                                                @Override
                                                public void onReadSuccess(byte[] data) {
                                                    Log.i("yys", "onReadSuccess");
                                                }

                                                @Override
                                                public void onReadFailure(BleException exception) {
                                                    Log.i("yys", "onReadFailure");
                                                }
                                            }
                                    );
                                }

                                @Override
                                public void onNotifyFailure(BleException exception) {
                                    // ????????????????????????
                                    Log.i("yys", "onNotifyFailure");
                                }

                                @Override
                                public void onCharacteristicChanged(byte[] data) {
                                    // ????????????????????????????????????????????????????????????
                                    Log.i("yys", "onCharacteristicChanged" + data);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.e("yys", "data==" + data);
                                            //                                            dialogDismiss();
                                            String convertString = ConvertUtils.bytes2HexString(data);
                                            showPop(DataConvert.hexStrToUTF8(convertString));
                                        }
                                    });
                                }
                            });
                }
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                Log.e("yys", "onDisConnected");
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                Log.e("yys", "onScanning");
            }
        });
    }

    private void showPop(String temperature) {
        temperaturePopup = new ShowTemperaturePopup(this);
        TextView temperatureTextView = temperaturePopup.findViewById(R.id.tv_temperature_text_result);
        temperatureTextView.setText(temperature);
        temperaturePopup.showPopupWindow();
        countDown(temperature);
    }

    private void countDown(String temperature) {
        CountDownTimer timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                saveData(temperature);
            }
        }.start();

    }


    /**
     * ??????????????????
     *
     * @param temperature
     */
    private void saveData(String temperature) {
        LoginBean user = (LoginBean) SharedPreferencesUtils.getBean(Utils.getApp(), SharedPreferencesUtils.USER_INFO);
        String userid = user.getUserid();
        String docId = SPStaticUtils.getString("docId");
        String token = user.getToken();
        Call<String> requestCall = DataManager.saveDataTemperature(token, userid, temperature, "", docId, "1", (call, response) -> {
            if (response.code == 200) {
                temperaturePopup.dismiss();
                ToastUtils.showShort(getString(R.string.show_up_msg));
            } else {
                temperaturePopup.dismiss();
            }
        }, (call, t) -> {
            temperaturePopup.dismiss();
            ToastUtils.showShort(getString(R.string.network_error));
        });
    }


    /**
     * ????????????
     */
    private void dialogStartConnect() {

        showBleWaitingPopup.showPopupWindow();
        //        bleDialogUtils.showProgress(HealthActivity.this, getString(R.string.ble_connecting));
    }

    /**
     * ????????????
     */
    private void dialogConnectSuccess() {
        showBleWaitingPopup.dismiss();
        //        bleDialogUtils.mProgressDialog = null;
        Toast.makeText(this, "????????????", Toast.LENGTH_LONG).show();
    }

    /**
     * ????????????
     */
    private void dialogDismiss() {
        bleDialogUtils.dismissProgress();
    }

    /**
     * ????????????
     */
    private void dialogConnectFailed() {
        bleDialogUtils.dismissProgress();
        ToastUtils.showLong(R.string.ble_connect_error);
    }

    /**
     * ?????????????????????
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_BLE) {
            switch (resultCode) {
                case Activity.RESULT_OK://????????????
                    //Log.e(TAG, "????????????");
                    dialogStartConnect();
                    startScanBpAndAio();
                    //????????????????????????
                    break;
                case Activity.RESULT_CANCELED://????????????
                    //Log.e(TAG, "????????????");
                    ToastUtils.showShort("???????????????");
                    break;
            }
        }
    }

    /**
     * ?????????????????????
     */
    private class BluetoothStateBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            switch (action) {
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    //Toast.makeText(context , "????????????:" + device.getName() + "?????????", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    //Toast.makeText(context , "????????????:" + device.getName() + "?????????", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_TURNING_ON:
                            break;
                        case BluetoothAdapter.STATE_ON:
                            ToastUtils.showShort("???????????????");
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            break;
                        case BluetoothAdapter.STATE_OFF:
                            ToastUtils.showShort("???????????????");
                            break;
                    }
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBluetoothReceiver();
    }

    private void initView() {
        ListView lvHealth = findViewById(R.id.lv_health);
        HealthAdapter healthAdapter = new HealthAdapter(this, healthNames, healthSlogans, healthImages, new HealthAdapter.HealthInterface() {
            @Override
            public void addOnClick(int position) {
                switch (position) {
                    case 0:
                        intent = new Intent(getPageContext(), BloodSugarAddActivity.class);//????????????
                        break;
                    case 1:
                        intent = new Intent(getPageContext(), BloodPressureAddActivity.class);//????????????
                        break;
                    case 2:
                        intent = new Intent(getPageContext(), FoodAddActivity.class);//????????????
                        break;
                    case 3:
                        intent = new Intent(getPageContext(), SportAddActivity.class);//????????????
                        break;
                    case 4:
                        intent = new Intent(getPageContext(), PharmacyAddActivity.class);//????????????
                        intent.putExtra("type", "0");
                        break;
                    case 5:
                        intent = new Intent(getPageContext(), BmiAddActivity.class);//??????BMI
                        break;
                    case 6:
                        intent = new Intent(getPageContext(), HemoglobinAddActivity.class);//??????????????????
                        break;
                    case 7:
                        intent = new Intent(getPageContext(), CheckAddActivity.class);//????????????
                        break;
                    case 8:
                        intent = new Intent(getPageContext(), HepatopathyPabulumAddActivity.class);//????????????
                        break;
                    case 9:
                        intent = new Intent(getPageContext(), WeightAddActivity.class);
                        break;
                    case 10:
                        intent = new Intent(getPageContext(), BloodOxygenAddActivity.class);
                        break;
                    case 11:
                        intent = new Intent(getPageContext(), TemperatureAddActivity.class);
                        break;
                }
                intent.putExtra("position", position);
                startActivity(intent);
            }

            @Override
            public void lookRecord(int position) {//????????????
                switch (position) {
                    case 0:
                        intent = new Intent(getPageContext(), BloodSugarListActivity.class);//????????????
                        break;
                    case 1:
                        intent = new Intent(getPageContext(), BloodPressureListActivity.class);//????????????
                        break;
                    case 2:
                        intent = new Intent(getPageContext(), FoodListActivity.class);//????????????
                        break;
                    case 3:
                        intent = new Intent(getPageContext(), SportListActivity.class);//????????????
                        break;
                    case 4:
                        intent = new Intent(getPageContext(), MedicineUseListActivity.class);//????????????
                        break;
                    case 5:
                        intent = new Intent(getPageContext(), BmiListActivity.class);//????????????
                        break;
                    case 6:
                        intent = new Intent(getPageContext(), HemoglobinListActivity.class);//??????????????????
                        break;
                    case 7:
                        intent = new Intent(getPageContext(), CheckListActivity.class);//????????????
                        break;
                    case 8:
                        intent = new Intent(getPageContext(), HepatopathyPabulumListActivity.class);//????????????
                        break;
                    case 9:
                        intent = new Intent(getPageContext(), WeightListActivity.class);//????????????
                        break;
                    case 10:
                        intent = new Intent(getPageContext(), BloodOxygenListActivity.class);//????????????
                        break;
                    case 11:
                        intent = new Intent(getPageContext(), TemperatureListActivity.class);
                        break;

                    default:
                        break;
                }
                intent.putExtra("key", position);
                startActivity(intent);
            }
        });
        lvHealth.setAdapter(healthAdapter);
    }

    @Override
    protected View addContentLayout() {
        return getLayoutInflater().inflate(R.layout.activity_health, contentLayout, false);
    }
}
