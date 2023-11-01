package com.vice.bloodpressure.event;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class BlueBindEvent {
    private boolean isBind;

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }

    public BlueBindEvent(boolean isBind) {
        this.isBind = isBind;
    }
}
