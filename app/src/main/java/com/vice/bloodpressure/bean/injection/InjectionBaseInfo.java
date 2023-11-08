package com.vice.bloodpressure.bean.injection;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class InjectionBaseInfo {
    private String text;
    private int img;

    public InjectionBaseInfo(String text, int img) {
        this.text = text;
        this.img = img;
    }

    public InjectionBaseInfo(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
