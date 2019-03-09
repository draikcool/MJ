package com.example.hqb98.mj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hqb98.mj.R;
import com.example.hqb98.mj.data.Date;
import com.example.hqb98.mj.fragment.DateFragmentOne;
import com.example.hqb98.mj.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CreatDate extends AppCompatActivity implements View.OnClickListener{

    private Button cancel_button;
    private Button complete_button;
    private EditText activityName;
    private EditText activityAttention;
    static final int COLOR1= Color.parseColor("#ffffff");
    static final int COLOR2= Color.parseColor("#ff9393");
    static java.util.Calendar cal;
    private TextView subTitle;
    private int date_image;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_date);
        preferences = PreferenceManager.getDefaultSharedPreferences(CreatDate.this);
        LitePal.initialize(this);
        initView();
    }

    private void initView() {
        cancel_button=(Button)findViewById(R.id.cancel);
        complete_button=(Button)findViewById(R.id.complete);
        cancel_button.setOnClickListener(this);
        complete_button.setOnClickListener(this);
        activityName = (EditText)findViewById(R.id.activity_name);
        activityAttention = (EditText)findViewById(R.id.activity_attention);
        activityName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(TextUtils.isEmpty(activityName.getText())){
                    complete_button.setEnabled(false);
                    complete_button.setTextColor(COLOR2);
                }else{
                    complete_button.setTextColor(COLOR1);
                    complete_button.setEnabled(true);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        subTitle = (TextView)findViewById(R.id.sub_title);
        Intent intent = getIntent();
        int i = Integer.parseInt(intent.getStringExtra("extra_data"));
        switch (i){
            case 0:
                subTitle.setText("记录心情");
                date_image = R.drawable.mood; break;
            case 1:
                subTitle.setText("生活经验");
                date_image = R.drawable.experience; break;
            case 2:
                subTitle.setText("课堂笔记");
                date_image = R.drawable.note; break;
            case 3:
                subTitle.setText("待办事项");
                date_image = R.drawable.backlog; break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                finish();
                break;
            case R.id.complete:
                LitePal.getDatabase();
                cal = java.util.Calendar.getInstance();
                int i = cal.get(java.util.Calendar.DAY_OF_WEEK);
                String week = getWeek(i);
                StringBuffer date_time = new StringBuffer();
                date_time.append(getMonth()).append(".").append(getDay()).append(" ").append(week).append(" ").append(getHour()).append(":").append(getMinute());
                String userid = preferences.getString("account","");
                String type = subTitle.getText().toString();
                String title = activityName.getText().toString();
                String content = activityAttention.getText().toString();
                String datetime = date_time.toString();
                String remindtime = "null";
                Log.d("createdsds",userid+"  "+type+"  "+title+"  "+content+"  "+datetime+"  "+remindtime);
                HttpUtil.uploadDateRequest(userid, type, title, content, datetime, remindtime, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d("createdsds",responseData);
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            if (jsonObject.getString("status").equals("true")){
                                cal = java.util.Calendar.getInstance();
                                int i = cal.get(java.util.Calendar.DAY_OF_WEEK);
                                String week = getWeek(i);
//                                StringBuffer date_time = new StringBuffer();
//                                date_time.append(getMonth()).append(".").append(getDay()).append(" ").append(week).append(" ").append(getHour()).append(":").append(getMinute());
                                long currentTime = System.currentTimeMillis();
                                String timeNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime);
                                Date date = new Date();//数据库关联类
                                date.setDate_image(date_image);
                                date.setDate_type(subTitle.getText().toString());
                                date.setDate_title(activityName.getText().toString());
                                date.setDate_content(activityAttention.getText().toString());
                                date.setDate_time(timeNow.toString());
                                date.setDate_id(jsonObject.getString("id"));
                                date.save();
                                DateFragmentOne.dateList.add(0,date);
                                Bundle bundle = new Bundle();
                                bundle.putString("Date_type",subTitle.getText().toString());
                                bundle.putString("Add_Date","ADD_DATE");
                                Intent intent = new Intent();
                                intent.putExtras(bundle);
                                setResult(RESULT_OK,intent);
                                finish();
                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(CreatDate.this);
                                        builder.setMessage("网络出现了一点小问题哦~");
                                        builder.show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });


            default:
                break;
        }
    }

    /**
     * 获取星期几
     * @param i
     * @return
     */
    public String getWeek(int i){
        switch (i){
            case 1:
                return "sunday";
            case 2:
                return "monday";
            case 3:
                return "tuesday";
            case 4:
                return "wednesday";
            case 5:
                return "thursday";
            case 6:
                return "friday";
            case 7:
                return "saturday";
            default:
                return "";
        }
    }

    /**
     * 获取当前月份
     */
    public int getMonth(){
        return (cal.get(java.util.Calendar.MONTH)+1);
    }
    /**
     * 获取当前日
     */
    public int getDay(){
        return cal.get(java.util.Calendar.DAY_OF_MONTH);
    }
    /**
     * 获取当前时
     */
    public int getHour(){
        return cal.get(java.util.Calendar.HOUR_OF_DAY);
    }
    /**
     * 获取当前分
     */
    public int getMinute(){
        return cal.get(java.util.Calendar.MINUTE);
    }
}
