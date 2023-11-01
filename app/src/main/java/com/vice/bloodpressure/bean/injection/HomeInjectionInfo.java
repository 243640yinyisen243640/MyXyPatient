package com.vice.bloodpressure.bean.injection;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class HomeInjectionInfo {
    /**
     * 第一针 1高 2低 3正常 0无
     */
    private String one;
    private String two;
    private String three;
    private String four;
    /**
     * 最新测量第几针
     */
    private String times;
    /**
     * 测量值
     */
    private String value;
    /**
     * 测量时间
     */
    private String time;

    private String plan_num;

    public String getPlan_num() {
        return plan_num;
    }

    public void setPlan_num(String plan_num) {
        this.plan_num = plan_num;
    }

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public String getThree() {
        return three;
    }

    public void setThree(String three) {
        this.three = three;
    }

    public String getFour() {
        return four;
    }

    public void setFour(String four) {
        this.four = four;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
