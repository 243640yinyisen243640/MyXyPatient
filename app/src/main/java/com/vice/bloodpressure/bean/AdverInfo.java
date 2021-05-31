package com.vice.bloodpressure.bean;

public class AdverInfo {
    private String id;
    private String title;
    private String img_url;
    /**
     * 跳转类型
     * 1：外部链接
     * 2：商品链接
     * 3：不跳转
     */
    private String type;
    private String rate;
    /**
     * 商品id，type=2时跳转商品详情
     */
    private int goods_id;
    /**
     * 跳转地址，type=1时跳转
     */
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
