package com.vice.bloodpressure.retrofit;

import android.util.Log;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.BiConsumer;
import retrofit2.Call;
import retrofit2.HttpException;

public class BaseNetworkUtils {


    private static final String TAG = "HHSoftNetworkUtils";
    private static final String AccessTokenKey = "Authorization";


    /**
     * 需要把返回数据 解析F成的对应类型
     * NONE 不需要解析数据
     * JSON_OBJECT 需要把数据解析成model
     * JSON_ARRAY 需要把数据解析成list
     */
    public static final int NONE = 0;
    public static final int JSON_OBJECT = 1;
    public static final int JSON_ARRAY = 2;

    @IntDef({NONE, JSON_OBJECT, JSON_ARRAY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface JsonParseMode {
    }

    /**
     * 网络请求
     *
     * @param requestType     请求方法，可参考：HHSoftNetReqUtils.RequestType.PUT
     * @param jsonParseMode   解析数据类型，可取值BaseNetworkUtils.NONE、BaseNetworkUtils.JSON_OBJECT、BaseNetworkUtils.JSON_ARRAY
     * @param methodName
     * @param paramMap
     * @param successCallBack
     * @param failureCallBack
     * @param accessToken     未保存token到本地之前，如果需要传token，此参数不能为空
     * @return
     */
    public static Call<String> networkRequest(boolean isNeedAccessToken, String accessToken, HHSoftNetReqUtils.RequestType requestType, HHSoftNetReqUtils.RequestBodyType requestBodyType, @JsonParseMode int jsonParseMode, Class clazz, String methodName, Map<String, String> paramMap, LinkedHashMap<String, String> fileMap, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, String> headerMap = new HashMap<>();
        //        if (isNeedAccessToken) {
        //            if (TextUtils.isEmpty(accessToken)) {
        //                headerMap.put("accessToken", SharedPreferencesUtils.getInfo(HHSoftApplication.getContext(), SharedPreferencesConstant.USER_ID));
        //            } else {
        //                headerMap.put("accessToken", accessToken);
        //            }
        //
        //        }
        return new HHSoftNetReqUtils.Builder()
                //IP
                .baseUrl(ConstantParamNew.IP)
                //方法名称
                .methodName(methodName)
                //请求方式
                .requestType(requestType)
                //
                .requestBodyType(requestBodyType)
                //请求头
                .headerMap(headerMap)
                //请求参数
                .paramMap(paramMap)
                //传递的文件参数
                .fileMap(fileMap)
                //请求成功
                .successCallBack((call, result) -> processJsonParse(call, result, jsonParseMode, clazz, successCallBack))
                //请求失败
                .failureCallBack((call, t) -> {
                    processFailureCallBack(requestType, requestBodyType, isNeedAccessToken, jsonParseMode, clazz, ConstantParamNew.IP, methodName, paramMap, fileMap, call, t, successCallBack, failureCallBack);
                }).build();
    }

    /**
     * 请求接口401的处理 待实现
     *
     * @param requestType
     * @param requestBodyType
     * @param isNeedAccessToken
     * @param parseMode
     * @param clazz
     * @param ip
     * @param methodName
     * @param paramMap
     * @param fileMap
     * @param failureCall
     * @param failureThrowable
     * @param successCallBack
     * @param failureCallBack
     * @throws Exception
     */
    public static void processFailureCallBack(HHSoftNetReqUtils.RequestType requestType, HHSoftNetReqUtils.RequestBodyType requestBodyType, boolean isNeedAccessToken, @JsonParseMode int parseMode, Class clazz, String ip, String methodName, Map<String, String> paramMap, LinkedHashMap<String, String> fileMap, Call<String> failureCall, Throwable failureThrowable, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) throws Exception {
        Log.i(TAG, "failureCallBack: ");
        if (failureThrowable instanceof HttpException && 401 == ((HttpException) failureThrowable).response().code()) {

        } else {
            if (failureCallBack != null) {
                failureCallBack.accept(failureCall, failureThrowable);
            }
        }
    }

    /**
     * 解析数据
     *
     * @param call
     * @param result
     * @param parseMode
     * @param clazz
     * @param successCallBack
     * @throws Exception
     */
    private static void processJsonParse(Call<String> call, String result, @JsonParseMode int parseMode, Class clazz, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack) throws Exception {
        HHSoftBaseResponse response = new HHSoftBaseResponse();
        JSONObject jsonObject = new JSONObject(result);
        response.code = jsonObject.optInt("code");
        response.msg = jsonObject.optString("msg");
        response.result = jsonObject.optString("data");
        //        response.url = jsonObject.optString("url");
        if (20001 == response.code) {
            //            //用户尚未登录 清掉当地的accessToken
            //            UserInfoUtils.outlog(HHSoftApplication.getContext(), null, null);
            //            ARouter.getInstance().build(PathConstant.REGIST_AND_LOGIN_MAIN_ACTIVITY).navigation();
        } else {
            if (JSON_OBJECT == parseMode) {
                if (200 == response.code) {
                    response.object = new Gson().fromJson(response.result, clazz);
                }
            } else if (JSON_ARRAY == parseMode) {
                if (response.code == 200) {
                    //Gson gson = new Gson();
                    //Type objectType = type(HHSoftResponseListJson.class, clazz);
                    //response.object = ((HHSoftResponseListJson) gson.fromJson(result, objectType)).data;
                    response.object = fromJsonToList(response.result, clazz);
                } else if (response.code == 101) {
                    response.object = new ArrayList<>();
                }
            }
        }
        successCallBack.accept(call, response);
    }

    public static Call<String> getRequest(boolean isNeedAccessToken, @JsonParseMode int jsonParseMode, Class clazz, String methodName, Map<String, String> paramMap, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        return networkRequest(isNeedAccessToken, "", HHSoftNetReqUtils.RequestType.GET, HHSoftNetReqUtils.RequestBodyType.MULTIPART, jsonParseMode, clazz, methodName, paramMap, null, successCallBack, failureCallBack);
    }

    public static Call<String> getRequest(boolean isNeedAccessToken, String accessToken, @JsonParseMode int jsonParseMode, Class clazz, String methodName, Map<String, String> paramMap, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        return networkRequest(isNeedAccessToken, accessToken, HHSoftNetReqUtils.RequestType.GET, HHSoftNetReqUtils.RequestBodyType.MULTIPART, jsonParseMode, clazz, methodName, paramMap, null, successCallBack, failureCallBack);
    }

    public static Call<String> postRequest(boolean isNeedAccessToken, @JsonParseMode int jsonParseMode, Class clazz, String methodName, Map<String, String> paramMap, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        return networkRequest(isNeedAccessToken, "", HHSoftNetReqUtils.RequestType.POST, HHSoftNetReqUtils.RequestBodyType.MULTIPART, jsonParseMode, clazz, methodName, paramMap, null, successCallBack, failureCallBack);
    }

    public static Call<String> putRequest(boolean isNeedAccessToken, @JsonParseMode int jsonParseMode, Class clazz, String methodName, Map<String, String> paramMap, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        return networkRequest(isNeedAccessToken, "", HHSoftNetReqUtils.RequestType.PUT, HHSoftNetReqUtils.RequestBodyType.MULTIPART, jsonParseMode, clazz, methodName, paramMap, null, successCallBack, failureCallBack);
    }

    public static Call<String> uploadImgRequest(boolean isNeedAccessToken, @JsonParseMode int jsonParseMode, Class clazz, String methodName, Map<String, String> paramMap, LinkedHashMap<String, String> fileMap, BiConsumer<Call<String>, HHSoftBaseResponse> successCallBack, BiConsumer<Call<String>, Throwable> failureCallBack) {
        return networkRequest(isNeedAccessToken, "", HHSoftNetReqUtils.RequestType.POST, HHSoftNetReqUtils.RequestBodyType.MULTIPART, jsonParseMode, clazz, methodName, paramMap, fileMap, successCallBack, failureCallBack);
    }


    /**
     * 将json字符串转换为ArrayList
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> fromJsonToList(String json, Class<T> clazz) {
        Type type = new ParameterizedTypeImpl(clazz);
        List<T> list = new Gson().fromJson(json, type);
        return list;
    }

    private static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return args;
            }

            @Override
            public Type getRawType() {
                return raw;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {
        private Class clazz;

        public ParameterizedTypeImpl(Class clazz) {
            this.clazz = clazz;
        }

        @NonNull
        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }

        @NonNull
        @Override
        public Type getRawType() {
            return List.class;
        }

        @Nullable
        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}

