package com.vice.bloodpressure.bean.injection;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class InjectionDataListInfo implements Serializable {
    private int date;
    private String datetime;
    private int plan_num;

    public List<InjectionDataInfo> jections;

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getPlan_num() {
        return plan_num;
    }

    public void setPlan_num(int plan_num) {
        this.plan_num = plan_num;
    }

    public List<InjectionDataInfo> getJections() {
        return jections;
    }

    public void setJections(List<InjectionDataInfo> jections) {
        this.jections = jections;
    }

    public class InjectionDataInfo implements Serializable {

        private int value;
        private int more;
        private int ishight;
        private int type;
        private List<InjectionData> jection_data;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getMore() {
            return more;
        }

        public void setMore(int more) {
            this.more = more;
        }

        public int getIshight() {
            return ishight;
        }

        public void setIshight(int ishight) {
            this.ishight = ishight;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<InjectionData> getJection_data() {
            return jection_data;
        }

        public void setJection_data(List<InjectionData> jection_data) {
            this.jection_data = jection_data;
        }
    }

    public class InjectionData implements Serializable {
        private String value;
        private String date;
        private String time;
        private String type;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
