package com.vice.bloodpressure.bean;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class BaseRateBean {
    private String time;
    private String value;

    public BaseRateBean(String time, String value) {
        this.time = time;
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
