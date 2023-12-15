package com.vice.bloodpressure.event;

import com.vice.bloodpressure.bean.insulin.MSTRecordDataInfo;

import java.util.List;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class MSTBlueEventBus {
    //1 基础信息
    //2 大剂量
    //3 日总量
    //4 警示数据
    //5 基础率数据
    private int type;
    private List<MSTRecordDataInfo> recordDataInfoList;
    private List<String> stringList;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<MSTRecordDataInfo> getRecordDataInfoList() {
        return recordDataInfoList;
    }

    public void setRecordDataInfoList(List<MSTRecordDataInfo> recordDataInfoList) {
        this.recordDataInfoList = recordDataInfoList;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }
}
