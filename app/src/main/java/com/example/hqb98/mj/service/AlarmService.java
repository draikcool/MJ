package com.example.hqb98.mj.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.hqb98.mj.activity.MainActivity;
import com.example.hqb98.mj.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AlarmService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.getSensorRequest(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            final String temperature = jsonObject.getString("temperature");
                            final String humidity = jsonObject.getString("humidity");
                            final String smoke = jsonObject.getString("smoke");
                            Log.d("alarmsss",smoke);
//                            final Vibrator vibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
//                            vibrator.vibrate(new long[]{1000,1000,1000,1000},2);
                            if (smoke.equals("dangerous")){
                                Intent intent1 = new Intent("com.example.hqb98.servicebroadcast");
                                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
                                localBroadcastManager.sendBroadcast(intent1);
                            }




//                            if (smoke.equals("dangerous")){
//                            if (true){
//                                AlertDialog dialog = new AlertDialog.Builder(getApplicationContext())
//                                        .setTitle("警告")
//                                        .setMessage("检测到家里烟雾异常")
//                                        .setCancelable(false)
//                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                vibrator.cancel();
//                                                dialog.dismiss();
//                                            }
//                                        })
//                                        .create();
//                                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//                                dialog.show();

//
//

//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        }).start();
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        int oneMinute = 5*1000;
        long triggerAtTime = SystemClock.elapsedRealtime()+oneMinute;
        Intent i = new Intent(this,AlarmService.class);
        PendingIntent pi = PendingIntent.getService(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }
}
