package com.vice.bloodpressure.bean;

import java.util.List;

public class PhysicalExaminationDoctorInfoAllInfo {

    private List<PhysicalExaminationDoctorInfoListBean> list;
    private List<String> time;

    public List<PhysicalExaminationDoctorInfoListBean> getList() {
        return list;
    }

    public void setList(List<PhysicalExaminationDoctorInfoListBean> list) {
        this.list = list;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }
}
