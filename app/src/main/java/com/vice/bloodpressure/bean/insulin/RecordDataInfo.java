package com.vice.bloodpressure.bean.insulin;

/**
 * 日总量记录   基础量记录
 */
public class RecordDataInfo {
    private String datetime;
    private String value;

    public RecordDataInfo(String datetime, String value) {
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
}