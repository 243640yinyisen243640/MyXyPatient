package com.vice.bloodpressure.bean.injection;

/**
 * Author: LYD
 * Date: 2021/9/8 18:34
 * Description:
 */
public class InjectionBaseData {
    private int value;
    private int ishight;
    private int times;
    private int isshot;
    private int isshot_num;
    private int all_times;
    private String drug_name;
    private String action_year;
    private String action_time;

    @Override
    public String toString() {
        return "InjectionBaseData{" +
                "value='" + value + '\'' +
                ", ishight='" + ishight + '\'' +
                ", times='" + times + '\'' +
                ", isshot='" + isshot + '\'' +
                ", isshot_num='" + isshot_num + '\'' +
                ", all_times='" + all_times + '\'' +
                ", drug_name='" + drug_name + '\'' +
                ", action_year='" + action_year + '\'' +
                ", action_time='" + action_time + '\'' +
                '}';
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getIshight() {
        return ishight;
    }

    public void setIshight(int ishight) {
        this.ishight = ishight;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getIsshot() {
        return isshot;
    }

    public void setIsshot(int isshot) {
        this.isshot = isshot;
    }

    public int getIsshot_num() {
        return isshot_num;
    }

    public void setIsshot_num(int isshot_num) {
        this.isshot_num = isshot_num;
    }

    public int getAll_times() {
        return all_times;
    }

    public void setAll_times(int all_times) {
        this.all_times = all_times;
    }

    public String getDrug_name() {
        return drug_name;
    }

    public void setDrug_name(String drug_name) {
        this.drug_name = drug_name;
    }

    public String getAction_year() {
        return action_year;
    }

    public void setAction_year(String action_year) {
        this.action_year = action_year;
    }

    public String getAction_time() {
        return action_time;
    }

    public void setAction_time(String action_time) {
        this.action_time = action_time;
    }
}
