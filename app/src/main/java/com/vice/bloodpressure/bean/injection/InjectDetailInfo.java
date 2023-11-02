package com.vice.bloodpressure.bean.injection;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class InjectDetailInfo implements Serializable {
    private String datetime;
    private String value;
    private String num;
    //值 1偏高 2偏低 3正常
    private String ishight;
    private String injectionNum;
    private List<InjectionDataListInfo.InjectionData> jection_data;

    public String getInjectionNum() {
        return injectionNum;
    }

    public void setInjectionNum(String injectionNum) {
        this.injectionNum = injectionNum;
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

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getIshight() {
        return ishight;
    }

    public void setIshight(String ishight) {
        this.ishight = ishight;
    }

    public List<InjectionDataListInfo.InjectionData> getJection_data() {
        return jection_data;
    }

    public void setJection_data(List<InjectionDataListInfo.InjectionData> jection_data) {
        this.jection_data = jection_data;
    }
}
