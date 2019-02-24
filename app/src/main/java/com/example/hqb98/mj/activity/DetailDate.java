package com.example.hqb98.mj.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.hqb98.mj.R;
import com.example.hqb98.mj.data.Date;

import org.litepal.LitePal;

import java.util.Calendar;

public class DetailDate extends AppCompatActivity implements View.OnClickListener {

    private Button complete;
    private ImageView cancel;
    private EditText detail_title;
    private EditText detail_content;
    private Button delete;
    private Intent intent;
    private Date date;
    private int position;
    private int id;

    private Calendar cal;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_date);
        initView();

    }

    private void initView() {
        complete = (Button)findViewById(R.id.detail_update);
        cancel = (ImageView)findViewById(R.id.detail_cancel);
        detail_title = (EditText)findViewById(R.id.detail_title);
        detail_content = (EditText)findViewById(R.id.detail_content);
        delete = (Button)findViewById(R.id.detail_delete);
        complete.setOnClickListener(this);
        cancel.setOnClickListener(this);
        delete.setOnClickListener(this);
        intent = getIntent();
        int[] d = intent.getIntArrayExtra("Detail_Date");
        position = d[0];
        id = d[1];
        date = LitePal.find(Date.class,id);
        detail_title.setText(date.getDate_title());
        detail_content.setText(date.getDate_content());
        cal =Calendar.getInstance();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.detail_update:
                StringBuilder sb = new StringBuilder();
                sb.append(getMonth()).append(".").append(getDay()).append(" ").append(getWeek(Calendar.DAY_OF_WEEK)).append(" ").append(getHour()).append(":").append(getMinute());
                date.setDate_time(sb.toString());
                date.setDate_title(detail_title.getText().toString());
                date.setDate_content(detail_content.getText().toString());
                date.update(id);
//                dateAdapter.updateData(position,date);
                final Intent intent = new Intent("com.example.hqb98.mj.activity.DetailDate");
                int[] extradata = new int[]{-1,position,id};
                intent.putExtra("POSITION",extradata);
                localBroadcastManager.sendBroadcast(intent);        //发送本地广播
                finish();
                break;
            case R.id.detail_delete:
                AlertDialog.Builder dialog = new AlertDialog.Builder(DetailDate.this);
                dialog.setTitle("提示");
                dialog.setMessage("确定要删除这个吗？");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LitePal.delete(Date.class,id);
                        Intent intent1 =new Intent("com.example.hqb98.mj.activity.DetailDate");
                        int[] extradata = new int[]{-2,position,id};
                        intent1.putExtra("POSITION",extradata);
                        localBroadcastManager.sendBroadcast(intent1);
                        finish();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                break;
            case R.id.detail_cancel:
                finish();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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
        return cal.get(java.util.Calendar.MONTH);
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
