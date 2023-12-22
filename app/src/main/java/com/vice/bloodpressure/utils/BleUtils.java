package com.vice.bloodpressure.utils;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.vice.bloodpressure.bean.insulin.RecordBigInfo;
import com.vice.bloodpressure.bean.insulin.RecordErrorInfo;
import com.vice.bloodpressure.bean.insulin.RecordInfo;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BleUtils {
    BluetoothGattCharacteristic mCharacteristic;
    private Context context;
    private boolean isConnectSuccess;
    private BluetoothGatt gatt;
    private onDataCallBack callBack;
    private static BleUtils instance;
    private boolean isCanSend = true;


    private BleUtils() {
    }

    public static BleUtils getInstance() {
        if (instance == null) {
            Class var0 = BleUtils.class;
            synchronized (BleUtils.class) {
                if (instance == null) {
                    instance = new BleUtils();
                }
            }
        }
        return instance;
    }

    public void connect(boolean isConnectSuccess, Context context, String mac, onDataCallBack callBack) {
        if (!TextUtils.equals(MySPUtils.getString(context,MySPUtils.BLUE_TYPE),"1")){
            return;
        }
        Log.i("yys", "开始链接");
        this.context = context;
        this.isConnectSuccess = isConnectSuccess;
        this.callBack = callBack;
        BluetoothDevice bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(mac);
        if (bluetoothDevice == null) {
            return;
        }
        //        bluetoothDevice.connectGatt(context, false, gattCallback);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bluetoothDevice.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_LE, BluetoothDevice.PHY_LE_1M_MASK);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bluetoothDevice.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_LE);
        } else {
            bluetoothDevice.connectGatt(context, false, gattCallback);
        }

    }

    public boolean sendData(String param) {
        if (!TextUtils.equals(MySPUtils.getString(context,MySPUtils.BLUE_TYPE),"1")){
            return false;
        }
        mCharacteristic.setValue(hex2byte(param));
        gatt.writeCharacteristic(mCharacteristic);
        return true;
    }

    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i("yys", "onConnectionStateChange: ==" + "已链接");
                // 连接成功，进行服务发现
                gatt.discoverServices();
                BleUtils.this.gatt = gatt;
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i("yys", "onConnectionStateChange: ==" + "已断开");
                // 连接断开，处理断开逻辑
                isCanSend = true;
                BleUtils.this.gatt = gatt;
                BleUtils.this.gatt.close();
                //                isConnectSuccess = false;
                if (callBack != null) {
                    callBack.onDisConnect(isConnectSuccess);
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.i("yys", "onServicesDiscovered==" + BluetoothGatt.GATT_SUCCESS);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                BluetoothGattService service = gatt.getService(UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb"));
                mCharacteristic = service.getCharacteristic(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"));
                gatt.setCharacteristicNotification(mCharacteristic, true);

                for (BluetoothGattDescriptor descriptor : mCharacteristic.getDescriptors()) {
                    if ((mCharacteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    } else if ((mCharacteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0) {
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                    }
                    gatt.writeDescriptor(descriptor);
                }
                callBack.onConnect();
            } else {
                // 服务发现失败
                Log.i("yys", "发现服务失败"
                );
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic
                characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            byte[] value = characteristic.getValue();
            Log.i("yys", "读取成功==onCharacteristicChanged==" + value);
            Log.i("yys", "receiveData.length==" + value.length);
            String hexString = bytesToHex(value);
            Log.i("yys", "读取成功==onCharacteristicChanged==" + hexString);
            isConnectSuccess = true;
            ByteBuffer buf = ByteBuffer.wrap(value);
            byte byte01;
            byte byte02;
            byte byte03;
            byte byte04;
            byte byte05;
            byte byte06;
            byte byte07;
            byte byte08;
            byte byte09;
            byte byte10;
            byte byte11;
            byte byte12;
            byte byte13;
            byte byte14;
            byte byte15;
            byte byte16;
            byte byte17;
            byte byte18;
            byte byte19;
            byte byte20;
            byte01 = buf.get();
            byte02 = buf.get();
            byte03 = buf.get();
            if (TextUtils.equals("55", bytesToHex(new byte[]{byte02}))) {
                if (callBack != null) {
                    callBack.onRecordInfoList(new ArrayList<>());
                    callBack.onRecordBigInfoList(new ArrayList<>());
                    callBack.onRecordErrorList(new ArrayList<>());
                }
                return;
            }
            if (TextUtils.equals("66", bytesToHex(new byte[]{byte02}))) {
                //成功
                switch (bytesToHex(new byte[]{byte03})) {
                    case "A0":
                        //序列号  共9字节
                        byte04 = buf.get();
                        byte05 = buf.get();
                        byte06 = buf.get();
                        byte07 = buf.get();

                        StringBuilder builder = new StringBuilder();
                        builder.append(bytesToHex(new byte[]{byte04}));
                        builder.append(bytesToHex(new byte[]{byte05}));
                        builder.append(bytesToHex(new byte[]{byte06}));
                        builder.append(bytesToHex(new byte[]{byte07}));

                        if (callBack != null) {
                            callBack.onSerialNum(builder.toString());
                        }
                        break;
                    case "A1":
                        //泵工作状态  共10字节
                        byte04 = buf.get();
                        byte05 = buf.get();
                        byte06 = buf.get();
                        byte07 = buf.get();
                        byte08 = buf.get();

                        if (callBack != null) {
                            callBack.onWorkState(bytesToHex(new byte[]{byte04}), byte05, byte06, bytesToHex(new byte[]{byte07}), bytesToHex(new byte[]{byte08}));
                        }
                        break;
                    case "A2":
                        byte04 = buf.get();
                        byte05 = buf.get();
                        byte06 = buf.get();
                        if (callBack != null) {
                            callBack.onBaseData(bytesToHex(new byte[]{byte04}), bytesToHex(new byte[]{byte05}), bytesToHex(new byte[]{byte06}));
                        }
                        break;
                    case "A3":
                        byte04 = buf.get();
                        byte05 = buf.get();
                        byte06 = buf.get();
                        byte07 = buf.get();
                        byte08 = buf.get();
                        byte09 = buf.get();
                        byte10 = buf.get();
                        byte11 = buf.get();
                        byte12 = buf.get();
                        byte13 = buf.get();
                        byte14 = buf.get();
                        byte15 = buf.get();
                        byte16 = buf.get();
                        byte17 = buf.get();
                        baseRate.add(bytesToHex(new byte[]{byte06}));
                        baseRate.add(bytesToHex(new byte[]{byte07}));
                        baseRate.add(bytesToHex(new byte[]{byte08}));
                        baseRate.add(bytesToHex(new byte[]{byte09}));
                        baseRate.add(bytesToHex(new byte[]{byte10}));
                        baseRate.add(bytesToHex(new byte[]{byte11}));
                        baseRate.add(bytesToHex(new byte[]{byte12}));
                        baseRate.add(bytesToHex(new byte[]{byte13}));
                        baseRate.add(bytesToHex(new byte[]{byte14}));
                        baseRate.add(bytesToHex(new byte[]{byte15}));
                        baseRate.add(bytesToHex(new byte[]{byte16}));
                        baseRate.add(bytesToHex(new byte[]{byte17}));
                        if (TextUtils.equals("02", bytesToHex(new byte[]{byte05}))) {
                            if (callBack != null) {
                                ArrayList<String> strings = new ArrayList<>();
                                strings.addAll(baseRate);
                                callBack.onBaseRate(strings);
                                baseRate.clear();
                            }
                        }
                        break;
                    case "A7":
                    case "A9":
                        byte04 = buf.get();
                        byte05 = buf.get();
                        byte06 = buf.get();
                        byte07 = buf.get();
                        byte08 = buf.get();
                        byte09 = buf.get();
                        RecordInfo info = new RecordInfo();
                        info.setMonth(bytesToHex(new byte[]{byte06}));
                        info.setDate(bytesToHex(new byte[]{byte07}));
                        info.setDataHeight(byte08);
                        info.setDataLow(byte09);
                        recordInfoList.add(info);
                        if (TextUtils.equals(bytesToHex(new byte[]{byte04}), bytesToHex(new byte[]{byte05}))) {
                            if (callBack != null) {
                                List<RecordInfo> list = new ArrayList<>();
                                list.addAll(recordInfoList);
                                callBack.onRecordInfoList(list);
                                recordInfoList.clear();
                            }
                        }
                        break;
                    case "A8":
                        byte04 = buf.get();
                        byte05 = buf.get();
                        byte06 = buf.get();
                        byte07 = buf.get();
                        byte08 = buf.get();
                        byte09 = buf.get();
                        byte10 = buf.get();
                        byte11 = buf.get();
                        byte12 = buf.get();
                        byte13 = buf.get();
                        RecordBigInfo bigInfo = new RecordBigInfo();
                        bigInfo.setMonth(bytesToHex(new byte[]{byte06}));
                        bigInfo.setDate(bytesToHex(new byte[]{byte07}));
                        bigInfo.setHour(bytesToHex(new byte[]{byte08}));
                        bigInfo.setMinute(bytesToHex(new byte[]{byte09}));
                        bigInfo.setType(bytesToHex(new byte[]{byte10}));
                        bigInfo.setTimes(bytesToHex(new byte[]{byte11}));
                        bigInfo.setDataHeight(byte12);
                        bigInfo.setDataLow(byte13);
                        recordBigInfoList.add(bigInfo);
                        if (TextUtils.equals(bytesToHex(new byte[]{byte04}), bytesToHex(new byte[]{byte05}))) {
                            if (callBack != null) {
                                List<RecordBigInfo> list = new ArrayList<>();
                                list.addAll(recordBigInfoList);
                                callBack.onRecordBigInfoList(list);
                                recordBigInfoList.clear();
                            }
                        }
                        break;
                    case "AA":
                        byte04 = buf.get();
                        byte05 = buf.get();
                        byte06 = buf.get();
                        byte07 = buf.get();
                        byte08 = buf.get();
                        byte09 = buf.get();
                        byte10 = buf.get();
                        RecordErrorInfo errorInfo = new RecordErrorInfo();
                        errorInfo.setMonth(bytesToHex(new byte[]{byte06}));
                        errorInfo.setDate(bytesToHex(new byte[]{byte07}));
                        errorInfo.setHour(bytesToHex(new byte[]{byte08}));
                        errorInfo.setMinute(bytesToHex(new byte[]{byte09}));
                        errorInfo.setType(bytesToHex(new byte[]{byte10}));
                        recordErrorInfoList.add(errorInfo);
                        if (TextUtils.equals(bytesToHex(new byte[]{byte04}), bytesToHex(new byte[]{byte05}))) {
                            if (callBack != null) {
                                List<RecordErrorInfo> list = new ArrayList<>();
                                list.addAll(recordErrorInfoList);
                                callBack.onRecordErrorList(list);
                                recordErrorInfoList.clear();
                            }
                        }
                        break;
                    default:
                        break;
                }

            }
        }
    };

    private byte[] hex2byte(String hexString) {
        String HEX = "0123456789ABCDEF";
        if (hexString != null && !"".equals(hexString)) {
            hexString = hexString.toUpperCase();
            int length = hexString.length() / 2;
            char[] hexChars = hexString.toCharArray();
            byte[] d = new byte[length];

            for (int i = 0; i < length; ++i) {
                int pos = i * 2;
                d[i] = (byte) (HEX.indexOf(hexChars[pos]) << 4 | HEX.indexOf(hexChars[pos + 1]));
            }
            return d;
        } else {
            return null;
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString().toUpperCase();
    }

    private final List<String> baseRate = new ArrayList<>();
    private final List<RecordInfo> recordInfoList = new ArrayList<>();
    private final List<RecordBigInfo> recordBigInfoList = new ArrayList<>();
    private final List<RecordErrorInfo> recordErrorInfoList = new ArrayList<>();


    public static class OnDataCallBackImpl implements onDataCallBack {

        @Override
        public void onDisConnect(boolean isSuccess) {

        }

        @Override
        public void onConnect() {

        }

        @Override
        public void onSerialNum(String serialNum) {

        }

        @Override
        public void onWorkState(String ele, byte drugHeight, byte drugLow, String isBlock, String infuSwitch) {

        }

        @Override
        public void onBaseData(String baseState, String baseValue, String baseValueAll) {

        }

        @Override
        public void onBaseRate(List<String> baseRateList) {

        }

        @Override
        public void onRecordInfoList(List<RecordInfo> recordInfoList) {

        }

        @Override
        public void onRecordBigInfoList(List<RecordBigInfo> recordInfoList) {

        }

        @Override
        public void onRecordErrorList(List<RecordErrorInfo> recordInfoList) {

        }
    }

    public interface onDataCallBack {
        void onDisConnect(boolean isSuccess);

        void onConnect();

        void onSerialNum(String serialNum);

        void onWorkState(String ele, byte drugHeight, byte drugLow, String isBlock, String infuSwitch);

        void onBaseData(String baseState, String baseValue, String baseValueAll);

        void onBaseRate(List<String> baseRateList);

        void onRecordInfoList(List<RecordInfo> recordInfoList);

        void onRecordBigInfoList(List<RecordBigInfo> recordInfoList);

        void onRecordErrorList(List<RecordErrorInfo> recordInfoList);

    }

    private BluetoothLeScanner bluetoothLeScanner;
    private ScanCallback scanCallback;

    public boolean initBlueBooth(Activity context) {
        BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mAdapter == null) {
            Toast.makeText(context, "当前设备不支持蓝牙功能", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!checkBle(context)) {
            Toast.makeText(context, "当前设备不支持ble蓝牙功能", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!mAdapter.isEnabled()) {
            //没有在开启中也没有打开
            if (mAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                Toast.makeText(context, "请打开手机蓝牙 ", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        //打开位置权限
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return false;
        } else {
            //有权限  这里注释了
            new Thread(new Runnable() {
                @Override
                public void run() {
                    bluetoothLeScanner = mAdapter.getBluetoothLeScanner();
                    scanCallback = new ScanCallback() {
                        @Override
                        public void onScanResult(int callbackType, ScanResult result) {
                            super.onScanResult(callbackType, result);
                            if (result != null) {
                                if (TextUtils.equals(result.getDevice().getAddress(), MySPUtils.getString(context, MySPUtils.BLUE_MAC))) {
                                    bluetoothLeScanner.stopScan(scanCallback);
                                }
                            }
                        }
                    };
                    bluetoothLeScanner.startScan(scanCallback);
                }
            }).start();
            new Handler().postDelayed(() -> stopScan(),30_000);
        }
        return true;
    }

    public void stopScan(){
        if (bluetoothLeScanner!=null&&scanCallback!=null){
            bluetoothLeScanner.stopScan(scanCallback);
        }
    }

    private boolean checkBle(Context context) {
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return false;
        }
        return true;
    }

    public void disConnect() {
        gatt.disconnect();
    }


    public static int hexToInt(String hex) {
        int result = 0;
        for (int i = 0; i < hex.length(); i++) {
            int digit = Character.digit(hex.charAt(i), 16);
            result = result * 16 + digit;
        }
        return result;
    }

    public static double byte2double(byte b1, byte b2) {
        short value = (short) ((b1 << 8) | (b2 & 0xFF));
        double a = (double) value / 10;
        return a;
    }

    public static double byte2double(byte b1, byte b2, int num) {
        short value = (short) ((b1 << 8) | (b2 & 0xFF));
        double a = (double) value / num;
        return a;
    }
}
