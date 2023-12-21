package com.vice.bloodpressure;

import android.text.TextUtils;

import com.vice.bloodpressure.bean.AdverInfo;
import com.vice.bloodpressure.bean.DeviceChangeBean;
import com.vice.bloodpressure.bean.DietPlanAddSuccessBean;
import com.vice.bloodpressure.bean.PhysicalExaminationDoctorInfoAllInfo;
import com.vice.bloodpressure.bean.ScheduleInfoBean;
import com.vice.bloodpressure.bean.injection.DrugListInfo;
import com.vice.bloodpressure.bean.injection.InjectDetailInfo;
import com.vice.bloodpressure.bean.injection.InjectionBaseData;
import com.vice.bloodpressure.bean.injection.InjectionDataDetail;
import com.vice.bloodpressure.bean.injection.InjectionDataListInfo;
import com.vice.bloodpressure.bean.injection.InjectionHistoryInfo;
import com.vice.bloodpressure.bean.injection.PlanNumInfo;
import com.vice.bloodpressure.bean.insulin.InsulinDeviceAllInfo;
import com.vice.bloodpressure.bean.insulin.InsulinDeviceInfo;
import com.vice.bloodpressure.bean.insulin.PlanAllBaseInfo;
import com.vice.bloodpressure.bean.insulin.PlanAllInfo;
import com.vice.bloodpressure.bean.insulin.PlanInfo;
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

    /**
     * 用户注射数据基础信息
     *
     * @param token
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> getInjectionBaseInfo(String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_OBJECT, InjectionBaseData.class, "/userInsuliInfo", map, successCallBack, failureCallBack);
    }

    /**
     * 获取用户胰岛素注射数据列表
     *
     * @param beginTime
     * @param token
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> getInjectionList(String beginTime, String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("begin_time", beginTime);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_ARRAY, InjectionDataListInfo.class, "/insulinList", map, successCallBack, failureCallBack);
    }

    /**
     * 获取方案详情
     *
     * @param action_time
     * @param isuse
     * @param token
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> getInjectionDetail(String action_time, String isuse, String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("action_time", action_time);
        map.put("isuse", isuse);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_OBJECT, InjectionDataDetail.class, "/planDetail", map, successCallBack, failureCallBack);
    }

    /**
     * 获取方案列表
     *
     * @param page
     * @param token
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> getInjectionHistoryList(int page, String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("page", page + "");
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_ARRAY, InjectionHistoryInfo.class, "/planList", map, successCallBack, failureCallBack);
    }


    public static Call<String> getPlanNum(String date, String time, String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("datetime", date);
        map.put("time", time);
        if (!TextUtils.isEmpty(time)) {
        }
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_OBJECT, PlanNumInfo.class, "/getPlanNum", map, successCallBack, failureCallBack);
    }

    public static Call<String> editInsulin(String value, String jection_id, String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("value", value);
        map.put("jection_id", jection_id);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.NONE, null, "/editInsulin", map, successCallBack, failureCallBack);
    }

    public static Call<String> addInsulin(String type, String times, String value, String datetime, String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("datetime", datetime);
        map.put("value", value);
        if (!TextUtils.isEmpty(type)) {
            map.put("type", type);
        }
        if (!TextUtils.isEmpty(times)) {
            map.put("times", times);
        }
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.NONE, null, "/addInsulin", map, successCallBack, failureCallBack);
    }

    public static Call<String> getDrugs(String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_ARRAY, DrugListInfo.class, "/getDrugs", map, successCallBack, failureCallBack);
    }


    public static Call<String> addPlan(String token, String plan_name, String plan, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("plan_name", plan_name);
        map.put("plan", plan);
        map.put("access_token", token);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.NONE, null, "/addPlan", map, successCallBack, failureCallBack);
    }

    /**
     * 绑定设备
     *
     * @param mac
     * @param token
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> bindInsulin(String mac, String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("mac", mac);
        map.put("access_token", token);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.NONE, null, "/bindInsulin", map, successCallBack, failureCallBack);
    }

    /**
     * 解绑设备
     *
     * @param mac
     * @param token
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> unbindInsulin(String mac, String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("mac", mac);
        map.put("access_token", token);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.NONE, null, "/unbindInsulin", map, successCallBack, failureCallBack);
    }

    /**
     * @param token
     * @param insulis
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> addInsulins(String token, String insulis, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("insulis", insulis);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.NONE, null, "/addInsulins", map, successCallBack, failureCallBack);
    }

    /**
     * 用户某日某针胰岛素注射数据列表
     */
    public static Call<String> getInjectionDetails(String token, String day_time, String times, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("day_time", day_time);
        map.put("times", times);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_OBJECT, InjectDetailInfo.class, "/insulinDetail", map, successCallBack, failureCallBack);
    }


    public static Call<String> getDeviceIsBind(String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_OBJECT, DeviceChangeBean.class, "/port/Personal/personalimei", map, successCallBack, failureCallBack);
    }

    /**
     * checkMac
     *
     * @param token
     * @param mac
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> checkMac(String token, String mac, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("mac", mac);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_OBJECT, DeviceChangeBean.class, "/checkMac", map, successCallBack, failureCallBack);
    }

    /**
     * @param token
     * @param eqcode
     * @param power
     * @param dosage
     * @param status
     * @param worning
     * @param model
     * @param base_rate
     * @param value
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> updateeqinfo(String token, String eqcode, String power, String dosage, String status, String worning, String model, String base_rate, String value, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("eqcode", eqcode);
        map.put("power", power);
        map.put("dosage", dosage);
        map.put("status", status);
        map.put("worning", worning);
        map.put("model", model);
        map.put("base_rate", base_rate);
        map.put("value", value);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_OBJECT, InsulinDeviceInfo.class, "/updateeqinfo", map, successCallBack, failureCallBack);
    }

    public static Call<String> geteqinsulins(String token, String type, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("type", type);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_ARRAY, InsulinDeviceInfo.class, "/geteqinsulins", map, successCallBack, failureCallBack);
    }

    /**
     * 添加设备数据
     *
     * @param token
     * @param type
     * @param data
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> addeqinsulins(String token, String type, String data, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("type", type);
        map.put("data", data);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_ARRAY, InsulinDeviceInfo.class, "/addeqinsulins", map, successCallBack, failureCallBack);
    }


    public static Call<String> adduserbase(String token, String base_rate1, String base_rate2, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("base_rate1", base_rate1);
        map.put("base_rate2", base_rate2);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_ARRAY, null, "/adduserbase", map, successCallBack, failureCallBack);
    }

    /**
     * 获取基础率信息
     *
     * @param token
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> getuserbase(String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_OBJECT, InsulinDeviceAllInfo.class, "/getuserbase", map, successCallBack, failureCallBack);
    }

    /**
     * 解绑设备
     *
     * @param eqcode
     * @param token
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> unbindeqinsulin(String eqcode, String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("eqcode", eqcode);
        map.put("access_token", token);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.NONE, null, "/unbindeqinsulin", map, successCallBack, failureCallBack);
    }

    /**
     * 设备绑定
     *
     * @param eqcode
     * @param token
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> bindeqinsulin(String eqcode, String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("eqcode", eqcode);
        map.put("access_token", token);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.NONE, null, "/bindeqinsulin", map, successCallBack, failureCallBack);
    }

    /**
     * 获取方案未读数
     *
     * @param token
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> getusereqplanunread(String token, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_OBJECT, PlanInfo.class, "/getusereqplanunread", map, successCallBack, failureCallBack);
    }

    /**
     * 方案列表
     *
     * @param token
     * @param type            1大剂量 2基础率
     * @param page
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> getusereqplan(String token, String type, String page, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("type", type);
        map.put("page", page);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_ARRAY, PlanInfo.class, "/getusereqplan", map, successCallBack, failureCallBack);
    }

    /**
     * 方案详情
     *
     * @param token
     * @param plan_id
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> getusereqplandetail(String token, String plan_id, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("plan_id", plan_id);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_OBJECT, PlanAllInfo.class, "/getusereqplandetail", map, successCallBack, failureCallBack);
    }

    /**
     * @param token
     * @param plan_id
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> getusereqplandetailBase(String token, String plan_id, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("plan_id", plan_id);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_OBJECT, PlanAllBaseInfo.class, "/getusereqplandetail", map, successCallBack, failureCallBack);
    }

    /**
     * @param token
     * @param plan_id
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public static Call<String> getusereqplanconfirm(String token, String plan_id, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("plan_id", plan_id);
        return BaseNetworkUtils.postRequest(false, BaseNetworkUtils.JSON_OBJECT, PlanAllBaseInfo.class, "/getusereqplanconfirm", map, successCallBack, failureCallBack);
    }
}
