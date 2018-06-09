package com.flowertale.flowertaleandroid;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GroupSwitchActivity extends AppCompatActivity {

    private String currentGroup;
    private String[] groupItems = {"group1","group2","group3"};
    private List<String> groupItemList = new ArrayList<>();
    private GroupAdapter groupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_switch);
        setFinishOnTouchOutside(true);
        Intent intent = getIntent();
        currentGroup = intent.getStringExtra("currentGroup");

        initView();
    }

    private void initView(){

        initGroup();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.group_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        groupAdapter = new GroupAdapter(groupItemList);
        recyclerView.setAdapter(groupAdapter);
    }


    private void initGroup(){
        groupItemList.clear();
        for (int i=0;i<5;i++){
            Random random = new Random();
            int index = random.nextInt(groupItems.length);
            groupItemList.add(groupItems[index]);
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout groupItem;
        private TextView groupName;
        private ImageView selectIcon;

        public ViewHolder(View view){
            super(view);
            groupItem = (LinearLayout)view;
            groupName = (TextView)view.findViewById(R.id.group_name);
            selectIcon = (ImageView)view.findViewById(R.id.select_icon);
        }
    }

    private class GroupAdapter extends RecyclerView.Adapter<ViewHolder>{

        private Context mContext;
        private List<String> mGroupItemList;

        public GroupAdapter(List<String> groupItemList){
            mGroupItemList = groupItemList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (mContext == null){
                mContext = parent.getContext();
            }

            View view  = LayoutInflater.from(mContext).inflate(R.layout.group_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            String groupItem = mGroupItemList.get(position);
            holder.groupName.setText(groupItem);
            if (!groupItem.equals(currentGroup))
                holder.selectIcon.setVisibility(View.GONE);

            final int index = position;
            holder.groupItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("groupName", mGroupItemList.get(index));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mGroupItemList.size();
        }
    }
}
