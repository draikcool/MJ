package com.example.hqb98.mj.fragment;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hqb98.mj.R;
import com.example.hqb98.mj.activity.MainActivity;
import com.example.hqb98.mj.data.MTemperature;
import com.example.hqb98.mj.data.MTemperatureAdapter;
import com.example.hqb98.mj.data.Picture;
import com.example.hqb98.mj.data.PictureAdapter;
import com.example.hqb98.mj.data.Sensor;
import com.example.hqb98.mj.data.SensorAdapter;
import com.example.hqb98.mj.service.AlarmService;
import com.example.hqb98.mj.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;
import static org.litepal.LitePal.findAll;

public class MonitorFragment extends Fragment implements View.OnClickListener {
    private View view;
    private List<MTemperature> mTemperatures = new ArrayList<MTemperature>();
    private FloatingActionButton floatingActionButton;
    private RecyclerView imageRecycle;
    public static List<Picture> pictureList = new ArrayList<>();
    private PictureAdapter pictureAdapter;
    private TextView temperaturetv;
    private TextView humiditytv;
    private TextView smoketv;
    private List<Sensor> sensorList = new ArrayList<>();
    private SensorAdapter sensorAdapter;
    private Toolbar toolbar;
    private ServiceRecever serviceRecever;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.monitor_layout,container,false);
        initData();
        initView();
        initMTemperature();
        return view;
    }

    private void initData() {
        pictureList.clear();
        pictureList.addAll(LitePal.findAll(Picture.class));
        Collections.reverse(pictureList);

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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            temperaturetv.setText(temperature+"℃");
                            humiditytv.setText(humidity+"%");
                            smoketv.setText(smoke);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void initView() {
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.monitor_fab);
        imageRecycle = (RecyclerView)view.findViewById(R.id.monitor_recycle_image);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        imageRecycle.setLayoutManager(linearLayoutManager);
        pictureAdapter = new PictureAdapter(pictureList);
        imageRecycle.setAdapter(pictureAdapter);
        floatingActionButton.setOnClickListener(this);
        temperaturetv = (TextView)view.findViewById(R.id.monitor_temperature);
        humiditytv = (TextView)view.findViewById(R.id.monitor_humidity);
        smoketv = (TextView)view.findViewById(R.id.monitor_smoke);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.monitor_recycler_temperature);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        sensorAdapter = new SensorAdapter(sensorList);
        recyclerView.setAdapter(sensorAdapter);

        toolbar = (Toolbar)view.findViewById(R.id.toolbar3);
        AppCompatActivity appCompatActivity= (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();


        intentFilter = new IntentFilter("com.example.hqb98.servicebroadcast");
        serviceRecever = new ServiceRecever();
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(serviceRecever,intentFilter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.monitor_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.monitor_alarm:
//                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SYSTEM_ALERT_WINDOW)!= PackageManager.PERMISSION_GRANTED){
//                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW},1);
//                }else{
                    startAlarm();
//                }
                break;
            case R.id.monitor_noalarm:
                Toast.makeText(getContext(),"您关闭了报警装置",Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getContext(),AlarmService.class);
                getContext().stopService(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//
    private void startAlarm(){
        Toast.makeText(getContext(),"您开启了报警装置",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(),AlarmService.class);
        getContext().startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d("jinlaileme","警报");
                    startAlarm();
                }else {
                    Toast.makeText(getContext(),"拒绝了权限可不能使用报警功能噢！",Toast.LENGTH_SHORT).show();
                }
                break;


        }
    }



    private void initMTemperature() {
        HttpUtil.getSensorAll(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getContext(),"网络不好",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("monitordd","wendu");
                String responseData = response.body().string();
                Gson gson = new Gson();
                List<Sensor> sensors = gson.fromJson(responseData,new TypeToken<List<Sensor>>(){}.getType());
                Collections.reverse(sensors);
                for (int i=0;i<15;i++){
                    if (sensors.size()>i&&sensors.get(i)!=null){
                        sensorList.add(sensors.get(i));
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sensorAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.monitor_fab:
                long currentTime = System.currentTimeMillis();
                String timeNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime);
                Picture picture = new Picture();
                picture.setUrl("http://101.132.169.177/testPic/upload/1.jpg");
                picture.setTime(timeNow);
                picture.save();
                pictureList.add(0,picture);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("monitordd","gengxin");
                        pictureAdapter.notifyItemInserted(0);
//                        pictureAdapter.notifyDataSetChanged();
                        imageRecycle.scrollToPosition(0);
                    }
                });

//
//                OkHttpClient client = new OkHttpClient();
//                Request request = new Request.Builder()
//                        .url("http://guolin.tech/api/bing_pic")
//                        .build();
//                client.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        Toast.makeText(getContext(),"网络不好",Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//
//                        String responseData = response.body().string();
//                        long currentTime = System.currentTimeMillis();
//                        String timeNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime);
//                        Picture picture = new Picture();
//                        picture.setUrl(responseData);
//                        picture.setTime(timeNow);
//                        picture.save();
//                        pictureList.add(0,picture);
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Log.d("monitordd","gengxin");
//                                pictureAdapter.notifyDataSetChanged();
//                                imageRecycle.scrollToPosition(0);
//                            }
//                        });
//                    }
//                });
                break;


        }
    }

    class ServiceRecever extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            final Vibrator vibrator = (Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE);
            vibrator.vibrate(new long[]{1000,1000,1000,1000},2);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle("警告")
                    .setMessage("\n检测到家里烟雾情况出现异常！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            vibrator.cancel();
                        }
                    });
            builder.create().show();
        }
    }
}
