package com.example.hqb98.mj;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hqb98.mj.data.DateAdapter;
import com.example.hqb98.mj.db.Date;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

import static org.litepal.LitePal.findAll;

public class DateFragment extends Fragment {
    private List<Date> dateList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private DateAdapter adapter;
    private DetailReceiver detailReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.date_layout,container,false);
        initDate();
        recyclerView= (RecyclerView)view.findViewById(R.id.date_RecycleView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DateAdapter(dateList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void initDate() {
        LitePal.initialize(getActivity());
        dateList =  LitePal.findAll(Date.class);
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.hqb98.mj.DetailDate");
        detailReceiver = new DetailReceiver();
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());    //获取广播管理器实例
        localBroadcastManager.registerReceiver(detailReceiver,intentFilter);    //注册本地广播监听器

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void addData(){
        dateList.add(LitePal.findLast(Date.class));
        adapter.notifyItemInserted(dateList.size()-1);
        recyclerView.scrollToPosition(dateList.size()-1);
    }

    public void removeData(){

    }

    public  class DetailReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int[] i = intent.getIntArrayExtra("POSITION");

            if (i[0]==-1){
                int id = i[2];
                Date date = LitePal.find(Date.class,id);
                dateList.set(i[1],date);
                adapter.notifyItemChanged(i[1]);
            }else if (i[0]==-2){
                int id=i[2];
                LitePal.delete(Date.class,id);
                dateList.remove(i[1]);
                adapter.notifyItemRemoved(i[1]);
                adapter.notifyItemRangeChanged(i[1],dateList.size()-i[1]);
                adapter.notifyDataSetChanged();
            }


        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        localBroadcastManager.unregisterReceiver(detailReceiver);
    }
}
