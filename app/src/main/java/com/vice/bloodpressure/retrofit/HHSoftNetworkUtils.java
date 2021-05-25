package com.vice.bloodpressure.retrofit;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class HHSoftNetworkUtils {
    private static final String TAG = "HHSoftNetworkUtils";
//    private final String TAG="xiao";

    /**
     * 常见Content-Type 类型
     */
    public enum ContentType {
        /**
         * :HTML格式
         */
        TEXT_HTML,
        /**
         * :纯文本格式
         */
        TEXT_PLAIN,
        /**
         * :XML格式
         */
        TEXT_XML,
        /**
         * :gif图片格式
         */
        IMAGE_GIF,
        /**
         * :jpg图片格式
         */
        IMAGE_JPEG,
        /**
         * :png图片格式
         */
        IMAGE_PNG,
        /**
         * 表单中默认的encType,表单数据被编码为key/value格式发送到服务器
         */
        APPLICATION_URLENCODED,
        /**
         * JSON数据格式
         */
        APPLICATION_JSON,
        /**
         * XML数据格式
         */
        APPLICATION_XML,
        /**
         * XHTML格式
         */
        APPLICATION_HTML,
        /**
         * 二进制流数据（常见的文件下载)
         */
        APPLICATION_STREAM,
        /**
         * 需要在表单中进行文件上传时，就需要使用该格式
         */
        MULTIPART_FORM_DATA
    }


    /**
     * GET请求
     *
     * @param ip
     * @param methodName
     * @param parameterMap
     * @param headerMap
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public Call<String> sendGetRequestAsync(final String ip, final String methodName, Map<String, String> parameterMap, Map<String, String> headerMap, final BiConsumer<Call<String>, String> successCallBack, final BiConsumer<Call<String>, Throwable> failureCallBack) {
        if (parameterMap == null) {
            parameterMap = new HashMap<>();
        }
        Call<String> call;
        if (headerMap == null || headerMap.size() == 0) {
            call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callGetRequest(methodName, parameterMap);
        } else {
            call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callGetRequestWithHeader(methodName, parameterMap, headerMap);
        }
        if (call != null) {
            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.i(TAG, "callGetRequest==onResponse==" + ip + methodName + "==" + response.code());
                    if (successCallBack == null) {
                        return;
                    }
                    if (response.isSuccessful()) {
                        try {
                            String result = response.body();
                            Log.i(TAG, "sendGetRequest==onResponse==" + result);
                            successCallBack.accept(call, result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            failureCallBack.accept(call, new Throwable(response.code() + ""));
                        } catch (Exception var4) {
                            var4.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if (failureCallBack == null) {
                        return;
                    }
                    try {
                        failureCallBack.accept(call, t);
                    } catch (Exception var4) {
                        var4.printStackTrace();
                    }
                }
            });
        } else {
            if (failureCallBack != null) {
                try {
                    failureCallBack.accept(call, new Throwable("retrofit create fail"));
                } catch (Exception var4) {
                    var4.printStackTrace();
                }
            }
        }
        return call;
    }

    /**
     * POST请求
     * 1、contentType为null，默认请求方式：application/x-www-form-urlencoded
     * 2、contentType为APPLICATION_JSON，请求方式：application/json
     *
     * @param contentType
     * @param ip
     * @param methodName
     * @param parameterMap
     * @param files
     * @param headerMap
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public Call<String> sendPostRequestAsync(ContentType contentType, final String ip, final String methodName, Map<String, String> parameterMap, List<MultipartBody.Part> files, Map<String, String> headerMap, final BiConsumer<Call<String>, String> successCallBack, final BiConsumer<Call<String>, Throwable> failureCallBack) {
        if (contentType == null) {
            contentType = ContentType.APPLICATION_URLENCODED;
        }
        if (parameterMap == null) {
            parameterMap = new HashMap<>();
        }
        if (parameterMap != null && parameterMap.size() > 0) {
            for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
                Log.i(TAG, "sendPostRequestAsync==para==" + entry.getKey() + "==" + entry.getValue());
            }
        }
        Call<String> call;
        if (ContentType.MULTIPART_FORM_DATA == contentType) {
            if (files == null) {
                files = new ArrayList<>();
            }
            Map<String, RequestBody> requestParamsMap = new HashMap<>();
            if (parameterMap != null && parameterMap.size() > 0) {
                for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
                    requestParamsMap.put(entry.getKey(), HHSoftNetworkUtils.toRequestBody(entry.getValue()));
                }
            }
            //文件上传
            if (headerMap == null || headerMap.size() == 0) {
                call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callPostRequestMultipartURL(methodName, requestParamsMap, files);
            } else {
                call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callPostRequestMultipartURLWithHeader(methodName, headerMap, requestParamsMap, files);
            }
        } else if (ContentType.APPLICATION_JSON == contentType) {
            // 遍历参数map，拼接成json
            StringBuilder paramStringBuilder = new StringBuilder();
            if (parameterMap.size() > 0) {
                paramStringBuilder.append("{");
                for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
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
            if (headerMap == null || headerMap.size() == 0) {
                call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callPostRequestFormUrlForBody(methodName, requestBody);
            } else {
                call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callPostRequestFormUrlWithHeaderForBody(methodName, headerMap, requestBody);
            }
        } else {
            if (headerMap == null || headerMap.size() == 0) {
                call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callPostRequestFormUrl(methodName, parameterMap);
            } else {
                call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callPostRequestFormUrlWithHeader(methodName, parameterMap, headerMap);
            }
        }
        if (call != null) {
            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.i(TAG, "sendPostRequestAsync==onResponse==" + ip + methodName + "==" + response.code());
                    if (successCallBack == null) {
                        return;
                    }
                    if (response.isSuccessful()) {
                        try {
                            String result = response.body();
                            Log.i(TAG, "sendPostRequestAsync==onResponse==" + result);
                            successCallBack.accept(call, result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            failureCallBack.accept(call, new Throwable(response.code() + ""));
                        } catch (Exception var4) {
                            var4.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.i(TAG, "postRequestMultipartURLWithHeadersAsync==onFailure==" + Log.getStackTraceString(t));
                    if (failureCallBack == null) {
                        return;
                    }
                    try {
                        failureCallBack.accept(call, t);
                    } catch (Exception var4) {
                        var4.printStackTrace();
                    }
                }
            });
        } else {
            if (failureCallBack != null) {
                try {
                    failureCallBack.accept(call, new Throwable("retrofit create fail"));
                } catch (Exception var4) {
                    var4.printStackTrace();
                }
            }
        }
        return call;
    }

    /**
     * 网络请求：post异步请求,明文传参
     * Content-Type: application/x-www-form-urlencoded
     *
     * @param ip              IP地址
     * @param methodName      接口名字
     * @param parameterMap    参数map
     * @param headerMap       请求头map
     * @param successCallBack 请求成功回调
     * @param failureCallBack 请求失败回调
     * @return
     */
    public Call<String> postRequestFormURLWithHeadersAsync(String ip, String methodName, Map<String, String> parameterMap, Map<String, String> headerMap, final BiConsumer<Call<String>, String> successCallBack, final BiConsumer<Call<String>, Throwable> failureCallBack) {
        Call<String> call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callPostRequestFormUrlWithHeader(methodName, headerMap, parameterMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                HHSoftLogUtils.i(TAG,"postRequestUrlAsync==onResponse=="+response);
                if (response.isSuccessful()) {
                    try {
                        String result = response.body();
                        Log.i(TAG, "postRequestUrlAsync==onResponse==" + result);
                        successCallBack.accept(call, result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        failureCallBack.accept(call, new Throwable(response.code() + ""));
                    } catch (Exception var4) {
                        var4.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                try {
                    failureCallBack.accept(call, t);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return call;
    }

    /**
     * 网络请求：post异步请求,明文传参,可传文件头部
     *
     * @param ip
     * @param methodName
     * @param parameterMap
     * @param files
     * @param headerMap
     * @param successCallBack
     * @param failureCallBack
     * @return
     */
    public Call<String> postRequestMultipartURLWithHeadersAsync(String ip, String methodName, Map<String, RequestBody> parameterMap, List<MultipartBody.Part> files, Map<String, String> headerMap, final BiConsumer<Call<String>, String> successCallBack, final BiConsumer<Call<String>, Throwable> failureCallBack) {
        Call<String> call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callPostRequestMultipartURLWithHeader(methodName, headerMap, parameterMap, files);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                HHSoftLogUtils.i(TAG,"postRequestUrlAsync==onResponse=="+response);
                if (response.isSuccessful()) {
                    try {
                        String result = response.body();
                        Log.i(TAG, "postRequestMultipartURLWithHeadersAsync==onResponse==" + result);
                        successCallBack.accept(call, result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        failureCallBack.accept(call, new Throwable(response.code() + ""));
                    } catch (Exception var4) {
                        var4.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "postRequestMultipartURLWithHeadersAsync==onFailure==" + Log.getStackTraceString(t));
                try {
                    failureCallBack.accept(call, t);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(TAG, "postRequestMultipartURLWithHeadersAsync==onFailure==error==" + Log.getStackTraceString(e));
                }
            }
        });
        return call;
    }

    /**
     * 网络请求：post异步请求,该方法进行了默认的加密解密
     *
     * @param ip              IP地址
     * @param methodName      接口名字
     * @param parameterMap    参数Map
     * @param headerMap       请求头map
     * @param successCallBack 请求成功回调
     * @param failureCallBack 请求失败回调
     * @return
     */
    public Call<String> defaultPostRequestFormURLAsync(String ip, String methodName, Map<String, String> parameterMap, Map<String, String> headerMap, final BiConsumer<Call<String>, String> successCallBack, final BiConsumer<Call<String>, Throwable> failureCallBack) {
        String parameterStr = getDefaultParaParameterByMap(parameterMap, "");
        return defaultPostRequestFormURLAsync("", ip, methodName, parameterStr, headerMap, successCallBack, failureCallBack);
    }

    /**
     * 网络请求：post异步请求
     *
     * @param ip              IP地址
     * @param methodName      接口名字
     * @param parameterStr    参数(json字符串)
     * @param headerMap       请求头map
     * @param successCallBack 请求成功回调
     * @param failureCallBack 请求失败回调
     * @return
     */
    public Call<String> defaultPostRequestFormURLAsync(final String aesKey, final String ip, final String methodName, String parameterStr, Map<String, String> headerMap, final BiConsumer<Call<String>, String> successCallBack, final BiConsumer<Call<String>, Throwable> failureCallBack) {
        Call<String> call;
        if (headerMap != null && headerMap.size() > 0) {
            call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).defaultCallPostRequestFormUrlWithHeader(methodName, headerMap, parameterStr);
        } else {
            call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).defaultCallPostRequestFormUrl(methodName, parameterStr);
        }
        if (call == null) {
            try {
                failureCallBack.accept(null, new Throwable("retrofit create fail"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.i(TAG, "defaultPostRequestFormURLAsync==onResponse==" + ip + methodName + "==" + response.code());
                    try {
                        if (response.isSuccessful()) {
                            String result = TextUtils.isEmpty(aesKey) ? HHSoftEncryptUtils.decryptAES(response.body()) : HHSoftEncryptUtils.decryptAESWithKey(response.body(), aesKey);
                            Log.i(TAG, "defaultPostRequestFormURLAsync==success==" + ip + methodName + "==" + result);
                            successCallBack.accept(call, result);
                        } else {
                            failureCallBack.accept(call, new HttpException(response));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i(TAG, "defaultPostRequestFormURLAsync==error==" + Log.getStackTraceString(e));
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.i(TAG, "defaultPostRequestFormURLAsync==onFailure==" + Log.getStackTraceString(t));
                    try {
                        failureCallBack.accept(call, t);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i(TAG, "defaultPostRequestFormURLAsync==error==" + Log.getStackTraceString(e));
                    }
                }
            });
        }
        return call;
    }


    /**
     * 网络请求：post异步请求 图文
     *
     * @param ip              IP地址
     * @param methodName      方法名
     * @param parameterMap    请求参数
     * @param fileList        文件集合
     * @param headerMap       请求头
     * @param successCallBack 成功回调
     * @param failureCallBack 失败回调，返回的Call会为空
     * @return
     */
    public Call<String> defaultPostRequestFileURLAsync(String ip, String methodName, Map<String, String> parameterMap, List<MultipartBody.Part> fileList, Map<String, String> headerMap, final BiConsumer<Call<String>, String> successCallBack, final BiConsumer<Call<String>, Throwable> failureCallBack) {
        String parameterStr = getDefaultParaParameterByMap(parameterMap, "");
        return defaultPostRequestFileURLAsync("", ip, methodName, parameterStr, fileList, headerMap, successCallBack, failureCallBack);
    }

    /**
     * 网络请求：post异步请求 图文，请求参数需要进行Json拼接时，使用该方法
     *
     * @param ip              IP地址
     * @param methodName      方法名
     * @param parameterStr    请求参数需要进行Json拼接时，拼接的json字符串
     * @param fileList        文件集合
     * @param headerMap       请求头
     * @param successCallBack 成功回调
     * @param failureCallBack 失败回调，返回的Call会为空
     * @return
     */
    public Call<String> defaultPostRequestFileURLAsync(final String aesKey, final String ip, final String methodName, String parameterStr, List<MultipartBody.Part> fileList, Map<String, String> headerMap, final BiConsumer<Call<String>, String> successCallBack, final BiConsumer<Call<String>, Throwable> failureCallBack) {
        Map<String, RequestBody> paramMap = new HashMap<>();
        paramMap.put("para", toRequestBody(parameterStr));
        Call<String> call;
        if (headerMap != null && headerMap.size() > 0) {
            call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callPostRequestMultipartURLWithHeader(methodName, headerMap, paramMap, fileList);
        } else {
            call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callPostRequestMultipartURL(methodName, paramMap, fileList);
        }
        if (call == null) {
            try {
                failureCallBack.accept(call, new Throwable("retrofit create fail"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.i(TAG, "defaultPostRequestFileURLAsync==onResponse==" + ip + methodName + "==" + response.code());
                    try {
                        if (response.isSuccessful()) {
                            String result = TextUtils.isEmpty(aesKey) ? HHSoftEncryptUtils.decryptAES(response.body()) : HHSoftEncryptUtils.decryptAESWithKey(response.body(), aesKey);
                            Log.i(TAG, "defaultPostRequestFileURLAsync==success==" + ip + methodName + "==" + result);
                            successCallBack.accept(call, result);
                        } else {
                            failureCallBack.accept(call, new HttpException(response));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i(TAG, "defaultPostRequestFileURLAsync==error==" + Log.getStackTraceString(e));
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.i(TAG, "defaultPostRequestFileURLAsync==onFailure==" + ip + methodName + "==" + Log.getStackTraceString(t));
                    try {
                        failureCallBack.accept(call, t);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i(TAG, "defaultPostRequestFileURLAsync==error==" + Log.getStackTraceString(t));
                    }
                }
            });
        }
        return call;
    }

    public Call<String> getRequest(final String ip, final String methodName, Map<String, String> parameterMap, final Consumer<String> successCallBack, final Consumer<Throwable> failureCallBack) {
        Call call = HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).callGetRequest(methodName, parameterMap);
        if (call != null) {
            try {
                Response<String> resp = call.execute();
                if (resp.isSuccessful()) {
                    if (successCallBack != null) {
                        successCallBack.accept(resp.body());
                    }
                } else {
                    if (failureCallBack != null) {
                        failureCallBack.accept(new HttpException(resp));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return call;
    }


    public Call<String> obtainDefaultPostRequestFormURLCall(String ip, String methodName, Map<String, String> parameterMap, Map<String, String> headerMap) {
        String parameterStr = getDefaultParaParameterByMap(parameterMap, "");
        if (headerMap != null && headerMap.size() > 0) {
            return HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).defaultCallPostRequestFormUrlWithHeader(methodName, headerMap, parameterStr);
        } else {
            return HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).defaultCallPostRequestFormUrl(methodName, parameterStr);
        }
    }

    public Call<String> obtainDefaultPostRequestFormURLCall(String ip, String methodName, String parameterStr, Map<String, String> headerMap) {
        return HHSoftRetrofitManager.getInstance().create(ip, HHSoftRetrofitService.class).defaultCallPostRequestFormUrlWithHeader(methodName, headerMap, parameterStr);
    }

    /**
     * 获取请求接口参数para的值（json字符串）
     *
     * @param map
     * @return
     */
    public String getDefaultParaParameterByMap(Map<String, String> map, String aesKey) {
        if (map == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Log.i(TAG, "HHSoftNetworkUtils==Parameter==" + entry.getKey() + "==" + entry.getValue());
            builder.append("\"" + entry.getKey() + "\":" + "\"" + HHSoftEncodeUtils.encodeBase64(entry.getValue()) + "\"" + ",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("}");
        String para = TextUtils.isEmpty(aesKey) ? HHSoftEncryptUtils.encryptAES(builder.toString()) : HHSoftEncryptUtils.encryptAESWithKey(builder.toString(), aesKey);
        Log.i(TAG, "HHSoftNetworkUtils==para==" + para);
        return para;
    }

    public static RequestBody toRequestBody(String paramStr) {
        return RequestBody.create(MediaType.parse("text/plain"), paramStr);
    }

    /**
     * 生成文件对象
     *
     * @param fileKey
     * @param filePath
     * @return
     */
    public static MultipartBody.Part toFileMultipartBodyPart(String fileKey, String filePath) {
        /*MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);  ///< ParamKey.TOKEN 自定义参数key常量类，即参数
        String contentType = "application/octet-stream";
        HHSoftFileUtils.FileType fileType = HHSoftFileUtils.fileTypeForImageData(filePath);
        if (HHSoftFileUtils.FileType.IMAGE_GIF == fileType) {
            contentType = "image/gif";
        } else if (HHSoftFileUtils.FileType.IMAGE_WEBP == fileType) {
            contentType = "image/webp";
        }
        File file = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse(contentType), file); ///< "multipart/from-data"
        builder.addFormDataPart(fileKey, file.getName(), requestBody);   ///< 多张图片，多个requestbody就行..
        List<MultipartBody.Part> partsBody = builder.build().parts();
        return partsBody.get(0);*/

        File file = new File(filePath);
        Log.i(TAG, "toFileMultipartBodyPart==" + filePath+"=="+file.isFile());
        String contentType = "application/octet-stream";
       /* HHSoftFileUtils.FileType fileType = HHSoftFileUtils.fileTypeForImageData(filePath);
        if (HHSoftFileUtils.FileType.IMAGE_GIF == fileType) {
            contentType = "image/gif";
        } else if (HHSoftFileUtils.FileType.IMAGE_WEBP == fileType) {
            contentType = "image/webp";
        }*/
        RequestBody requestBody = RequestBody.create(MediaType.parse(contentType), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData(fileKey, file.getName(), requestBody);
        return part;
    }

    private static class SingletonHolder {
        static HHSoftNetworkUtils mInstance = new HHSoftNetworkUtils();

        private SingletonHolder() {
        }
    }

    public static HHSoftNetworkUtils getInstance() {
        return SingletonHolder.mInstance;
    }
}
