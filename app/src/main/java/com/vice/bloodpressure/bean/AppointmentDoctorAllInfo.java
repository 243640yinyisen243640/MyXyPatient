package com.vice.bloodpressure.bean;


import java.util.List;

public class AppointmentDoctorAllInfo {
    private List<String> time;
    private List<AppointmentDoctorListBean> list;

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public List<AppointmentDoctorListBean> getList() {
        return list;
    }

    public void setList(List<AppointmentDoctorListBean> list) {
        this.list = list;
    }
}
