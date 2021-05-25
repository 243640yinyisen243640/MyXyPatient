package com.vice.bloodpressure.retrofit;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.BiConsumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class HHSoftNetReqUtils {

    private static final String TAG = "HHSoftNetReqUtils";

    /**
     * 请求方式
     */
    public enum ThreadType {
        SYNC,
        ASYNC
    }

    /**
     * 请求方式
     */
    public enum RequestType {
        GET,
        POST,
        PUT,
        DELETE
    }

    /**
     * 请求主体
     */
    public enum RequestBodyType {
        STREAMING,
        FORM_URL_ENCODED_FIELD,
        FORM_URL_ENCODED_QUERY,
        BODY_JSON,
        MULTIPART
    }


    public static final class Builder {
        private ThreadType threadType;

        private RequestType requestType;
        private RequestBodyType requestBodyType;

        private Map<String, String> headerMap;
        private Map<String, String> paramMap;
        private Map<String, String> fileMap;
        private String ip;
        private String methodName;

        private BiConsumer<Call<String>, String> successCallBack;
        private BiConsumer<Call<String>, Throwable> failureCallBack;

        public Builder() {
            this.threadType = ThreadType.ASYNC;
            this.requestType = RequestType.POST;
            this.requestBodyType = RequestBodyType.FORM_URL_ENCODED_FIELD;
            this.headerMap = new HashMap<>();
            this.paramMap = new HashMap<>();
            this.fileMap = new LinkedHashMap<>();
        }

        public Call<String> build() {
            Log.i(TAG, "HHSoftNetReqUtils:start:" + ip + methodName + "==" + threadType + "==" + requestType + "==" + requestBodyType);
            if (TextUtils.isEmpty(ip)) {
                throw new IllegalStateException("Base URL required is empty.");
            }
            if (headerMap != null && headerMap.size() > 0) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    Log.i(TAG, "HHSoftNetReqUtils:headerMap:" + entry.getKey() + "==" + entry.getValue());
                }
            }
            if (paramMap != null && paramMap.size() > 0) {
                StringBuilder paramStringBuilder = new StringBuilder();
                paramStringBuilder.append("{");
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    Log.i(TAG, "HHSoftNetReqUtils:paramMap:" + entry.getKey() + "==" + entry.getValue());
                    paramStringBuilder.append("\"");
                    paramStringBuilder.append(entry.getKey());
                    paramStringBuilder.append("\"");
                    paramStringBuilder.append(":");
                    paramStringBuilder.append("\"");
                    paramStringBuilder.append(entry.getValue());
                    paramStringBuilder.append("\"");
                    paramStringBuilder.append(",");
                }
                paramStringBuilder.deleteCharAt(paramStringBuilder.length() - 1);
                paramStringBuilder.append("}");
                Log.i(TAG, "HHSoftNetReqUtils:paramJsonStr:" + paramStringBuilder.toString());
            }
            if (fileMap != null && fileMap.size() > 0) {
                for (Map.Entry<String, String> entry : fileMap.entrySet()) {
                    Log.i(TAG, "HHSoftNetReqUtils:fileMap:" + entry.getKey() + "==" + entry.getValue());
                }
            }
            Call<String> call = null;
            if (requestType == RequestType.GET) {
                if (headerMap != null && headerMap.size() > 0) {
                    call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callGetRequestWithHeader(methodName, paramMap, headerMap);
                } else {
                    call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callGetRequest(methodName, paramMap);
                }
            } else if (requestType == RequestType.POST) {
                if (requestBodyType == RequestBodyType.FORM_URL_ENCODED_FIELD) {
                    if (headerMap != null && headerMap.size() > 0) {
                        call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callPostRequestFormUrlWithHeader(methodName, paramMap, headerMap);
                    } else {
                        call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callPostRequestFormUrl(methodName, paramMap);
                    }
                } else if (requestBodyType == RequestBodyType.BODY_JSON) {
                    // 遍历参数map，拼接成json
                    StringBuilder paramStringBuilder = new StringBuilder();
                    if (paramMap != null && paramMap.size() > 0) {
                        paramStringBuilder.append("{");
                        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                            paramStringBuilder.append("\"");
                            paramStringBuilder.append(entry.getKey());
                            paramStringBuilder.append("\"");
                            paramStringBuilder.append(":");
                            paramStringBuilder.append("\"");
                            paramStringBuilder.append(entry.getValue());
                            paramStringBuilder.append("\"");
                            paramStringBuilder.append(",");
                        }
                        paramStringBuilder.deleteCharAt(paramStringBuilder.length() - 1);
                        paramStringBuilder.append("}");
                    }
                    RequestBody requestBody = RequestBody.create(paramStringBuilder.toString(), MediaType.parse("application/json; charset=utf-8"));
                    if (headerMap != null && headerMap.size() > 0) {
                        call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callPostRequestFormUrlWithHeaderForBody(methodName, headerMap, requestBody);
                    } else {
                        call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callPostRequestFormUrlForBody(methodName, requestBody);
                    }
                } else if (requestBodyType == RequestBodyType.MULTIPART) {
                    Map<String, RequestBody> requestParamsMap = new HashMap<>();
                    if (paramMap != null && paramMap.size() > 0) {
                        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                            requestParamsMap.put(entry.getKey(), HHSoftNetworkUtils.toRequestBody(entry.getValue()));
                        }
                    }
                    List<MultipartBody.Part> files = new ArrayList<>();
                    if (fileMap != null && fileMap.size() > 0) {
                        for (Map.Entry<String, String> entry : fileMap.entrySet()) {
                            files.add(HHSoftNetworkUtils.toFileMultipartBodyPart(entry.getKey(), entry.getValue()));
                        }
                    }
                    if (headerMap != null && headerMap.size() > 0) {
                        call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callPostRequestMultipartURLWithHeader(methodName, headerMap, requestParamsMap, files);
                    } else {
                        call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callPostRequestMultipartURL(methodName, requestParamsMap, files);
                    }
                }
            } else if (requestType == RequestType.PUT) {
                if (requestBodyType == RequestBodyType.FORM_URL_ENCODED_FIELD) {
                    if (headerMap != null && headerMap.size() > 0) {
                        call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callPutRequestFormUrlWithHeader(methodName, paramMap, headerMap);
                    } else {
                        call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callPutRequestFormUrl(methodName, paramMap);
                    }
                } else if (requestBodyType == RequestBodyType.BODY_JSON) {
                    // 遍历参数map，拼接成json
                    StringBuilder paramStringBuilder = new StringBuilder();
                    if (paramMap != null && paramMap.size() > 0) {
                        paramStringBuilder.append("{");
                        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                            paramStringBuilder.append("\"");
                            paramStringBuilder.append(entry.getKey());
                            paramStringBuilder.append("\"");
                            paramStringBuilder.append(":");
                            paramStringBuilder.append("\"");
                            paramStringBuilder.append(entry.getValue());
                            paramStringBuilder.append("\"");
                            paramStringBuilder.append(",");
                        }
                        paramStringBuilder.deleteCharAt(paramStringBuilder.length() - 1);
                        paramStringBuilder.append("}");
                    }
                    RequestBody requestBody = RequestBody.create(paramStringBuilder.toString(), MediaType.parse("application/json; charset=utf-8"));
                    if (headerMap != null && headerMap.size() > 0) {
                        call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callPutRequestFormUrlWithHeaderForBody(methodName, headerMap, requestBody);
                    } else {
                        call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callPutRequestFormUrlForBody(methodName, requestBody);
                    }
                } else if (requestBodyType == RequestBodyType.MULTIPART) {
                    Map<String, RequestBody> requestParamsMap = new HashMap<>();
                    if (paramMap != null && paramMap.size() > 0) {
                        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                            requestParamsMap.put(entry.getKey(), HHSoftNetworkUtils.toRequestBody(entry.getValue()));
                        }
                    }
                    List<MultipartBody.Part> files = new ArrayList<>();
                    if (fileMap != null && fileMap.size() > 0) {
                        for (Map.Entry<String, String> entry : fileMap.entrySet()) {
                            files.add(HHSoftNetworkUtils.toFileMultipartBodyPart(entry.getKey(), entry.getValue()));
                        }
                    }
                    if (headerMap != null && headerMap.size() > 0) {
                        call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callPutRequestMultipartURLWithHeader(methodName, headerMap, requestParamsMap, files);
                    } else {
                        call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callPutRequestMultipartURL(methodName, requestParamsMap, files);
                    }
                }
            }
            if (call == null) {
                throw new IllegalStateException("Object Call required is null.");
            }
            if (threadType == ThreadType.ASYNC) {
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        LogUtils.showLongLog(TAG, "HHSoftNetReqUtils：ThreadType.ASYNC:onResponse：" + ip + methodName + "==" + response.code());
                        if (response.isSuccessful()) {
                            String respResult = response.body();
                            LogUtils.showLongLog(TAG, "HHSoftNetReqUtils：ThreadType.ASYNC:onResponse：" + respResult);
                            if (successCallBack != null) {
                                try {
                                    successCallBack.accept(call, respResult);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    throw new Error(e);
                                }
                            }
                        } else {
                            if (failureCallBack != null) {
                                try {
                                    failureCallBack.accept(call, new HttpException(response));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    throw new Error(e);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        LogUtils.showLongLog(TAG, "HHSoftNetReqUtils：ThreadType.ASYNC:onFailure：" + Log.getStackTraceString(t));
                        if (failureCallBack != null) {
                            try {
                                failureCallBack.accept(call, t);
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new Error(e);
                            }
                        }
                    }
                });
            } else if (threadType == ThreadType.SYNC) {
                try {
                    Response<String> response = call.execute();
                    LogUtils.showLongLog(TAG, "HHSoftNetReqUtils：ThreadType.SYNC:onResponse：" + ip + methodName + "==" + response.code());
                    if (response.isSuccessful()) {
                        String respResult = response.body();
                        LogUtils.showLongLog(TAG, "HHSoftNetReqUtils：ThreadType.SYNC:onResponse：" + respResult);
                        if (successCallBack != null) {
                            successCallBack.accept(call, respResult);
                        }
                    } else {
                        if (failureCallBack != null) {
                            failureCallBack.accept(call, new HttpException(response));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new Error(e);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new Error(e);
                }

            }
            return call;
        }

        public Builder threadType(ThreadType threadType) {
            if (threadType != null) {
                this.threadType = threadType;
            }
            return this;
        }

        public Builder requestType(RequestType requestType) {
            if (requestType != null) {
                this.requestType = requestType;
            }
            return this;
        }

        public Builder requestBodyType(RequestBodyType requestBodyType) {
            if (requestBodyType != null) {
                this.requestBodyType = requestBodyType;
            }
            return this;
        }

        public Builder headerMap(Map<String, String> headerMap) {
            if (headerMap != null && headerMap.size() > 0) {
                this.headerMap = headerMap;
            }
            return this;
        }

        public Builder paramMap(Map<String, String> paramMap) {
            if (paramMap != null && paramMap.size() > 0) {
                this.paramMap = paramMap;
            }
            return this;
        }

        public Builder fileMap(LinkedHashMap<String, String> fileMap) {
            if (fileMap != null && fileMap.size() > 0) {
                this.fileMap = fileMap;
            }
            return this;
        }

        public Builder methodName(String methodName) {
            if (!TextUtils.isEmpty(methodName)) {
                this.methodName = methodName;
            }
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            if (!TextUtils.isEmpty(baseUrl)) {
                this.ip = baseUrl;
            }
            return this;
        }

        public Builder successCallBack(BiConsumer<Call<String>, String> successCallBack) {
            if (successCallBack != null) {
                this.successCallBack = successCallBack;
            }
            return this;
        }

        public Builder failureCallBack(BiConsumer<Call<String>, Throwable> failureCallBack) {
            if (failureCallBack != null) {
                this.failureCallBack = failureCallBack;
            }
            return this;
        }
    }
}
