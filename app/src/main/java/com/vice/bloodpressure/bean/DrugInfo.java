package com.vice.bloodpressure.bean;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class DrugInfo {
    private String name;
    private boolean isCheck;

    public DrugInfo(String name) {
        this.name = name;
        isCheck = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
