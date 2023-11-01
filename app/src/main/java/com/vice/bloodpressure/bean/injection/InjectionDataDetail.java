package com.vice.bloodpressure.bean.injection;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class InjectionDataDetail implements Serializable {
    private String plan_name;
    private String action_time;
    private int action_day;
    private List<Detail>detail;

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public String getAction_time() {
        return action_time;
    }

    public void setAction_time(String action_time) {
        this.action_time = action_time;
    }

    public int getAction_day() {
        return action_day;
    }

    public void setAction_day(int action_day) {
        this.action_day = action_day;
    }

    public List<Detail> getDetail() {
        return detail;
    }

    public void setDetail(List<Detail> detail) {
        this.detail = detail;
    }

    public class Detail{
        private String begin;
        private String end;
        private String drug_name;
        private int times;
        private int value;

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

        public String getDrug_name() {
            return drug_name;
        }

        public void setDrug_name(String drug_name) {
            this.drug_name = drug_name;
        }

        public int getTimes() {
            return times;
        }

        public void setTimes(int times) {
            this.times = times;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
