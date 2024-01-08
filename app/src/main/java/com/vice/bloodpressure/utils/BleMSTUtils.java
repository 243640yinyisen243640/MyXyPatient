package com.vice.bloodpressure.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.vice.bloodpressure.bean.insulin.MSTRecordDataInfo;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 迈世通蓝牙
 */
public class BleMSTUtils {
    private Context context;
    private BluetoothGatt bluetoothGatt;
    private onDataCallBack callBack;
    private static BleMSTUtils instance;
    private static boolean isConnect = false;

    private BleMSTUtils() {

    }

    public static BleMSTUtils getInstance() {
        if (instance == null) {
            synchronized (BleMSTUtils.class) {
                if (instance == null) {
                    instance = new BleMSTUtils();
                }
            }
        }

        return instance;
    }

    public boolean isConnect() {
        if (bluetoothGatt == null) {
            return false;
        }
        if (bluetoothGatt.connect() && isConnect) {
            return true;
        }
        return false;
    }

    public void connect(Context context, String mac) {
        if (!TextUtils.equals(MySPUtils.getString(context, MySPUtils.BLUE_TYPE), "2")) {
            return;
        }
        Log.i("xjh", "开始连接");
        this.context = context;
        BluetoothDevice bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(mac);
        if (bluetoothDevice == null) {
            return;
        }
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        //            bluetoothGatt = bluetoothDevice.connectGatt(context, false, gattCallback, TRANSPORT_LE, PHY_LE_1M_MASK);
        //        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        //            bluetoothGatt = bluetoothDevice.connectGatt(context, false, gattCallback, TRANSPORT_LE);
        //        } else {
        //            bluetoothGatt = bluetoothDevice.connectGatt(context, false, gattCallback);
        //        }
        bluetoothGatt = bluetoothDevice.connectGatt(context, false, gattCallback);
    }

    public void setOnDataCallBack(onDataCallBack callBack) {
        this.callBack = callBack;
    }

    public void setRead() {
        if (bluetoothGatt == null) {
            return;
        }
        bluetoothGatt.discoverServices();
    }

    public void sendData(String param) {
        if (!TextUtils.equals(MySPUtils.getString(context, MySPUtils.BLUE_TYPE), "2")) {
            return;
        }
        Log.i("xjh", "param==" + param);
        if (bluetoothGatt == null) {
            Toast.makeText(context, "请进行蓝牙链接", Toast.LENGTH_SHORT).show();
            return;
        }
        BluetoothGattService service = bluetoothGatt.getService(UUID.fromString("0000ffe5-0000-1000-8000-00805f9b34fb"));
        if (service == null) {
            Toast.makeText(context, "蓝牙写入服务未发现", Toast.LENGTH_SHORT).show();
            return;
        }
        BluetoothGattCharacteristic writeCharacteristic = service.getCharacteristic(UUID.fromString("0000ffe9-0000-1000-8000-00805f9b34fb"));
        if (writeCharacteristic == null) {
            Toast.makeText(context, "蓝牙写入特征未发现", Toast.LENGTH_SHORT).show();
            return;
        }
        initData();
        writeCharacteristic.setValue(hex2byte(param.replaceAll(" ", "")));
        writeCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
        bluetoothGatt.writeCharacteristic(writeCharacteristic);

    }

    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {


        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i("xjh", "onConnectionStateChange: ==" + "已链接");
                // 连接成功，进行服务发现
                //                bluetoothGatt.discoverServices();
                isConnect = true;
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                isConnect = false;
                bluetoothGatt.close();
                Log.i("xjh", "onConnectionStateChange: ==" + "已断开");
                //                // 连接断开，处理断开逻辑
                callBack.onDisConnect();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.i("xjh", "onServicesDiscovered==" + BluetoothGatt.GATT_SUCCESS);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //进行读的服务发现
                // 服务发现成功，处理服务和特征值
                if (bluetoothGatt == null) {
                    Log.i("xjh", "请进行蓝牙链接");
                } else {
                    BluetoothGattService service = bluetoothGatt.getService(UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb"));
                    if (service == null) {
                        Log.i("xjh", "蓝牙读取服务未发现");
                    } else {
                        BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString("0000ffe4-0000-1000-8000-00805f9b34fb"));
                        if (characteristic == null) {
                            Log.i("xjh", "蓝牙读取服务未发现");
                        } else {
                            boolean notification = bluetoothGatt.setCharacteristicNotification(characteristic, true);
                            if (notification) {
                                Log.i("xjh", "打开通知成功");
                            } else {
                                Log.i("xjh", "打开通知失败成功");
                            }
                            List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
                            Log.i("xjh", "descriptors.size==" + descriptors.size());
                            for (int j = 0; j < descriptors.size(); j++) {
                                BluetoothGattDescriptor bluetoothGattDescriptor = descriptors.get(j);
                                bluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                bluetoothGatt.writeDescriptor(bluetoothGattDescriptor);
                            }
                        }
                    }
                }
            } else {
                // 服务发现失败
                Log.i("xjh", "发现服务失败"
                );
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            byte[] value = characteristic.getValue();

            Log.i("xjh", "读取成功==onCharacteristicChanged==" + value);
            Log.i("xjh", "receiveData.length==" + value.length);
            String hexString = bytesToHex(value);
            Log.d("xjh", "读取成功==onCharacteristicChanged==" + hexString);


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
            byte04 = buf.get();
            byte05 = buf.get();


            //读取大剂量回顾数据  共16字节  读取前14字节
            if (TextUtils.equals("01", bytesToHex(new byte[]{byte05}))) {
                byte06 = buf.get();
                byte07 = buf.get();
                byte08 = buf.get();
                byte09 = buf.get();
                byte10 = buf.get();
                byte11 = buf.get();
                byte12 = buf.get();
                byte13 = buf.get();
                byte14 = buf.get();

                if (TextUtils.equals("AA", bytesToHex(new byte[]{byte06}))) {
                    List<MSTRecordDataInfo> infos = new ArrayList<>();
                    infos.addAll(recordBigInfos);
                    recordBigInfos.clear();
                    //完成读取
                    if (callBack != null) {
                        callBack.onBigRecord(infos);
                    }
                } else {
                    String num = String.valueOf(hexToInt(bytesToHex(new byte[]{byte06})));
                    String year = String.valueOf(hexToInt(bytesToHex(new byte[]{byte07})));
                    String month = String.valueOf(hexToInt(bytesToHex(new byte[]{byte08})));
                    String date = String.valueOf(hexToInt(bytesToHex(new byte[]{byte09})));
                    String hour = String.valueOf(hexToInt(bytesToHex(new byte[]{byte10})));
                    String minute = String.valueOf(hexToInt(bytesToHex(new byte[]{byte11})));
                    String second = String.valueOf(hexToInt(bytesToHex(new byte[]{byte12})));
                    String datetime = month + "-" + date + " " + hour + ":" + minute;
                    String data1 = bytesToHex(new byte[]{byte13});
                    String data2 = bytesToHex(new byte[]{byte14});
                    int data = hexToInt(data2 + data1);
                    double dataDouble = data / 10.0;
                    MSTRecordDataInfo recordInfo = new MSTRecordDataInfo(num, datetime, dataDouble + "");
                    recordBigInfos.add(recordInfo);
                }
            }

            //读取警示回顾   共16字节  读取前14字节
            if (TextUtils.equals("03", bytesToHex(new byte[]{byte05}))) {
                byte06 = buf.get();
                byte07 = buf.get();
                byte08 = buf.get();
                byte09 = buf.get();
                byte10 = buf.get();
                byte11 = buf.get();
                byte12 = buf.get();
                byte13 = buf.get();
                byte14 = buf.get();

                if (TextUtils.equals("AA", bytesToHex(new byte[]{byte06}))) {
                    List<MSTRecordDataInfo> infos = new ArrayList<>();
                    infos.addAll(recordErrorInfos);
                    recordErrorInfos.clear();
                    //完成读取
                    if (callBack != null) {
                        callBack.onErrorRecord(infos);
                    }
                } else {
                    String num = String.valueOf(hexToInt(bytesToHex(new byte[]{byte06})));
                    String year = String.valueOf(hexToInt(bytesToHex(new byte[]{byte07})));
                    String month = String.valueOf(hexToInt(bytesToHex(new byte[]{byte08})));
                    String date = String.valueOf(hexToInt(bytesToHex(new byte[]{byte09})));
                    String hour = String.valueOf(hexToInt(bytesToHex(new byte[]{byte10})));
                    String minute = String.valueOf(hexToInt(bytesToHex(new byte[]{byte11})));
                    String second = String.valueOf(hexToInt(bytesToHex(new byte[]{byte12})));
                    String datetime = month + "-" + date + " " + hour + ":" + minute;
                    String data1 = bytesToHex(new byte[]{byte13});
                    String data2 = bytesToHex(new byte[]{byte14});
                    int data = hexToInt(data2 + data1);
                    MSTRecordDataInfo recordInfo = new MSTRecordDataInfo(num, datetime, data + "");
                    recordErrorInfos.add(recordInfo);
                }
            }

            //读取日总量    共16字节  读取前14字节
            if (TextUtils.equals("06", bytesToHex(new byte[]{byte05}))) {
                byte06 = buf.get();
                byte07 = buf.get();
                byte08 = buf.get();
                byte09 = buf.get();
                byte10 = buf.get();
                byte11 = buf.get();
                byte12 = buf.get();
                byte13 = buf.get();
                byte14 = buf.get();

                if (TextUtils.equals("AA", bytesToHex(new byte[]{byte06}))) {
                    List<MSTRecordDataInfo> infos = new ArrayList<>();
                    infos.addAll(recordSunInfos);
                    recordSunInfos.clear();
                    //完成读取
                    if (callBack != null) {
                        callBack.onSunRecord(infos);
                    }
                } else {
                    String num = String.valueOf(hexToInt(bytesToHex(new byte[]{byte06})));
                    String year = String.valueOf(hexToInt(bytesToHex(new byte[]{byte07})));
                    String month = String.valueOf(hexToInt(bytesToHex(new byte[]{byte08})));
                    String date = String.valueOf(hexToInt(bytesToHex(new byte[]{byte09})));
                    String hour = String.valueOf(hexToInt(bytesToHex(new byte[]{byte10})));
                    String minute = String.valueOf(hexToInt(bytesToHex(new byte[]{byte11})));
                    String second = String.valueOf(hexToInt(bytesToHex(new byte[]{byte12})));
                    String datetime = month + "-" + date;
                    String data1 = bytesToHex(new byte[]{byte13});
                    String data2 = bytesToHex(new byte[]{byte14});
                    int data = hexToInt(data2 + data1);
                    double dataDouble = data / 10.0;
                    MSTRecordDataInfo recordInfo = new MSTRecordDataInfo(num, datetime, dataDouble + "");
                    recordSunInfos.add(recordInfo);
                }
            }

            //读取基础率回顾数据
            if (stringList.size() == 0) {
                if (TextUtils.equals("02", bytesToHex(new byte[]{byte05}))) {
                    byte06 = buf.get();
                    if (TextUtils.equals("AA", bytesToHex(new byte[]{byte06}))) {
                        //数据结束
                        List<MSTRecordDataInfo> infos = new ArrayList<>();
                        infos.addAll(recordBaseInfos);
                        recordBaseInfos.clear();
                        if (callBack != null) {
                            callBack.onBaseRecord(infos);
                        }
                    }
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
                    byte18 = buf.get();
                    byte19 = buf.get();
                    byte20 = buf.get();
                    stringList.add(bytesToHex(new byte[]{byte01}));
                    stringList.add(bytesToHex(new byte[]{byte02}));
                    stringList.add(bytesToHex(new byte[]{byte03}));
                    stringList.add(bytesToHex(new byte[]{byte04}));
                    stringList.add(bytesToHex(new byte[]{byte05}));
                    stringList.add(bytesToHex(new byte[]{byte06}));
                    stringList.add(bytesToHex(new byte[]{byte07}));
                    stringList.add(bytesToHex(new byte[]{byte08}));
                    stringList.add(bytesToHex(new byte[]{byte09}));
                    stringList.add(bytesToHex(new byte[]{byte10}));
                    stringList.add(bytesToHex(new byte[]{byte11}));
                    stringList.add(bytesToHex(new byte[]{byte12}));
                    stringList.add(bytesToHex(new byte[]{byte13}));
                    stringList.add(bytesToHex(new byte[]{byte14}));
                    stringList.add(bytesToHex(new byte[]{byte15}));
                    stringList.add(bytesToHex(new byte[]{byte16}));
                    stringList.add(bytesToHex(new byte[]{byte17}));
                    stringList.add(bytesToHex(new byte[]{byte18}));
                    stringList.add(bytesToHex(new byte[]{byte19}));
                    stringList.add(bytesToHex(new byte[]{byte20}));
                }
            } else {
                if (stringList.size() == 100) {
                    byte06 = buf.get();
                    byte07 = buf.get();
                    byte08 = buf.get();
                    byte09 = buf.get();
                    byte10 = buf.get();
                    stringList.add(bytesToHex(new byte[]{byte01}));
                    stringList.add(bytesToHex(new byte[]{byte02}));
                    stringList.add(bytesToHex(new byte[]{byte03}));
                    stringList.add(bytesToHex(new byte[]{byte04}));
                    stringList.add(bytesToHex(new byte[]{byte05}));
                    stringList.add(bytesToHex(new byte[]{byte06}));
                    stringList.add(bytesToHex(new byte[]{byte07}));
                    stringList.add(bytesToHex(new byte[]{byte08}));
                    stringList.add(bytesToHex(new byte[]{byte09}));
                    stringList.add(bytesToHex(new byte[]{byte10}));

                    String year = String.valueOf(hexToInt(stringList.get(6)));
                    String month = String.valueOf(hexToInt(stringList.get(7)));
                    String date = String.valueOf(hexToInt(stringList.get(8)));
                    String hour = String.valueOf(hexToInt(stringList.get(9)));
                    String minute = String.valueOf(hexToInt(stringList.get(10)));
                    String second = String.valueOf(hexToInt(stringList.get(11)));
                    String datetime = month + "-" + date;
                    String num = String.valueOf(hexToInt(stringList.get(5)));

                    List<Double> doubleList = new ArrayList<>();
                    for (int i = 12; i < 108; i = i + 2) {
                        String data1 = stringList.get(i);
                        String data2 = stringList.get(i + 1);
                        int data = hexToInt(data2 + data1);
                        double dataDouble = data / 100.0;
                        doubleList.add(dataDouble);
                    }

                    BigDecimal doubles = new BigDecimal(0);
                    for (int i = 0; i < doubleList.size(); i++) {
                        Log.i("yys", "doubleList" + i + "==" + doubleList.get(i));
                        doubles = doubles.add(BigDecimal.valueOf(doubleList.get(i)));
                    }
                    BigDecimal dataDouble = doubles.divide(new BigDecimal(2));
                    dataDouble.setScale(2, BigDecimal.ROUND_HALF_UP);
                    MSTRecordDataInfo recordInfo = new MSTRecordDataInfo(num, datetime, dataDouble + "");

                    //一条记录
                    recordBaseInfos.add(recordInfo);

                    stringList.clear();

                    byte11 = buf.get();
                    byte12 = buf.get();
                    byte13 = buf.get();
                    byte14 = buf.get();
                    byte15 = buf.get();
                    byte16 = buf.get();
                    byte17 = buf.get();
                    byte18 = buf.get();
                    byte19 = buf.get();
                    byte20 = buf.get();

                    stringList.add(bytesToHex(new byte[]{byte11}));
                    stringList.add(bytesToHex(new byte[]{byte12}));
                    stringList.add(bytesToHex(new byte[]{byte13}));
                    stringList.add(bytesToHex(new byte[]{byte14}));
                    stringList.add(bytesToHex(new byte[]{byte15}));
                    stringList.add(bytesToHex(new byte[]{byte16}));
                    stringList.add(bytesToHex(new byte[]{byte17}));
                    stringList.add(bytesToHex(new byte[]{byte18}));
                    stringList.add(bytesToHex(new byte[]{byte19}));
                    stringList.add(bytesToHex(new byte[]{byte20}));

                    if (TextUtils.equals("AA", bytesToHex(new byte[]{byte16}))) {
                        //数据结束
                        stringList.clear();
                        List<MSTRecordDataInfo> infos = new ArrayList<>();
                        infos.addAll(recordBaseInfos);
                        recordBaseInfos.clear();
                        if (callBack != null) {
                            callBack.onBaseRecord(infos);
                        }
                    }
                    //                    }
                } else if (stringList.size() == 90) {
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
                    byte18 = buf.get();
                    byte19 = buf.get();
                    byte20 = buf.get();
                    stringList.add(bytesToHex(new byte[]{byte01}));
                    stringList.add(bytesToHex(new byte[]{byte02}));
                    stringList.add(bytesToHex(new byte[]{byte03}));
                    stringList.add(bytesToHex(new byte[]{byte04}));
                    stringList.add(bytesToHex(new byte[]{byte05}));
                    stringList.add(bytesToHex(new byte[]{byte06}));
                    stringList.add(bytesToHex(new byte[]{byte07}));
                    stringList.add(bytesToHex(new byte[]{byte08}));
                    stringList.add(bytesToHex(new byte[]{byte09}));
                    stringList.add(bytesToHex(new byte[]{byte10}));
                    stringList.add(bytesToHex(new byte[]{byte11}));
                    stringList.add(bytesToHex(new byte[]{byte12}));
                    stringList.add(bytesToHex(new byte[]{byte13}));
                    stringList.add(bytesToHex(new byte[]{byte14}));
                    stringList.add(bytesToHex(new byte[]{byte15}));
                    stringList.add(bytesToHex(new byte[]{byte16}));
                    stringList.add(bytesToHex(new byte[]{byte17}));
                    stringList.add(bytesToHex(new byte[]{byte18}));
                    stringList.add(bytesToHex(new byte[]{byte19}));
                    stringList.add(bytesToHex(new byte[]{byte20}));

                    String year = String.valueOf(hexToInt(stringList.get(6)));
                    String month = String.valueOf(hexToInt(stringList.get(7)));
                    String date = String.valueOf(hexToInt(stringList.get(8)));
                    String hour = String.valueOf(hexToInt(stringList.get(9)));
                    String minute = String.valueOf(hexToInt(stringList.get(10)));
                    String second = String.valueOf(hexToInt(stringList.get(11)));
                    String num = String.valueOf(hexToInt(stringList.get(5)));
                    String datetime = month + "-" + date;

                    List<Double> doubleList = new ArrayList<>();
                    for (int i = 12; i < 108; i = i + 2) {
                        String data1 = stringList.get(i);
                        String data2 = stringList.get(i + 1);
                        int data = hexToInt(data2 + data1);
                        double dataDouble = data / 100.0;
                        doubleList.add(dataDouble);
                    }

                    BigDecimal doubles = new BigDecimal(0);
                    for (int i = 0; i < doubleList.size(); i++) {
                        Log.i("yys", "doubleList" + i + "==" + doubleList.get(i));
                        doubles = doubles.add(BigDecimal.valueOf(doubleList.get(i)));
                    }
                    BigDecimal dataDouble = doubles.divide(new BigDecimal(2));
                    dataDouble.setScale(2, BigDecimal.ROUND_HALF_UP);
                    MSTRecordDataInfo recordInfo = new MSTRecordDataInfo(num, datetime, dataDouble + "");

                    //一条记录
                    recordBaseInfos.add(recordInfo);

                    stringList.clear();
                } else {
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
                    byte18 = buf.get();
                    byte19 = buf.get();
                    byte20 = buf.get();
                    stringList.add(bytesToHex(new byte[]{byte01}));
                    stringList.add(bytesToHex(new byte[]{byte02}));
                    stringList.add(bytesToHex(new byte[]{byte03}));
                    stringList.add(bytesToHex(new byte[]{byte04}));
                    stringList.add(bytesToHex(new byte[]{byte05}));
                    stringList.add(bytesToHex(new byte[]{byte06}));
                    stringList.add(bytesToHex(new byte[]{byte07}));
                    stringList.add(bytesToHex(new byte[]{byte08}));
                    stringList.add(bytesToHex(new byte[]{byte09}));
                    stringList.add(bytesToHex(new byte[]{byte10}));
                    stringList.add(bytesToHex(new byte[]{byte11}));
                    stringList.add(bytesToHex(new byte[]{byte12}));
                    stringList.add(bytesToHex(new byte[]{byte13}));
                    stringList.add(bytesToHex(new byte[]{byte14}));
                    stringList.add(bytesToHex(new byte[]{byte15}));
                    stringList.add(bytesToHex(new byte[]{byte16}));
                    stringList.add(bytesToHex(new byte[]{byte17}));
                    stringList.add(bytesToHex(new byte[]{byte18}));
                    stringList.add(bytesToHex(new byte[]{byte19}));
                    stringList.add(bytesToHex(new byte[]{byte20}));
                }
            }

            //读取系统运行状态数据
            if (byteValue1 == null) {
                if (TextUtils.equals("00", bytesToHex(new byte[]{byte05}))) {
                    byteValue1 = value;
                }
            } else {
                if (byteValue2 == null) {
                    byteValue2 = value;
                    return;
                } else if (byteValue3 == null) {
                    byteValue3 = value;
                    return;
                } else if (byteValue4 == null) {
                    byteValue4 = value;
                }

                List<String> list = new ArrayList<>();
                buf = ByteBuffer.wrap(byteValue1);
                for (int i = 0; i < 20; i++) {
                    list.add(bytesToHex(new byte[]{buf.get()}));
                }
                buf = ByteBuffer.wrap(byteValue2);
                for (int i = 0; i < 20; i++) {
                    list.add(bytesToHex(new byte[]{buf.get()}));
                }
                buf = ByteBuffer.wrap(byteValue3);
                for (int i = 0; i < 20; i++) {
                    list.add(bytesToHex(new byte[]{buf.get()}));
                }
                buf = ByteBuffer.wrap(byteValue4);
                for (int i = 0; i < 20; i++) {
                    list.add(bytesToHex(new byte[]{buf.get()}));
                }
                byteValue1 = null;
                byteValue2 = null;
                byteValue3 = null;
                byteValue4 = null;
                if (callBack != null) {
                    callBack.onStateData(list);
                }
            }
        }
    };

    private byte[] byteValue1;
    private byte[] byteValue2;
    private byte[] byteValue3;
    private byte[] byteValue4;

    private void initData() {
        byteValue1 = null;
        byteValue2 = null;
        byteValue3 = null;
        byteValue4 = null;
        stringList.clear();
        recordBigInfos.clear();
        recordBaseInfos.clear();
        recordErrorInfos.clear();
        recordSunInfos.clear();
    }

    private final List<MSTRecordDataInfo> recordBigInfos = new ArrayList<>();
    private final List<MSTRecordDataInfo> recordErrorInfos = new ArrayList<>();
    private final List<MSTRecordDataInfo> recordSunInfos = new ArrayList<>();
    public final List<MSTRecordDataInfo> recordBaseInfos = new ArrayList<>();

    private final List<String> stringList = new ArrayList<>();

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

    public static int hexToInt(String hex) {
        int result = 0;
        for (int i = 0; i < hex.length(); i++) {
            int digit = Character.digit(hex.charAt(i), 16);
            result = result * 16 + digit;
        }
        return result;
    }


    public interface onDataCallBack {
        void onDisConnect();

        //基础信息
        void onStateData(List<String> list);

        //大剂量
        void onBigRecord(List<MSTRecordDataInfo> bigRecordInfos);

        //日总量
        void onSunRecord(List<MSTRecordDataInfo> sunRecordInfos);

        //警示数据
        void onErrorRecord(List<MSTRecordDataInfo> errorRecordInfos);

        //基础率数据
        void onBaseRecord(List<MSTRecordDataInfo> baseRecordInfo);

    }


    public void disConnect() {
        bluetoothGatt.disconnect();
    }


    /**
     * 泵表编号转hex
     *
     * @return
     */
    private String pumpToHex() {
        //保存起来的泵编号  11位
        String num = MySPUtils.getString(context, MySPUtils.DEVICE_NUM);
        String pumpNum = "";
        for (int i = 0; i < num.length(); i++) {
            pumpNum = pumpNum + "3" + num.charAt(i) + " ";
        }
        pumpNum = pumpNum + "00";
        return pumpNum;
    }

    public String comlpeteInstruct(String data) {
        data = data + pumpToHex();
        String[] s = data.split(" ");
        byte[] a = new byte[s.length];

        for (int i = 0; i < s.length; i++) {
            a[i] = (byte) Integer.parseInt(s[i], 16);
        }
        String crcStr = Integer.toHexString(calcCrc16(a));
        System.out.println(crcStr);
        String crc = "";
        if (crcStr.length() == 4) {
            crc = crcStr.charAt(2) + String.valueOf(crcStr.charAt(3)) + " " + crcStr.charAt(0) + crcStr.charAt(1);
        }
        crc = crc.toUpperCase();
        return data + " " + crc;
    }


    /**
     * 计算CRC16校验
     *
     * @param data
     * 需要计算的数组
     * @return CRC16校验值
     */
    private byte[] crc16_tab_h = {(byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40};

    private byte[] crc16_tab_l = {(byte) 0x00, (byte) 0xC0, (byte) 0xC1, (byte) 0x01, (byte) 0xC3, (byte) 0x03, (byte) 0x02, (byte) 0xC2, (byte) 0xC6, (byte) 0x06, (byte) 0x07, (byte) 0xC7, (byte) 0x05, (byte) 0xC5, (byte) 0xC4, (byte) 0x04, (byte) 0xCC, (byte) 0x0C, (byte) 0x0D, (byte) 0xCD, (byte) 0x0F, (byte) 0xCF, (byte) 0xCE, (byte) 0x0E, (byte) 0x0A, (byte) 0xCA, (byte) 0xCB, (byte) 0x0B, (byte) 0xC9, (byte) 0x09, (byte) 0x08, (byte) 0xC8, (byte) 0xD8, (byte) 0x18, (byte) 0x19, (byte) 0xD9, (byte) 0x1B, (byte) 0xDB, (byte) 0xDA, (byte) 0x1A, (byte) 0x1E, (byte) 0xDE, (byte) 0xDF, (byte) 0x1F, (byte) 0xDD, (byte) 0x1D, (byte) 0x1C, (byte) 0xDC, (byte) 0x14, (byte) 0xD4, (byte) 0xD5, (byte) 0x15, (byte) 0xD7, (byte) 0x17, (byte) 0x16, (byte) 0xD6, (byte) 0xD2, (byte) 0x12,
            (byte) 0x13, (byte) 0xD3, (byte) 0x11, (byte) 0xD1, (byte) 0xD0, (byte) 0x10, (byte) 0xF0, (byte) 0x30, (byte) 0x31, (byte) 0xF1, (byte) 0x33, (byte) 0xF3, (byte) 0xF2, (byte) 0x32, (byte) 0x36, (byte) 0xF6, (byte) 0xF7, (byte) 0x37, (byte) 0xF5, (byte) 0x35, (byte) 0x34, (byte) 0xF4, (byte) 0x3C, (byte) 0xFC, (byte) 0xFD, (byte) 0x3D, (byte) 0xFF, (byte) 0x3F, (byte) 0x3E, (byte) 0xFE, (byte) 0xFA, (byte) 0x3A, (byte) 0x3B, (byte) 0xFB, (byte) 0x39, (byte) 0xF9, (byte) 0xF8, (byte) 0x38, (byte) 0x28, (byte) 0xE8, (byte) 0xE9, (byte) 0x29, (byte) 0xEB, (byte) 0x2B, (byte) 0x2A, (byte) 0xEA, (byte) 0xEE, (byte) 0x2E, (byte) 0x2F, (byte) 0xEF, (byte) 0x2D, (byte) 0xED, (byte) 0xEC, (byte) 0x2C, (byte) 0xE4, (byte) 0x24, (byte) 0x25, (byte) 0xE5, (byte) 0x27, (byte) 0xE7,
            (byte) 0xE6, (byte) 0x26, (byte) 0x22, (byte) 0xE2, (byte) 0xE3, (byte) 0x23, (byte) 0xE1, (byte) 0x21, (byte) 0x20, (byte) 0xE0, (byte) 0xA0, (byte) 0x60, (byte) 0x61, (byte) 0xA1, (byte) 0x63, (byte) 0xA3, (byte) 0xA2, (byte) 0x62, (byte) 0x66, (byte) 0xA6, (byte) 0xA7, (byte) 0x67, (byte) 0xA5, (byte) 0x65, (byte) 0x64, (byte) 0xA4, (byte) 0x6C, (byte) 0xAC, (byte) 0xAD, (byte) 0x6D, (byte) 0xAF, (byte) 0x6F, (byte) 0x6E, (byte) 0xAE, (byte) 0xAA, (byte) 0x6A, (byte) 0x6B, (byte) 0xAB, (byte) 0x69, (byte) 0xA9, (byte) 0xA8, (byte) 0x68, (byte) 0x78, (byte) 0xB8, (byte) 0xB9, (byte) 0x79, (byte) 0xBB, (byte) 0x7B, (byte) 0x7A, (byte) 0xBA, (byte) 0xBE, (byte) 0x7E, (byte) 0x7F, (byte) 0xBF, (byte) 0x7D, (byte) 0xBD, (byte) 0xBC, (byte) 0x7C, (byte) 0xB4, (byte) 0x74,
            (byte) 0x75, (byte) 0xB5, (byte) 0x77, (byte) 0xB7, (byte) 0xB6, (byte) 0x76, (byte) 0x72, (byte) 0xB2, (byte) 0xB3, (byte) 0x73, (byte) 0xB1, (byte) 0x71, (byte) 0x70, (byte) 0xB0, (byte) 0x50, (byte) 0x90, (byte) 0x91, (byte) 0x51, (byte) 0x93, (byte) 0x53, (byte) 0x52, (byte) 0x92, (byte) 0x96, (byte) 0x56, (byte) 0x57, (byte) 0x97, (byte) 0x55, (byte) 0x95, (byte) 0x94, (byte) 0x54, (byte) 0x9C, (byte) 0x5C, (byte) 0x5D, (byte) 0x9D, (byte) 0x5F, (byte) 0x9F, (byte) 0x9E, (byte) 0x5E, (byte) 0x5A, (byte) 0x9A, (byte) 0x9B, (byte) 0x5B, (byte) 0x99, (byte) 0x59, (byte) 0x58, (byte) 0x98, (byte) 0x88, (byte) 0x48, (byte) 0x49, (byte) 0x89, (byte) 0x4B, (byte) 0x8B, (byte) 0x8A, (byte) 0x4A, (byte) 0x4E, (byte) 0x8E, (byte) 0x8F, (byte) 0x4F, (byte) 0x8D, (byte) 0x4D,
            (byte) 0x4C, (byte) 0x8C, (byte) 0x44, (byte) 0x84, (byte) 0x85, (byte) 0x45, (byte) 0x87, (byte) 0x47, (byte) 0x46, (byte) 0x86, (byte) 0x82, (byte) 0x42, (byte) 0x43, (byte) 0x83, (byte) 0x41, (byte) 0x81, (byte) 0x80, (byte) 0x40};

    public int calcCrc16(byte[] data) {
        return calcCrc16(data, 0, data.length);
    }

    /**
     * 计算CRC16校验
     *
     * @param data   需要计算的数组
     * @param offset 起始位置
     * @param len    长度
     * @return CRC16校验值
     */
    public int calcCrc16(byte[] data, int offset, int len) {
        return calcCrc16(data, offset, len, 0xffff);
    }

    /**
     * 计算CRC16校验
     *
     * @param data   需要计算的数组
     * @param offset 起始位置
     * @param len    长度
     * @param preval 之前的校验值
     * @return CRC16校验值
     */
    public int calcCrc16(byte[] data, int offset, int len, int preval) {
        int ucCRCHi = (preval & 0xff00) >> 8;
        int ucCRCLo = preval & 0x00ff;
        int iIndex;
        for (int i = 0; i < len; ++i) {
            iIndex = (ucCRCLo ^ data[offset + i]) & 0x00ff;
            ucCRCLo = ucCRCHi ^ crc16_tab_h[iIndex];
            ucCRCHi = crc16_tab_l[iIndex];
        }
        return ((ucCRCHi & 0x00ff) << 8) | (ucCRCLo & 0x00ff) & 0xffff;
    }
}
