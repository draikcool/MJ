package com.example.hqb98.mj.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hqb98.mj.R;
import com.example.hqb98.mj.data.MTemperature;
import com.example.hqb98.mj.data.MTemperatureAdapter;

import java.util.ArrayList;
import java.util.List;

import static org.litepal.LitePal.findAll;

public class MonitorFragment extends Fragment implements View.OnClickListener {
    
    private List<MTemperature> mTemperatures = new ArrayList<MTemperature>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.monitor_layout,container,false);
        initMTemperature();
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.monitor_recycler_temperature);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        MTemperatureAdapter adapter = new MTemperatureAdapter(mTemperatures);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void initMTemperature() {
        MTemperature mTemperature = new MTemperature("半夜12.00",R.drawable.tianqiicon_duoyun_heiye,"10°C");
        MTemperature mTemperature1 = new MTemperature("凌晨1.00",R.drawable.tianqiicon_duoyun_heiye,"10°C");
        MTemperature mTemperature2 = new MTemperature("凌晨2.00",R.drawable.tianqiicon_duoyun_heiye,"10°C");
        MTemperature mTemperature3 = new MTemperature("凌晨3.00",R.drawable.tianqiicon_duoyun_heiye,"10°C");
        MTemperature mTemperature4 = new MTemperature("凌晨4.00",R.drawable.tianqiicon_duoyun_heiye,"10°C");
        MTemperature mTemperature5 = new MTemperature("清晨5.00",R.drawable.tianqiicon_duoyun_heiye,"10°C");
        MTemperature mTemperature6 = new MTemperature("清晨6.00",R.drawable.tianqiicon_duoyun_heiye,"10°C");
        MTemperature mTemperature7 = new MTemperature("早上7.00",R.drawable.tianqiicon_duoyun_heiye,"10°C");
        MTemperature mTemperature8 = new MTemperature("早上8.00",R.drawable.tianqiicon_duoyun_heiye,"10°C");
        MTemperature mTemperature9 = new MTemperature("上午9.00",R.drawable.tianqiicon_duoyun_heiye,"10°C");
        MTemperature mTemperature10 = new MTemperature("上午10.00",R.drawable.tianqiicon_duoyun_heiye,"10°C");
        MTemperature mTemperature11 = new MTemperature("中午11.00",R.drawable.tianqiicon_duoyun_heiye,"10°C");
        MTemperature mTemperature12 = new MTemperature("中午12.00",R.drawable.tianqiicon_duoyun_heiye,"10°C");
        MTemperature mTemperature13 = new MTemperature("中午1.00",R.drawable.tianqiicon_duoyun_heiye,"10°C");
        mTemperatures.add(mTemperature);
        mTemperatures.add(mTemperature1);
        mTemperatures.add(mTemperature2);
        mTemperatures.add(mTemperature3);mTemperatures.add(mTemperature10);
        mTemperatures.add(mTemperature4);
        mTemperatures.add(mTemperature5);mTemperatures.add(mTemperature11);
        mTemperatures.add(mTemperature6);
        mTemperatures.add(mTemperature7);mTemperatures.add(mTemperature12);
        mTemperatures.add(mTemperature8);
        mTemperatures.add(mTemperature9);
        mTemperatures.add(mTemperature13);





    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


        }
    }
}
