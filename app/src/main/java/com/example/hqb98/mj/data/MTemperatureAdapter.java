package com.example.hqb98.mj.data;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hqb98.mj.R;

import java.util.List;

public class MTemperatureAdapter extends RecyclerView.Adapter<MTemperatureAdapter.ViewHolder> {
    private List<MTemperature> mTemperatureList;

    public MTemperatureAdapter(List<MTemperature> mTemperatureList) {
        this.mTemperatureList = mTemperatureList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.monitor_temperature_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        MTemperature mTemperature = mTemperatureList.get(i);
        viewHolder.temperatureTime.setText(mTemperature.gettTime());
        viewHolder.temperatureImage.setImageResource(mTemperature.gettImage());
        viewHolder.temperatureTemp.setText(mTemperature.gettTemperature());

    }

    @Override
    public int getItemCount() {
        return mTemperatureList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView temperatureTime;
        ImageView temperatureImage;
        TextView temperatureTemp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            temperatureTime = (TextView)itemView.findViewById(R.id.temperature_time);
            temperatureImage = (ImageView)itemView.findViewById(R.id.temperature_iamge);
            temperatureTemp = (TextView)itemView.findViewById(R.id.temperature_temp);
        }
    }
}
