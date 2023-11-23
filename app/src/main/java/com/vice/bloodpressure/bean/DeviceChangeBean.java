package com.vice.bloodpressure.bean;

public class DeviceChangeBean {
    /**
     * imei : 123456789
     */

    private String imei;
    private String snnum;
    /**
     * 快舒尔的设备码
     */
    private String insulinnum;
    /**
     * 胰岛素泵的设备码
     */
    private String eqinsulinnum;

    public String getEqinsulinnum() {
        return eqinsulinnum;
    }

    public void setEqinsulinnum(String eqinsulinnum) {
        this.eqinsulinnum = eqinsulinnum;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }


    public String getSnnum() {
        return snnum;
    }

    public void setSnnum(String snnum) {
        this.snnum = snnum;
    }

    public String getInsulinnum() {
        return insulinnum;
    }

    public void setInsulinnum(String insulinnum) {
        this.insulinnum = insulinnum;
    }
}
