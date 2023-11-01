package com.vice.bloodpressure.event;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class BlueUnbindEvent {
    private boolean isUnBind;

    public BlueUnbindEvent(boolean isUnBind) {
        this.isUnBind = isUnBind;
    }

    public boolean isUnBind() {
        return isUnBind;
    }

    public void setUnBind(boolean unBind) {
        isUnBind = unBind;
    }
}
