package com.vice.bloodpressure;

import com.vice.bloodpressure.bean.AdverInfo;
import com.vice.bloodpressure.bean.DietPlanAddSuccessBean;
import com.vice.bloodpressure.bean.PhysicalExaminationDoctorInfoAllInfo;
import com.vice.bloodpressure.bean.ScheduleInfoBean;
import com.vice.bloodpressure.bean.injection.DrugListInfo;
import com.vice.bloodpressure.bean.injection.InjectionBaseData;
import com.vice.bloodpressure.bean.injection.InjectionDataDetail;
import com.vice.bloodpressure.bean.injection.InjectionDataListInfo;
import com.vice.bloodpressure.bean.injection.InjectionHistoryInfo;
import com.vice.bloodpressure.bean.injection.PlanNumInfo;
import com.vice.bloodpressure.constant.ConstantParam;
import com.vice.bloodpressure.retrofit.BaseNetworkUtils;
import com.vice.bloodpressure.retrofit.HHSoftBaseResponse;

import java.util.HashMap;
import java.util.Map;

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
     *
     * @param accessToken
     * @param uid
     * @param temperature
     * @param time
     * @param docId
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> saveDataTemperature(String accessToken, String uid, String temperature, String time, String docId, String type, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token", accessToken);
        map.put("uid", uid);
        map.put("temperature", temperature);
        map.put("datetime", time);
        //上传类型 1自动 2手动
        map.put("type", type);
        map.put("docuserid", docId);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.NONE, null, "/port/record/addTemperatureData", map, successCallBack, failureCallBack);
    }

    /**
     * 一键已读
     *
     * @param accessToken
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> readMessage(String accessToken, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token", accessToken);
        map.put("version", ConstantParam.SERVER_VERSION);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.NONE, null, "/port/Message/allReading", map, successCallBack, failureCallBack);
    }

    /**
     * 领取优惠券
     *
     * @param accessToken
     * @param coupon_id
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> receiveCoupin(String accessToken, String coupon_id, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token", accessToken);
        map.put("coupon_id", coupon_id);
        map.put("version", ConstantParam.SERVER_VERSION);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.NONE, null, "/nsgetcoupons", map, successCallBack, failureCallBack);
    }

    /**
     * 举报
     *
     * @param accessToken
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> reportDoctor(String accessToken, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token", accessToken);
        map.put("version", ConstantParam.SERVER_VERSION);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.NONE, null, "/index/Myinfo/toReport", map, successCallBack, failureCallBack);
    }

    /**
     * @param doctorid
     * @param accessToken
     * @param successCallBack
     * @param failureCallBack
     * @return
     */

    public static Call<String> getAppDataInfo(String doctorid, String accessToken, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        HashMap<String, String> map = new HashMap<>();
        map.put("docuserid", doctorid);
        map.put("access_token", accessToken);
        map.put("version", ConstantParam.SERVER_VERSION);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_OBJECT, PhysicalExaminationDoctorInfoAllInfo.class, "/scheduling/Hospitals/scheduleDetail", map, successCallBack, failureCallBack);
    }

    /**
     * @param sid
     * @param schday
     * @param accessToken
     * @param type
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> getCheckData1(String sid, String schday, String accessToken, String type, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        HashMap<String, String> map = new HashMap<>();
        map.put("sid", sid);
        map.put("schday", schday);
        map.put("type", type);
        map.put("access_token", accessToken);
        map.put("version", ConstantParam.SERVER_VERSION);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_OBJECT, ScheduleInfoBean.class, "/scheduling/Schedule/scheduleInfo", map, successCallBack, failureCallBack);
    }

    /**
     * @param glucosevalue
     * @param category
     * @param datetime
     * @param accessToken
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> saveXuetang(String glucosevalue, String category, String datetime, String accessToken, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        HashMap<String, String> map = new HashMap<>();
        map.put("glucosevalue", glucosevalue);
        map.put("category", category);
        map.put("datetime", datetime);
        map.put("access_token", accessToken);
        map.put("version", ConstantParam.SERVER_VERSION);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_OBJECT, ScheduleInfoBean.class, "/port/record/addbloodglucose", map, successCallBack, failureCallBack);
    }


    public static Call<String> dietPlanAdd(String sex, String height, String weight, String profession, String dn, String dn_type, String accessToken, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        HashMap<String, String> map = new HashMap<>();
        map.put("sex", sex);
        map.put("height", height);
        map.put("weight", weight);
        map.put("profession", profession);
        map.put("dn", dn);
        map.put("dn_type", dn_type);
        map.put("access_token", accessToken);
        map.put("version", ConstantParam.SERVER_VERSION);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_OBJECT, DietPlanAddSuccessBean.class, "/port/Food/dietPlanAdd", map, successCallBack, failureCallBack);
    }

    public static Call<String> getInjectionBaseInfo(String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        //        map.put("userid", userid);
        map.put("access_token", token);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_OBJECT, InjectionBaseData.class, "/userInsuliInfo", map, successCallBack, failureCallBack);
    }

    public static Call<String> getInjectionList(String beginTime,String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        //        map.put("userid", userid);
        map.put("access_token", token);
        map.put("begin_time", beginTime);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_ARRAY, InjectionDataListInfo.class, "/insulinList", map, successCallBack, failureCallBack);
    }

    public static Call<String> getInjectionDetail(String action_time, String isuse,String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        //        map.put("userid", userid);
        map.put("access_token", token);
        map.put("action_time", action_time);
        map.put("isuse", isuse);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_OBJECT, InjectionDataDetail.class, "/planDetail", map, successCallBack, failureCallBack);
    }

    public static Call<String> getInjectionHistoryList(int page, String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        //        map.put("userid", userid);
        map.put("access_token", token);
        map.put("page", page + "");
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_ARRAY, InjectionHistoryInfo.class, "/planList", map, successCallBack, failureCallBack);
    }


    public static Call<String> getPlanNum(String datetime, String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("datetime", datetime);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_OBJECT, PlanNumInfo.class, "/getPlanNum", map, successCallBack, failureCallBack);
    }

    public static Call<String> editInsulin(String value,String jection_id, String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("value", value);
        map.put("jection_id", jection_id);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.NONE, null, "/editInsulin", map, successCallBack, failureCallBack);
    }

    public static Call<String> addInsulin(String type,String times,String value,String datetime, String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("datetime", datetime);
        map.put("value", value);
        map.put("times", times);
        map.put("type", type);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.NONE, null, "/addInsulin", map, successCallBack, failureCallBack);
    }

    public static Call<String> getDrugs(String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_ARRAY, DrugListInfo.class , "/getDrugs", map, successCallBack, failureCallBack);
    }


    public static Call<String> addPlan(String token,String plan_name,String plan, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("plan_name", plan_name);
        map.put("plan", plan);
        map.put("access_token", token);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.NONE, null , "/addPlan", map, successCallBack, failureCallBack);
    }


    public static Call<String> bindInsulin(String mac,String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("mac", mac);
        map.put("access_token", token);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.NONE, null , "/bindInsulin", map, successCallBack, failureCallBack);
    }


    public static Call<String> unbindInsulin(String mac,String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("mac", mac);
        map.put("access_token", token);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.NONE, null , "/unbindInsulin", map, successCallBack, failureCallBack);
    }
}
