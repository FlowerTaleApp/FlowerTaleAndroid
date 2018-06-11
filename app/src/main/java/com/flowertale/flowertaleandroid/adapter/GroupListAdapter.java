package com.flowertale.flowertaleandroid.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flowertale.flowertaleandroid.R;
import com.flowertale.flowertaleandroid.entity.GroupListItem;

import java.util.List;

public class GroupListAdapter  extends RecyclerView.Adapter<GroupListAdapter.ViewHolder>{
    private List<GroupListItem> mGroupLst;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView groupName;

        public ViewHolder(View view){
            super(view);
            groupName = view.findViewById(R.id.user_info_group_name);
        }
    }

    public GroupListAdapter(List<GroupListItem> groupListItems) {
        mGroupLst = groupListItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_info_group_list_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroupListItem groupListItem = mGroupLst.get(position);
        holder.groupName.setText(groupListItem.getGroupName());
    }

    @Override
    public int getItemCount() {
        return mGroupLst.size();
    }
}
