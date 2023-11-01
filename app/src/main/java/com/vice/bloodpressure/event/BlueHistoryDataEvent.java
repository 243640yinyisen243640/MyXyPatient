package com.vice.bloodpressure.event;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class BlueHistoryDataEvent {
    private String historyData;

    public BlueHistoryDataEvent(String historyData) {
        this.historyData = historyData;
    }

    public String getHistoryData() {
        return historyData;
    }

    public void setHistoryData(String historyData) {
        this.historyData = historyData;
    }
}
