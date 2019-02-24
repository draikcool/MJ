package com.example.hqb98.mj.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hqb98.mj.R;
import com.example.hqb98.mj.data.StudentInfo;
import com.example.hqb98.mj.data.UserInfo;
import com.example.hqb98.mj.util.HttpUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private EditText accountEdit;
    private EditText passwordEdit;
    private CheckBox remember_password;
    private CheckBox auto_login;
    private Button loginButton;
    private boolean isRemember;
    private boolean isAutoLogin;
    private ProgressBar progressBar;
    private boolean isCreateAccount = false;

    private static final int IFLOGIN = 1234;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case IFLOGIN:
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            finish();
                        }
                    });
                    if (isCreateAccount==false){
                        String userid = preferences.getString("account","");
                        String username = preferences.getString("username","");
                        String password = preferences.getString("password","");
                        HttpUtil.isCreateAccountRequest(userid, username, password, new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("LoginActivitydd",e.toString());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseData = response.body().string();
                                Log.d("LoginActivitydd",responseData);
                                try {
                                    JSONObject jsonObject = new JSONObject(responseData);
                                    String status = jsonObject.getString("status");
                                    if ("1".equals(status)){
                                        Log.d("LoginActivitydd","进来了");
                                        editor.putBoolean("isCreateAccount",false);
                                        editor.apply();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        isCreateAccount = preferences.getBoolean("isCreateAccount",false);
        initView();
    }

    private void initView() {

        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        accountEdit = (EditText)findViewById(R.id.login_account);
        passwordEdit = (EditText)findViewById(R.id.login_password);
        remember_password = (CheckBox)findViewById(R.id.login_ifRemember);
        auto_login = (CheckBox)findViewById(R.id.login_aotoLogin);
        loginButton = (Button)findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        isRemember = preferences.getBoolean("rememberpassword",false);
        isAutoLogin = preferences.getBoolean("autologin",false);
        if (isAutoLogin){
            String account = preferences.getString("account","");
            String password = preferences.getString("password","");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            remember_password.setChecked(true);
            auto_login.setChecked(true);
            editor.putBoolean("rememberpassword",true);
            editor.apply();
            progressBar.setVisibility(View.VISIBLE);

            if_user_and_pw_isRight(account,password);
        }else if (isRemember){
            progressBar.setVisibility(View.GONE);
            String account = preferences.getString("account","");
            String password = preferences.getString("password","");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            remember_password.setChecked(true);
        }else if (!isRemember){
            progressBar.setVisibility(View.GONE);
            editor.clear();
            editor.apply();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button:
                if (loginUser()){
                    progressBar.setVisibility(View.VISIBLE);
                    String account = accountEdit.getText().toString();
                    String password = passwordEdit.getText().toString();
                    if_user_and_pw_isRight(account,password);
                }
                break;

                default:
                    break;
        }
    }

    private void if_user_and_pw_isRight(final String account, final String password){
        HttpUtil.loginRequest(account,password, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                        dialog.setTitle("登陆失败");
                        dialog.setMessage("\n网络出现点小问题，请重新登陆。");
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d("loginss",responseData);
                Gson gson = new Gson();
                StudentInfo studentInfo = gson.fromJson(responseData,StudentInfo.class);
                UserInfo userInfo = studentInfo.getUserInfo();
                int code = studentInfo.getCode();
                if (code==200){
                    editor = preferences.edit();
                    boolean rememberpassword = remember_password.isChecked();
                    boolean autologin = auto_login.isChecked();
                    if (autologin){
                        rememberpassword = true;
                    }
                    editor.putString("account",account);
                    editor.putString("password",password);
                    editor.putBoolean("rememberpassword",rememberpassword);
                    editor.putBoolean("autologin",autologin);
                    editor.putString("birthday",userInfo.getBirthday());
                    editor.putString("class_num",userInfo.getClass_num());
                    editor.putString("major",userInfo.getMajor());
                    editor.putString("username",userInfo.getName());
                    editor.putString("academy",userInfo.getAcademy());
                    editor.apply();
                    Message message = new Message();
                    message.what = IFLOGIN;
                    handler.sendMessage(message);

                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                            dialog.setTitle("登陆失败");
                            dialog.setMessage("\n账号或密码错误，请重新登陆。");
                            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    editor.putString("password","");
                                    editor.apply();
                                }
                            });
                            dialog.show();
                        }
                    });
                }
            }
        });


        //判断是否可以登陆

//        HttpUtil.sendOkHttpRequest("http://120.25.88.41/get_all_kb",account,password, new okhttp3.Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("login",e.toString());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String responseData = response.body().string();
//                int code = Integer.parseInt(responseData.substring(9,12));
//                if (code==200){
//                    Gson gson = new Gson();
//                    MyApplication.information = gson.fromJson(responseData, Information.class);
//                    if("所有课表查询成功".equals(MyApplication.information.getMsg())){
//                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                        startActivity(intent);
//                        boolean rememberpassword = remember_password.isChecked();
//                        boolean autologin = auto_login.isChecked();
//                        if (autologin){
//                            rememberpassword = true;
//                        }
//                        editor = preferences.edit();
//                        editor.putString("account",account);
//                        editor.putString("password",password);
//                        editor.putBoolean("rememberpassword",rememberpassword);
//                        editor.putBoolean("autologin",autologin);
//                        editor.apply();
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                progressBar.setVisibility(View.GONE);
//                                finish();
//                            }
//                        });
//                    }
//                }
//                else {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            progressBar.setVisibility(View.GONE);
//                            AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
//                            dialog.setTitle("登陆失败");
//                            dialog.setMessage("\n账号或密码错误，请重新登陆。");
//                            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    editor.putString("password","");
//                                    editor.apply();
//                                }
//                            });
//                            dialog.show();
//                        }
//                    });
//
//                }
//            }
//        });

    }

    private boolean loginUser() {
        String account = accountEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        if (TextUtils.isEmpty(account)||TextUtils.isEmpty(password)){
            Toast.makeText(this, "账户和密码不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

}
