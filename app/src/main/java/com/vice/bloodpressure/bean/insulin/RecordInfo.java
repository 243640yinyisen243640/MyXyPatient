package com.vice.bloodpressure.bean.insulin;

/**
 * 日总量记录   基础量记录
 */
public class RecordInfo {
    private String month;
    private String date;
    private byte dataHeight;
    private byte dataLow;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public byte getDataHeight() {
        return dataHeight;
    }

    public void setDataHeight(byte dataHeight) {
        this.dataHeight = dataHeight;
    }

    public byte getDataLow() {
        return dataLow;
    }

    public void setDataLow(byte dataLow) {
        this.dataLow = dataLow;
    }
}