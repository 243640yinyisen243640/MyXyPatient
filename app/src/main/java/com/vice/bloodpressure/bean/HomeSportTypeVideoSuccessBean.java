package com.vice.bloodpressure.bean;

public class HomeSportTypeVideoSuccessBean {

    /**
     * kcaling : 0
     * sportTime : 00:00:04
     * sportName : 太极拳
     */

    private int kcaling;
    private String sportTime;
    private String sportName;
    private String sportPicUrl;
    private String sportContent;

    public String getSportPicUrl() {
        return sportPicUrl;
    }

    public void setSportPicUrl(String sportPicUrl) {
        this.sportPicUrl = sportPicUrl;
    }

    public String getSportContent() {
        return sportContent;
    }

    public void setSportContent(String sportContent) {
        this.sportContent = sportContent;
    }

    public int getKcaling() {
        return kcaling;
    }

    public void setKcaling(int kcaling) {
        this.kcaling = kcaling;
    }

    public String getSportTime() {
        return sportTime;
    }

    public void setSportTime(String sportTime) {
        this.sportTime = sportTime;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }
}
