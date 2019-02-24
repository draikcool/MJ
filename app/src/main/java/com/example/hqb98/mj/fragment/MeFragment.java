package com.example.hqb98.mj.fragment;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hqb98.mj.util.ActivityCollector;
import com.example.hqb98.mj.R;
import com.example.hqb98.mj.activity.LoginActivity;
import com.example.hqb98.mj.data.StudentInfo;
import com.example.hqb98.mj.util.HttpUtil;

import java.util.List;

public class MeFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView me_name;
    private TextView me_number;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String loginString;
    private StudentInfo studentInfo;
//    private LinearLayout jiankongqingkuang;
    private LinearLayout fankuigeikaifazhe;
    private LinearLayout qiehuanzhanghao;
    private LinearLayout tuichudenglu;
    private LinearLayout jianchagengxin;
//    private TextView temperature;
//    private TextView humidity;
//    private TextView smoke;
    private String temperaturedata;
    private String humiditydata;
    private String smokedata;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.me_layout,container,false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = preferences.edit();
        loginString = preferences.getString("loginString","");
        studentInfo = HttpUtil.handleLoginString(loginString);


        initView();
//        initData();
        return view;
    }
//
//    private void initData() {
//        HttpUtil.sendSensorRequest("http://101.132.169.177/magicmirror/genxin/sensor.php", new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Toast.makeText(getContext(),"获取室内温湿度失败!",Toast.LENGTH_SHORT).show();
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        temperature.setText("-1");
//                        humidity.setText("-1");
//                        smoke.setText("wrong");
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String responseData = response.body().string();
//                try {
//                    JSONObject jsonObject = new JSONObject(responseData);
//                    temperaturedata = jsonObject.getString("temperature");
//                    humiditydata = jsonObject.getString("humidity");
//                    smokedata = jsonObject.getString("smoke");
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            temperature.setText(temperaturedata);
//                            humidity.setText(humiditydata);
//                            smoke.setText(smokedata);
//                        }
//                    });
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    private void initView() {
        me_name = (TextView)view.findViewById(R.id.me_name);
        me_number = (TextView)view.findViewById(R.id.me_number);
        fankuigeikaifazhe = (LinearLayout)view.findViewById(R.id.me_fankuigeikaifazhe);
        qiehuanzhanghao = (LinearLayout)view.findViewById(R.id.me_qiehuanzhanghao);
        tuichudenglu = (LinearLayout)view.findViewById(R.id.me_tuichu);
        jianchagengxin = (LinearLayout)view.findViewById(R.id.me_jianchagengxin);
//        jiankongqingkuang = (LinearLayout)view.findViewById(R.id.me_jiankongqingkuang);
        fankuigeikaifazhe.setOnClickListener(this);
        qiehuanzhanghao.setOnClickListener(this);
        tuichudenglu.setOnClickListener(this);
        jianchagengxin.setOnClickListener(this);
//        jiankongqingkuang.setOnClickListener(this);
        String name = preferences.getString("username","");
        String number = preferences.getString("account","");
        me_name.setText(name);
        me_number.setText("账号："+number);

//        temperature = (TextView)view.findViewById(R.id.me_temperature);
//        humidity = (TextView)view.findViewById(R.id.me_humidity);
//        smoke = (TextView)view.findViewById(R.id.me_smoke);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.me_fankuigeikaifazhe:
                if (isQQAvailable(getContext())){
                    String url="mqqwpa://im/chat?chat_type=wpa&uin=2601407043";
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }else {
                    Toast.makeText(getContext(),"您未安装QQ",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.me_qiehuanzhanghao:
                ActivityCollector.finishAll();
                Intent intent = new Intent(getContext(),LoginActivity.class);
                startActivity(intent);
                editor = preferences.edit();
                editor.putBoolean("autologin",false);
                editor.apply();
                break;
            case R.id.me_tuichu:
                LinearLayout popup = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.me_exit,null);
                final AlertDialog.Builder dialog= new AlertDialog.Builder(getContext());
                dialog.setView(popup)
                        .create()
                        .show();
                Button ok = (Button)popup.findViewById(R.id.me_ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCollector.finishAll();
                    }
                });
                Button cancel = (Button)popup.findViewById(R.id.me_cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(){
                            @Override
                            public void run() {
                                Instrumentation inst = new Instrumentation();
                                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                            }
                        }.start();
                    }
                });
                break;
            case R.id.me_jianchagengxin:
                Toast.makeText(getContext(),"现在已经是最新版本了哦",Toast.LENGTH_SHORT).show();
                break;
//            case R.id.me_jiankongqingkuang:
//                Intent intent1 = new Intent(getContext(),MeMonitor.class);
//                startActivity(intent1);
//
//                break;
                default:

                    break;
        }
    }
    public static boolean isQQAvailable(Context context) {
        final PackageManager mPackageManager = context.getPackageManager();
        List<PackageInfo> pinfo = mPackageManager.getInstalledPackages(0);
        if(pinfo !=null) {
            for(int i =0;i < pinfo.size();i++) {
                String pn = pinfo.get(i).packageName;
                if(pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
}
