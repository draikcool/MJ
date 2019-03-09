package com.example.hqb98.mj.util;


import android.app.AlertDialog;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Vibrator;

import android.util.Log;

import org.litepal.LitePal;

import com.example.hqb98.mj.data.Information;

public class MyApplication extends Application {

    private static Context context;
    public static String account;
    public static String password;

    public static Information information;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);

    }

    public static Context getContext(){
        return context;
    }


}
