package com.example.hqb98.mj.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hqb98.mj.R;
import com.example.hqb98.mj.data.Date;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class Date_item_fragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private List<Date> dateList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_date_item,container,false);
        initData();
        initView();
        return view;
    }

    private void initData() {
        dateList.clear();
        dateList.addAll(LitePal.where("date_type = '记录心情'").order("id desc").find(Date.class));
        Log.d("dateList",dateList.size()+" changdu");

    }

    private void initView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.date_item1_recycle);


    }
}
