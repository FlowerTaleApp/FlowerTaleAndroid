package com.flowertale.flowertaleandroid.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<String> mTimeList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(android.R.id.text2);
        }
    }

    public TimeAdapter(ArrayList<String> timeList){
        mTimeList = timeList;
    }

    public void update(String time){
        mTimeList.add(time);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }

        View view = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String time = mTimeList.get(position);
        holder.time.setText(time);
    }

    @Override
    public int getItemCount() {
        return mTimeList.size();
    }
}
