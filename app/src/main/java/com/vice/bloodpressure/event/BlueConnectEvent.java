package com.vice.bloodpressure.event;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class BlueConnectEvent {
    private boolean isConnect;

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }

    public BlueConnectEvent(boolean isBind) {
        this.isConnect = isBind;
    }
}
