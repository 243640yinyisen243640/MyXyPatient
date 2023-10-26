package com.vice.bloodpressure.bean.injection;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class InjectionHistoryInfo {
    private String action_time;
    private int plan_id;
    private String plan_name;
    //1正在执行 2历史数据
    private int isuse;
    private int action_day;

    public String getAction_time() {
        return action_time;
    }

    public void setAction_time(String action_time) {
        this.action_time = action_time;
    }

    public int getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(int plan_id) {
        this.plan_id = plan_id;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public int getIsuse() {
        return isuse;
    }

    public void setIsuse(int isuse) {
        this.isuse = isuse;
    }

    public int getAction_day() {
        return action_day;
    }

    public void setAction_day(int action_day) {
        this.action_day = action_day;
    }
}
