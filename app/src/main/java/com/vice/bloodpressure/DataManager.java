package com.vice.bloodpressure;

import com.vice.bloodpressure.bean.AdverInfo;
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
        return BaseNetworkUtils.getRequest(false, BaseNetworkUtils.JSON_OBJECT, AdverInfo.class, "port/advertising/getBulletAdv", map, successCallBack, failureCallBack);
    }
}
