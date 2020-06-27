package com.example.testone.Net;

import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.example.testone.Model.Responses;
import okhttp3.*;
//import okhttp3.FormBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;

public class OkHttpHelper {
    private static final String TAG = "OkHttpHelper";
    private static final int TIMEOUT = 5000;
    //public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8"); MediaType.parse("application/octet-stream")


    private static OkHttpClient okHttpClient = simpleClient();

    public static <T> Responses<T>  get(String url, Type type) {
        // okHttpClient = simpleClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        return go(okHttpClient, request, type);
    }

    public static Response get(String url,long length){
        Request request=new Request.Builder()
                //断点下载,指定从哪个字节开始下载
                .addHeader("RANGE","bytes="+length +"-")
                .url(url)
                .build();
        Response response=null;
        try{
            response = okHttpClient.newCall(request).execute();
        }
        catch (IOException e){
            e.printStackTrace();
        }


        return response;
    }
    public static long getContentLength(String url){
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                response.close();
                //这里返回的，就是资源文件的大小
                return contentLength;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;

    }

    public static <T> Responses<T> post(String url, Type type, File file) {
        RequestBody filebody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        Log.d("file:",file.getName());
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), filebody)
                .build();
        Log.d("file:",file.getName());
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return go(okHttpClient, request, type);
    }
//.addFormDataPart("file", file.getName(), filebody)


    public static <T> Responses<T> post(String url, Type type, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : params.keySet()) {
            builder.add(key, Objects.requireNonNull(params.get(key)));
        }
        RequestBody body = builder.build();
        //OkHttpClient okHttpClient = simpleClient();
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return go(okHttpClient, request, type);
    }

    private static <T> Responses<T> go(OkHttpClient okHttpClient, Request request, Type type) {
        try {
            Call call = okHttpClient.newCall(request);
            Response response=call.execute();
            String result = response.body().string();
            Log.d(TAG,"http的code: "+ response.code());
            Log.d(TAG, "url: " + request.url() + "; response: " + result);
            return new Gson().fromJson(result, type);
        } catch (Exception e) {
            Log.e(TAG, "OkHttp exception, method: %s, url: %s"+ e + " " + request.method() + " " +request.url());
        }
        return null;
    }

    private static OkHttpClient simpleClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }
}
