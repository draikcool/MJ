package com.example.hqb98.mj.data;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hqb98.mj.R;
import com.example.hqb98.mj.activity.DetailDate;

import java.util.List;

public class DateOneAdapter extends RecyclerView.Adapter<DateOneAdapter.ViewHolder> {
    private List<Date> mDateList;

    public DateOneAdapter(List<Date> dateList){
        mDateList = dateList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.date_item_one,viewGroup,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Date date = mDateList.get(position);
                int i = date.getId();
                int[] extra = new int[]{position,i};
                Intent intent = new Intent(v.getContext(), DetailDate.class);
                intent.putExtra("Detail_Date",extra);
                v.getContext().startActivity(intent);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Date date = mDateList.get(i);
        viewHolder.date_image.setImageResource(date.getDate_image());
        viewHolder.date_title.setText(date.getDate_title());
        viewHolder.date_time.setText(date.getDate_time());
    }

    @Override
    public int getItemCount() {
        return mDateList.size();
    }

    static public class ViewHolder extends RecyclerView.ViewHolder {
        View dateView;
        ImageView date_image;
        TextView date_title;
        TextView date_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateView = itemView;
            date_image = (ImageView)itemView.findViewById(R.id.date_type);
            date_title = (TextView)itemView.findViewById(R.id.date_title);
            date_time = (TextView)itemView.findViewById(R.id.date_time);
        }
    }

    public void addData(int position,Date date){
        mDateList.add(date);
        notifyItemInserted(position);
    }

    public void updateData(int position,Date date){
        mDateList.set(position,date);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }
}
