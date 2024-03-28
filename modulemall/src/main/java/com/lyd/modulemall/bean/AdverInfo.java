package com.lyd.modulemall.bean;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class AdverInfo {
    /**
     * 医生发送的消息 1有 0无
     */
    private String docMsg;
    /**
     * 客服发送的消息 1有 0无
     */
    private String kfMsg;

    public String getDocMsg() {
        return docMsg;
    }

    public void setDocMsg(String docMsg) {
        this.docMsg = docMsg;
    }

    public String getKfMsg() {
        return kfMsg;
    }

    public void setKfMsg(String kfMsg) {
        this.kfMsg = kfMsg;
    }
}
