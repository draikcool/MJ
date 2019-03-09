package com.example.hqb98.mj.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hqb98.mj.R;
import com.example.hqb98.mj.activity.CreatDate;
import com.example.hqb98.mj.data.DateAdapter;
import com.example.hqb98.mj.data.Date;
import com.example.hqb98.mj.data.DateHttp;
import com.example.hqb98.mj.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static org.litepal.LitePal.find;
import static org.litepal.LitePal.findAll;

public class DateFragment extends Fragment implements View.OnClickListener {
    public static final int TOAST = 123;
    public static final int UPDATE_DATE = 1234;
    private List<Date> dateList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private DateAdapter adapter;
    private DetailReceiver detailReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    private ImageView add_date;
    private View view;
    private ImageView kongkongruye;
    private SharedPreferences preferences;
    private List<DateHttp> dateHttps = new ArrayList<DateHttp>();
    private RefreshLayout refreshLayout;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case TOAST:
                    Toast.makeText(getContext(),"网络出现了一点小问题哦~",Toast.LENGTH_SHORT).show();
                    break;
                case UPDATE_DATE:
                    addData();
                    break;
            }
        }

    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.date_layout,container,false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        refreshLayout = (RefreshLayout)view.findViewById(R.id.date_refresh);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                requestData();
                refreshLayout.finishRefresh();//传入false表示刷新失败
            }
        });
        kongkongruye = (ImageView) view.findViewById(R.id.date_kongkongruye);
        initDate();
        recyclerView= (RecyclerView)view.findViewById(R.id.date_RecycleView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DateAdapter(dateList);
        Log.d("datefragmentdsds","前"+dateList.toString());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        requestData();
        return view;
    }

    private void requestData() {
        String userid = preferences.getString("account","");
        Log.d("getDate",userid);
        HttpUtil.getDateRequest(userid, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("getDate",e.toString());
                Message message = new Message();
                message.what = TOAST;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                dateHttps.clear();
                Gson gson = new Gson();
                dateHttps = gson.fromJson(responseData,new TypeToken<List<DateHttp>>(){}.getType());
                if (dateHttps.size()!=0){
                    LitePal.deleteAll(Date.class);
                    for (int i=0;i<dateHttps.size();i++){
                        Date date = new Date();
                        date.setDate_type(dateHttps.get(i).getType());
                        date.setDate_title(dateHttps.get(i).getTitle());
                        date.setDate_content(dateHttps.get(i).getContent());
                        date.setDate_time(dateHttps.get(i).getDatetime());
                        switch (dateHttps.get(i).getType()){
                            case "记录心情":
                                date.setDate_image(R.drawable.mood);
                                break;
                            case "生活经验":
                                date.setDate_image(R.drawable.experience);
                                break;
                            case "课堂笔记":
                                date.setDate_image(R.drawable.note);
                                break;
                            case "待办事项":
                                date.setDate_image(R.drawable.backlog);
                                break;
                        }
                        date.save();
                    }

                    Message message = new Message();
                    message.what = UPDATE_DATE;
                    handler.sendMessage(message);
                }

            }
        });
    }


    private void initDate() {
        LitePal.initialize(getActivity());
        dateList.addAll(LitePal.findAll(Date.class));
        if (dateList.size()!=0){
            kongkongruye.setVisibility(View.GONE);
        }
        Collections.reverse(dateList);
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.hqb98.mj.activity.DetailDate");
        detailReceiver = new DetailReceiver();
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());    //获取广播管理器实例
        localBroadcastManager.registerReceiver(detailReceiver,intentFilter);    //注册本地广播监听器
        add_date = (ImageView)view.findViewById(R.id.add_date);
        add_date.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void addData(){
        List<Date> dates = LitePal.findAll(Date.class);
        Collections.reverse(dates);
        dateList.clear();
        dateList.addAll(dates);
        adapter.notifyDataSetChanged();
        if (dateList.size()!=0){
            kongkongruye.setVisibility(View.GONE);
        }else {
            kongkongruye.setVisibility(View.VISIBLE);
        }
    }

    public void removeData(){

    }
    int i;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_date:
                String[] itemts = new String[]{"记录心情","生活经验","课堂笔记","待办事项"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("请选择以下一项")
                        .setSingleChoiceItems(itemts, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        i = 0; break;
                                    case 1:
                                        i = 1; break;
                                    case 2:
                                        i = 2; break;
                                    case 3:
                                        i = 3; break;
                                }
                            }
                        });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(),CreatDate.class);
                        intent.putExtra("extra_data",i+"");
                        startActivityForResult(intent,1);
                        dialog.dismiss();
                    }
                });
                builder.create().show();

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode==RESULT_OK){
                    addData();
                }
                break;
        }
    }

    public  class DetailReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
//            int[] i = intent.getIntArrayExtra("POSITION");
//
//            if (i[0]==-1){
//                int id = i[2];
//                Date date = LitePal.find(Date.class,id);
//
//                dateList.set(i[1],date);
//                adapter.notifyItemChanged(i[1]);
//            }else if (i[0]==-2){
//                int id=i[2];
//                LitePal.delete(Date.class,id);
//                dateList.remove(i[1]);
//                if (dateList.size()==0){
//                    kongkongruye.setVisibility(View.VISIBLE);
//                }else {
//                    kongkongruye.setVisibility(View.GONE);
//                }
//                adapter.notifyItemRemoved(i[1]);
//                adapter.notifyItemRangeChanged(i[1],dateList.size()-i[1]);
//                adapter.notifyDataSetChanged();
//            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        localBroadcastManager.unregisterReceiver(detailReceiver);
    }
}
