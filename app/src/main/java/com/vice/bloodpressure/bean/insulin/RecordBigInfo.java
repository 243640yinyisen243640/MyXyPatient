package com.vice.bloodpressure.bean.insulin;

/**
 * 大剂量记录
 */
public class RecordBigInfo {
    private String month;
    private String date;
    private String hour;
    private String minute;
    private String type;
    private String times;
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

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
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