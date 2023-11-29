package com.vice.bloodpressure.bean.insulin;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class InsulinDeviceInfo {
    //设备号
    private String eqcode;
    //设备号
    private String eq_code;
    //电量
    private String power;
    //药量
    private String dosage;
    //开关
    private String status;
    //worning
    private String worning;
    //基础模式类型 1 2
    private String model;
    //基础率
    private String base_rate;
    //已注射总量
    private String value;
    //更新时间
    private String updatetime;
    //时间
    private String datetime;

    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getEq_code() {
        return eq_code;
    }

    public void setEq_code(String eq_code) {
        this.eq_code = eq_code;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getEqcode() {
        return eqcode;
    }

    public void setEqcode(String eqcode) {
        this.eqcode = eqcode;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorning() {
        return worning;
    }

    public void setWorning(String worning) {
        this.worning = worning;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBase_rate() {
        return base_rate;
    }

    public void setBase_rate(String base_rate) {
        this.base_rate = base_rate;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
