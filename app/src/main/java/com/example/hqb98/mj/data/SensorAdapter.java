package com.example.hqb98.mj.data;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hqb98.mj.R;

import java.util.List;

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.ViewHolder> {
    private List<Sensor> sensorList;

    public SensorAdapter(List<Sensor> sensorList) {
        this.sensorList = sensorList;
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
        Sensor sensor = sensorList.get(i);
        Log.d("timeee",sensor.getReg_date());
        String time = sensor.getReg_date().substring(11,16);
        Log.d("timeee",time);
        int hour = Integer.parseInt(time.substring(0,2));
        if (hour>=0&&hour<4){
            viewHolder.temperatureTime.setText("凌晨"+time);
            viewHolder.temperatureImage.setImageResource(R.drawable.tianqiicon_qingtian_heiye);
        }else if (hour>=4&&hour<=6){
            viewHolder.temperatureTime.setText("清晨"+time);
            viewHolder.temperatureImage.setImageResource(R.drawable.tianqiicon_qingtian_heiye);
        }else if (hour>6&&hour<=8){
            viewHolder.temperatureTime.setText("早上"+time);
            viewHolder.temperatureImage.setImageResource(R.drawable.tianqiicon_duoyun_baitian);
        }else if (hour>8&&hour<=11){
            viewHolder.temperatureTime.setText("上午"+time);
            viewHolder.temperatureImage.setImageResource(R.drawable.tianqiicon_duoyun_baitian);
        }else if (hour>11&&hour<=14){
            viewHolder.temperatureTime.setText("中午"+time);
            viewHolder.temperatureImage.setImageResource(R.drawable.tianqiicon_qingtian_baitian);
        }else if (hour>14&&hour<=17){
            viewHolder.temperatureTime.setText("下午"+time);
            viewHolder.temperatureImage.setImageResource(R.drawable.tianqiicon_qingtian_baitian);
        }else if (hour>17&&hour<=20){
            viewHolder.temperatureTime.setText("傍晚"+time);
            viewHolder.temperatureImage.setImageResource(R.drawable.tianqiicon_duoyun_heiye);
        }else{
            viewHolder.temperatureTime.setText("晚上"+time);
            viewHolder.temperatureImage.setImageResource(R.drawable.tianqiicon_duoyun_heiye);
        }
        viewHolder.temperatureTemp.setText(sensor.getTemperature()+"℃");

    }

    @Override
    public int getItemCount() {
        return sensorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView temperatureTime;
        ImageView temperatureImage;
        TextView temperatureTemp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            temperatureTime = (TextView)itemView.findViewById(R.id.temperature_time);
            temperatureImage = (ImageView)itemView.findViewById(R.id.temperature_iamge);
            temperatureTemp = (TextView)itemView.findViewById(R.id.temperature_temp);
        }
    }
}
