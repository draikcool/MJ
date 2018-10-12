package com.example.hqb98.mj;

import android.app.Application;
import android.content.Context;
import org.litepal.LitePal;

import com.example.hqb98.mj.gson.Information;

public class MyApplication extends Application {

    private static Context context;
    public static String account;
    public static String password;
    public static String semester;
    public static Information information;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);

        semester="2018-2019-1";
    }

    public static Context getContext(){
        return context;
    }
}
