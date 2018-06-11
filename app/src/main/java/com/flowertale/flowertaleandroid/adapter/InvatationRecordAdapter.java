package com.flowertale.flowertaleandroid.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.flowertale.flowertaleandroid.R;
import com.flowertale.flowertaleandroid.entity.InvitationRecord;

import java.util.List;

public class InvatationRecordAdapter extends RecyclerView.Adapter<InvatationRecordAdapter.ViewHolder>{
    private List<InvitationRecord> mInvitationRecordList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View InvitationRecordView;
        TextView inviter;
        TextView groupName;
        Button accept;
        Button reject;
        TextView accept_text;
        TextView reject_text;

        public ViewHolder(View view){
            super(view);
            InvitationRecordView = view;
            inviter = view.findViewById(R.id.group_inviter);
            groupName = view.findViewById(R.id.group_name);
            accept = view.findViewById(R.id.invitation_accept);
            accept_text = view.findViewById(R.id.invitation_info_accept);
            reject = view.findViewById(R.id.invitation_reject);
            reject_text = view.findViewById(R.id.invitation_info_reject);
        }
    }

    public InvatationRecordAdapter(List<InvitationRecord> invitationRecordList) {
         mInvitationRecordList = invitationRecordList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_invitation_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        InvitationRecord invitationRecord = mInvitationRecordList.get(position);
        holder.inviter.setText(invitationRecord.getInviter());
        holder.groupName.setText(invitationRecord.getGroupName());
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.accept.setVisibility(View.GONE);
                holder.reject.setVisibility(View.GONE);
                holder.accept_text.setVisibility(View.VISIBLE);
            }
        });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.accept.setVisibility(View.GONE);
                holder.reject.setVisibility(View.GONE);
                holder.reject_text.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mInvitationRecordList.size();
    }
}
