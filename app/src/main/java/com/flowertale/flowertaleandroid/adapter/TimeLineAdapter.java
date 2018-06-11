package com.flowertale.flowertaleandroid.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flowertale.flowertaleandroid.Enum.NurtureType;
import com.flowertale.flowertaleandroid.entity.FlowerRecord;
import com.flowertale.flowertaleandroid.R;
import com.flowertale.flowertaleandroid.util.EnumUtil;
import com.flowertale.flowertaleandroid.util.VectorDrawableUtils;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder>{

    private Context mContext;
    private List<FlowerRecord> mRecordList;

    class ViewHolder extends RecyclerView.ViewHolder{

        private TimelineView mTimelineView;
        private TextView recordContent;
        private TextView recordName;
        private TextView recordDateTime;
        private TextView recordType;
        private ImageView recordImage;

        public ViewHolder(View view, int viewType){
            super(view);
            recordContent = view.findViewById(R.id.record_content);
            recordName = view.findViewById(R.id.record_name);
            recordDateTime = view.findViewById(R.id.record_date_time);
            recordType = view.findViewById(R.id.record_type);
            recordImage = view.findViewById(R.id.record_image);
            mTimelineView = view.findViewById(R.id.time_marker);
            mTimelineView.initLine(viewType);
        }
    }

    public TimeLineAdapter(List<FlowerRecord> recordList){
        mRecordList = recordList;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.flower_time_record, parent,false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final FlowerRecord flowerRecord = mRecordList.get(position);
        holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_inactive, android.R.color.darker_gray));
        holder.recordContent.setText(flowerRecord.getDesc());
        holder.recordName.setText(flowerRecord.getName());
        holder.recordDateTime.setText(flowerRecord.getDate());
        holder.recordType.setText(EnumUtil.getByCode(flowerRecord.getType(), NurtureType.class).getMessage());
        Glide.with(mContext).load(flowerRecord.getRecordImage()).into(holder.recordImage);

        holder.recordImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View imgEntryView = inflater.inflate(R.layout.dialog_photo_entry, null); // 加载自定义的布局文件
                final AlertDialog dialog = new AlertDialog.Builder(mContext, R.style.NoBackGroundDialog).create();
                ImageView img = imgEntryView.findViewById(R.id.large_image);
                Glide.with(mContext).load(flowerRecord.getRecordImage()).into(img);
                dialog.setView(imgEntryView); // 自定义dialog
                dialog.show();
                imgEntryView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecordList.size();
    }


}
