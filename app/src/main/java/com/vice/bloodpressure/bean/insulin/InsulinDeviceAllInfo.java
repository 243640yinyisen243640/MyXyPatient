package com.vice.bloodpressure.bean.insulin;

import java.util.List;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class InsulinDeviceAllInfo {
  private List<InsulinDeviceInfo> type1;
  private List<InsulinDeviceInfo> type2;
  private String now_value;

    public List<InsulinDeviceInfo> getType1() {
        return type1;
    }

    public void setType1(List<InsulinDeviceInfo> type1) {
        this.type1 = type1;
    }

    public List<InsulinDeviceInfo> getType2() {
        return type2;
    }

    public void setType2(List<InsulinDeviceInfo> type2) {
        this.type2 = type2;
    }

    public String getNow_value() {
        return now_value;
    }

    public void setNow_value(String now_value) {
        this.now_value = now_value;
    }
}
