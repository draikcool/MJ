package com.example.hqb98.mj;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hqb98.mj.db.Date;

import org.litepal.LitePal;
import org.w3c.dom.Text;

import java.util.List;

import static org.litepal.LitePal.findAll;

public class MonitorFragment extends Fragment implements View.OnClickListener {

    private TextView textView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.monitor_layout,container,false);
        textView = (TextView)view.findViewById(R.id.tv2);
        textView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv2:
                List<Date> dates = LitePal.findAll(Date.class);
                for (int i = 0; i<dates.size();i++){
                    Date date = dates.get(i);
                    Log.d("zzzxxx",date.getId()+"");
                }

        }
    }
}
