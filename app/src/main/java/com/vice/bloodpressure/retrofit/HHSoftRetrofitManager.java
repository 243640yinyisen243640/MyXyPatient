package com.vice.bloodpressure.retrofit;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vice.bloodpressure.retrofit.gson.GsonConverterFactory;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @类说明 Retrofit管理类
 * @作者 hhsoft
 * @创建日期 2019/8/2 17:32
 */
public class HHSoftRetrofitManager {
    private static final String TAG="HHSoftRetrofitManager";
    private static final int DEFAULT_TIME_OUT =20;//5s
    private static final int DEFAULT_READ_TIME_OUT=10;//10s
    private Map<String, Retrofit> mRetrofitMap;
    public HHSoftRetrofitManager() {
        mRetrofitMap=new HashMap<>();
    }


    private OkHttpClient getOkHttpClient(final Map<String, String> headerMap){
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        if (headerMap!=null){
            builder.addInterceptor(new Interceptor() {
                @NotNull
                @Override
                public Response intercept(@NotNull Chain chain) throws IOException {
                    Log.i(TAG,"getOkHttpClient==intercept==");
                    Request.Builder requestBuilder=chain.request().newBuilder();
                    for (Map.Entry<String, String> header:headerMap.entrySet()) {
                        requestBuilder.addHeader(header.getKey(),header.getValue());
                    }
                    return chain.proceed(requestBuilder.build());
                }
            });
        }
        builder.callTimeout(DEFAULT_TIME_OUT, TimeUnit.MINUTES);
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.MINUTES);
        builder.readTimeout(DEFAULT_TIME_OUT, TimeUnit.MINUTES);
        builder.writeTimeout(DEFAULT_TIME_OUT, TimeUnit.MINUTES);
        return builder.build();
    }

    /**
     * 利用静态内部类特性实现外部类的单例
     * 原理：Java中静态内部类可以访问其外部类的成员属性和方法，同时，静态内部类只有当被调用的时候才开始首次被加载，
     * 利用此特性，可以实现懒汉式，在静态内部类中静态初始化外部类的单一实例即可。
     */
    private static class SingletonHolder {
        static HHSoftRetrofitManager mInstance = new HHSoftRetrofitManager();

        private SingletonHolder() {
        }
    }

    /**
     * 获取单例类
     * @return
     */
    public static HHSoftRetrofitManager getInstance(){
        return SingletonHolder.mInstance;
    }

    /**
     * 生成service对象
     * @param url IP地址
     * @param service
     * @param <T>
     * @return
     */
    public <T>T create(String url, Class<T> service){
        if (TextUtils.isEmpty(url)){
            return null;
        }
        if (!mRetrofitMap.containsKey(url)){
            Gson gson=new GsonBuilder().setLenient().create();
            Retrofit retrofit=new Retrofit.Builder()
                    .client(getOkHttpClient(null))
                    .addConverterFactory(GsonConverterFactory.create(gson))//设置数据解析的工具
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(url)
                    .build();
            mRetrofitMap.put(url,retrofit);
        }
        return mRetrofitMap.get(url).create(service);
    }
    public <T>T create(String url, Class<T> service, Map<String, String> headerMap) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        Gson gson=new GsonBuilder().setLenient().create();
        Retrofit mRetrofit=new Retrofit.Builder()
                .client(getOkHttpClient(headerMap))
                .addConverterFactory(GsonConverterFactory.create(gson))//设置数据解析的工具
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
        return mRetrofit.create(service);
    }
}
