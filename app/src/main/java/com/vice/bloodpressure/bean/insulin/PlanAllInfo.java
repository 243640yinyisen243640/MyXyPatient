package com.vice.bloodpressure.bean.insulin;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class PlanAllInfo {
    private PlanInfo data;
    private String picurl;
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

    public PlanInfo getData() {
        return data;
    }

    public void setData(PlanInfo data) {
        this.data = data;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }
}
