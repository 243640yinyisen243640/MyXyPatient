/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vice.bloodpressure.retrofit.gson;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private static final String TAG = "xiao";
//    private static final String TAG = GsonResponseBodyConverter.class.getSimpleName();
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String result=value.string();
//        showLog(result);
        return (T) result;
//        String result=value.string();
//        HHSoftLogUtils.i(TAG,"ResponseBody==convert=="+result);
////        result= HHSoftEncryptUtils.decodeAES(result);
////        HHSoftLogUtils.i(TAG,"ResponseBody==convert==decodeAES_B=="+result);
////        return (T) result;
////        return (T) ResponseBody.create(MEDIA_TYPE,result);
////        return (T) gson.fromJson(result, BaseModel.class);
////        JsonReader jsonReader = gson.newJsonReader(value.charStream());
////        try {
////            return adapter.read(jsonReader);
////        } finally {
////            value.close();
////        }
//        return (T) result;

//        JsonReader jsonReader = gson.newJsonReader(value.charStream());
//        try {
//            return adapter.read(jsonReader);  //原支持库没有catch
//        }  catch (Exception e) {
//            e.fillInStackTrace();
//        } finally {
//            value.close();
//        }
//        return null;

    }

    public static void showLog(String msg)
    {  //信息太长,分段打印
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        //  把4*1024的MAX字节打印长度改为2001字符数
        int max_str_length = 2001 - TAG.length();
        //大于4000时
        while (msg.length() > max_str_length)
        {
            Log.i(TAG, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        //剩余部分
        Log.i(TAG,"GsonResponseBodyConverter=="+msg);
    }
}
