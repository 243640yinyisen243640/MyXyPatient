package com.vice.bloodpressure.event;

import java.util.List;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class BlueHistoryDataEvent {
    //1:历史消息  2：当前消息
    private int type;

    private insulis insuli;

    private List<insulis> insulis;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BlueHistoryDataEvent.insulis getInsuli() {
        return insuli;
    }

    public void setInsuli(BlueHistoryDataEvent.insulis insuli) {
        this.insuli = insuli;
    }

    public BlueHistoryDataEvent(int type, BlueHistoryDataEvent.insulis insuli) {
        this.type = type;
        this.insuli = insuli;
    }

    public BlueHistoryDataEvent(List<BlueHistoryDataEvent.insulis> insulis, int type) {
        this.insulis = insulis;
        this.type = type;
    }

    public List<BlueHistoryDataEvent.insulis> getInsulis() {
        return insulis;
    }

    public void setInsulis(List<BlueHistoryDataEvent.insulis> insulis) {
        this.insulis = insulis;
    }

    public static class insulis {
        private String datetime;
        private String value;

        public insulis(String datetime, String value) {
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


}
