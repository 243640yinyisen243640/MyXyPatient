package com.vice.bloodpressure.bean.insulin;

import java.util.List;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class PlanAllBaseInfo {
    private List<PlanInfo> data;
    /**
     * 方案确认 1是 2否
     */
    private String confirm;

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public List<PlanInfo> getData() {
        return data;
    }

    public void setData(List<PlanInfo> data) {
        this.data = data;
    }
}
