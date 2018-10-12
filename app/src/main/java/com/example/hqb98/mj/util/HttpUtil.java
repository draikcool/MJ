package com.example.hqb98.mj.util;

import com.example.hqb98.mj.MyApplication;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {



    public static void sendOkHttpRequest(String url, String name, String password, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5,TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = new FormBody.Builder()
                .add("username",name)
                .add("password",password)
                .add("semester", MyApplication.semester)
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }


}
