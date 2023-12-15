package com.vice.bloodpressure.bean.insulin;

/**
 * 日总量记录   基础量记录
 */
public class MSTRecordDataInfo {
    private String num;
    private String datetime;
    private String value;

    public MSTRecordDataInfo(String num,String datetime, String value) {
        this.num = num;
        this.datetime = datetime;
        this.value = value;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MSTRecordDataInfo{" +
                "num='" + num + '\'' +
                ", datetime='" + datetime + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}