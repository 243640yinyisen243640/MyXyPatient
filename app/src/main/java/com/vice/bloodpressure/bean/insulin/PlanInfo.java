package com.vice.bloodpressure.bean.insulin;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class PlanInfo {
    /**
     * 方案未读数 大剂量
     */
    private String big;
    /**
     * 方案未读数 基础率
     */
    private String base_rate;


    private String plan_id;
    /**
     * 1已读 2未读
     */
    private String isread;
    /**
     * 下发时间
     */
    private String addtime;

    /**
     *
     剂量值
     */
    private String value;


    private String big1;
    private String big2;
    private String big3;
    private String big4;
    private String begin;
    private String end;

    public String getBig1() {
        return big1;
    }

    public void setBig1(String big1) {
        this.big1 = big1;
    }

    public String getBig2() {
        return big2;
    }

    public void setBig2(String big2) {
        this.big2 = big2;
    }

    public String getBig3() {
        return big3;
    }

    public void setBig3(String big3) {
        this.big3 = big3;
    }

    public String getBig4() {
        return big4;
    }

    public void setBig4(String big4) {
        this.big4 = big4;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getBig() {
        return big;
    }

    public void setBig(String big) {
        this.big = big;
    }

    public String getBase_rate() {
        return base_rate;
    }

    public void setBase_rate(String base_rate) {
        this.base_rate = base_rate;
    }
}
