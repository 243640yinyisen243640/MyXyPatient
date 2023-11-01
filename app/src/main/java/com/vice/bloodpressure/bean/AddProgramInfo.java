package com.vice.bloodpressure.bean;

import java.io.Serializable;
import java.util.List;

public class AddProgramInfo implements Serializable {
    private String plan_name;
    private List<plan> planList;

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }


    public List<plan> getPlanList() {
        return planList;
    }

    public void setPlanList(List<plan> planList) {
        this.planList = planList;
    }

    public AddProgramInfo(String plan_name, List<plan> planList) {
        this.plan_name = plan_name;
        this.planList = planList;
    }

    public static class plan implements Serializable{
        private String plan_time;
        private String drug_name;
        private String value;

        public plan(String plan_time, String drug_name, String value) {
            this.plan_time = plan_time;
            this.drug_name = drug_name;
            this.value = value;
        }

        public String getPlan_time() {
            return plan_time;
        }

        public void setPlan_time(String plan_time) {
            this.plan_time = plan_time;
        }

        public String getDrug_name() {
            return drug_name;
        }

        public void setDrug_name(String drug_name) {
            this.drug_name = drug_name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
