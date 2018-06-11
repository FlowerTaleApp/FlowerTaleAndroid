package com.flowertale.flowertaleandroid.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flowertale.flowertaleandroid.MemberInviteActivity;
import com.flowertale.flowertaleandroid.entity.MemberItem;
import com.flowertale.flowertaleandroid.R;

import java.util.List;

import static com.flowertale.flowertaleandroid.GroupCreateActivity.INVITE;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder>{

    private Context mContext;
    private List<MemberItem> mMemberList;

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView selfImage;
        private TextView memberName;

        public ViewHolder(View view){
            super(view);

            selfImage = view.findViewById(R.id.self_image);
            memberName = view.findViewById(R.id.member_name);
        }
    }

    public MemberAdapter(List<MemberItem> memberItemList){
        mMemberList = memberItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.member_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MemberItem memberItem = mMemberList.get(position);
        holder.memberName.setText(memberItem.getName());
        Glide.with(mContext).load(memberItem.getSelfImage()).into(holder.selfImage);

        if (position == 0 && holder.memberName.getText().toString().equals("新增")){
            holder.selfImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MemberInviteActivity.class);
                    ((Activity)mContext).startActivityForResult(intent, INVITE);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mMemberList.size();
    }


}
