package com.vice.bloodpressure.bean;

import android.bluetooth.BluetoothDevice;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class BlueInfo {
    private BluetoothDevice device;
    private boolean isCheck;

    public BlueInfo(BluetoothDevice device) {
        this.device = device;
        isCheck = false;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
