package com.example.hqb98.mj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hqb98.mj.db.Corse;
import com.example.hqb98.mj.gson.Information;
import com.example.hqb98.mj.util.HttpUtil;
import com.google.gson.Gson;

import org.litepal.LitePal;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
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
//                LitePal.getDatabase();
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
        HttpUtil.sendOkHttpRequest("http://120.25.88.41/get_all_kb",account,password, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Gson gson = new Gson();
                MyApplication.information = gson.fromJson(responseData, Information.class);
                if("所有课表查询成功".equals(MyApplication.information.getMsg())){
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    boolean rememberpassword = remember_password.isChecked();
                    boolean autologin = auto_login.isChecked();
                    if (autologin){
                        rememberpassword = true;
                    }
                    editor = preferences.edit();
                    editor.putString("account",account);
                    editor.putString("password",password);
                    editor.putBoolean("rememberpassword",rememberpassword);
                    editor.putBoolean("autologin",autologin);
                    editor.apply();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            finish();
                        }
                    });
                }
            }
        });

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
