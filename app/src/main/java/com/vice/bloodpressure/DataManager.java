package com.vice.bloodpressure;

import com.vice.bloodpressure.bean.AdverInfo;
import com.vice.bloodpressure.constant.ConstantParam;
import com.vice.bloodpressure.retrofit.BaseNetworkUtils;
import com.vice.bloodpressure.retrofit.HHSoftBaseResponse;

import java.util.HashMap;

import io.reactivex.functions.BiConsumer;
import retrofit2.Call;

public class DataManager {
    public static Call<String> getAdver(String accessToken, String type, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("access_token", accessToken);
        map.put("version", ConstantParam.SERVER_VERSION);
        return BaseNetworkUtils.getRequest(false, BaseNetworkUtils.JSON_OBJECT, AdverInfo.class, "port/advertising/getBulletAdv", map, successCallBack, failureCallBack);
    }
    /**
     * 上传温度数据
     * @param accessToken
     * @param uid
     * @param temperature
     * @param time
     * @param docId
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> saveDataTemperature(String accessToken,String uid,String temperature,String time,String docId,String type, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token", accessToken);
        map.put("uid", uid);
        map.put("temperature", temperature);
        map.put("datetime", time);
        //上传类型 1自动 2手动
        map.put("type", type);
        map.put("docuserid",docId);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.NONE, null, "/port/record/addTemperatureData", map, successCallBack, failureCallBack);
    }
}