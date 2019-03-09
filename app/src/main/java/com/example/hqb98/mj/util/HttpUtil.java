package com.example.hqb98.mj.util;

import com.example.hqb98.mj.data.StudentInfo;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {



    public static void getCourseRequest(String name, String password, String semester, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5,TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = new FormBody.Builder()
                .add("username",name)
                .add("password",password)
                .add("semester", semester)
                .build();
        final Request request = new Request.Builder()
                .url("https://guohe3.com/api/student/getSchoolTimetable")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }

    public static void loginRequest(String username, String password, okhttp3.Callback callback ){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5,TimeUnit.SECONDS)
                .readTimeout(5,TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = new FormBody.Builder()
                .add("username",username)
                .add("password",password)
                .build();
        Request request = new Request.Builder()
                .url("https://guohe3.com//api/student/login")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }


    public static StudentInfo handleLoginString(String string){
        return new Gson().fromJson(string,StudentInfo.class);
    }


    public static void uploadDateRequest(String userid,String type,String title,String content,String datetime,String remindtime,okhttp3.Callback callback){
        String path = "http://101.132.169.177/magicmirror/genxin/get_memo.php?";
        String url = path+"userid="+userid+"&type="+type+"&title="+title+"&content="+content+"&datetime="+datetime+"&remindtime="+remindtime;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(callback);
    }


    public static void isCreateAccountRequest(String userid,String username,String password,okhttp3.Callback callback){
        String path = "http://101.132.169.177/magicmirror/genxin/register.php";
        String url = path+"?userid="+userid+"&username="+username+"&password="+password;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);

    }

    public static void getDateRequest(String userid,okhttp3.Callback callback){
        String path = "http://101.132.169.177/magicmirror/genxin/memo1.php?userid=";
        String url = path + userid;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void deleteDateRequest(String id,String account,okhttp3.Callback callback){
        String url = "http://101.132.169.177/magicmirror/genxin/delete_memo.php?id="+id+"&userid="+account;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void getSensorRequest(okhttp3.Callback callback){
        String url = "http://101.132.169.177/magicmirror/genxin/sensor.php";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void getSensorAll(okhttp3.Callback callback){
        String url = "http://101.132.169.177/magicmirror/genxin/sensor_all.php";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }


}
