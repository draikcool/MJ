package com.example.hqb98.mj.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hqb98.mj.R;
import com.example.hqb98.mj.data.Date;
import com.example.hqb98.mj.data.DateOneAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shizhefei.fragment.LazyFragment;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Date_item_fragment_one extends LazyFragment {
    public static final int LOAD_DATA = 123;
    private View view;
    public static RecyclerView recyclerView;
    public static List<Date> dateList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    public static DateOneAdapter adapter = new DateOneAdapter(dateList);;
    public static ImageView kong_image;
    private RefreshLayout refreshLayout;
    private LocalBroadcastManager localBroadcastManager;
    private DetailReceiver detailReceiver;
    private IntentFilter intentFilter;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case LOAD_DATA:
                    initData1();
//                    initData();
                    initView1();
                    break;
            }
        }
    };

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview,container,false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_date_item);
        //初始化在handle进行
        Message message = new Message();
        message.what = LOAD_DATA;
        handler.sendEmptyMessageDelayed(message.what,500);

    }

    private void initView1() {
        recyclerView = (RecyclerView)findViewById(R.id.date_item1_recycle);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);
        kong_image = (ImageView)findViewById(R.id.date_kongkongruye);
        checkLength();
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        refreshLayout = (RefreshLayout)findViewById(R.id.date_refresh);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                Intent intent = new Intent("com.example.hqb98.mj.activity.requestdate");
                localBroadcastManager.sendBroadcast(intent);
                refreshLayout.finishRefresh();//传入false表示刷新失败
//                refreshDate();
//                refreshData1();
            }

        });
        detailReceiver = new DetailReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.hqb98.mj.activity.detaildate.one");
        localBroadcastManager.registerReceiver(detailReceiver,intentFilter);

    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
//        refreshData1();
    }

    private void initData1(){

        dateList.clear();
        for (int i=0;i<DateFragmentOne.dateList.size();i++){
            if (DateFragmentOne.dateList.get(i).getDate_type().equals("记录心情")){
                dateList.add(DateFragmentOne.dateList.get(i));
            }
        }

        adapter.notifyDataSetChanged();
    }

    //    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_date_item,container,false);
//        initData();
//        initView();
//        return view;
//    }

//    private void initData() {
//        dateList.clear();
//        dateList.addAll(LitePal.where("date_type = '记录心情'").order("id desc").find(Date.class));
//        Log.d("dateList",dateList.size()+" changdu");
//
//    }

//    private void initView() {
//        recyclerView = (RecyclerView) view.findViewById(R.id.date_item1_recycle);
//        linearLayoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(linearLayoutManager);
//        adapter = new DateOneAdapter(dateList);
//        recyclerView.setAdapter(adapter);
//        kong_image = (ImageView)view.findViewById(R.id.date_kongkongruye);
//        checkLength();
//        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
//        refreshLayout = (RefreshLayout)view.findViewById(R.id.date_refresh);
//        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshLayout) {
//                Intent intent = new Intent("com.example.hqb98.mj.activity.requestdate");
//                localBroadcastManager.sendBroadcast(intent);
//                refreshLayout.finishRefresh();//传入false表示刷新失败
//                refreshDate();
//            }
//        });
//
//    }



    public static void refreshData1(){
        dateList.clear();
        for (int i=0;i<DateFragmentOne.dateList.size();i++){
            if (DateFragmentOne.dateList.get(i).getDate_type().equals("记录心情")){
                Log.d("date_itemtt","记录ss");
                dateList.add(DateFragmentOne.dateList.get(i));
            }
        }
//        checkLength();
        adapter.notifyDataSetChanged();
    }

//    public static void refreshDate(){
//        List<Date> dates = new ArrayList<>();
//        dates.addAll(LitePal.where("date_type = '记录心情'").order("id desc").find(Date.class));
//        if (dates.size()!=0){
//            dateList.clear();
//            dateList.addAll(dates);
//            Log.d("date_one","刷新了");
//            adapter.notifyDataSetChanged();
//        }
//    }

    public static void checkLength(){
        if (dateList.size()<=0){
            kong_image.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }else {
            kong_image.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public  class DetailReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int[] i = intent.getIntArrayExtra("POSITION");

            if (i[0]==-1){
                int id = i[2];
                Date date = LitePal.find(Date.class,id);

                dateList.set(i[1],date);
            }else if (i[0]==-2){
                dateList.remove(i[1]);
                adapter.notifyItemRemoved(i[1]);
//                adapter.notifyItemRangeChanged(i[1],dateList.size()-i[1]);
                adapter.notifyDataSetChanged();

            }
        }
    }
}
