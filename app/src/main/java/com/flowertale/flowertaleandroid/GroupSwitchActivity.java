package com.flowertale.flowertaleandroid;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flowertale.flowertaleandroid.DTO.SimpleTeamDTO;
import com.flowertale.flowertaleandroid.DTO.response.BaseResponse;
import com.flowertale.flowertaleandroid.service.FlowerTaleApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupSwitchActivity extends AppCompatActivity {

    private String currentGroup;
    /*private String[] groupItems = {"group1","group2","group3"};*/
    private List<String> groupItemList = new ArrayList<>();
    private List<Integer> teamIdList  = new ArrayList<>();
    private GroupAdapter groupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_switch);
        setFinishOnTouchOutside(true);
        Intent intent = getIntent();
        currentGroup = intent.getStringExtra("teamName");

        initView();
    }

    private void initView(){

        getGroups();

    }


    private class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout groupItem;
        private TextView groupName;
        private ImageView selectIcon;

        public ViewHolder(View view){
            super(view);
            groupItem = (LinearLayout)view;
            groupName = view.findViewById(R.id.group_name);
            selectIcon = view.findViewById(R.id.select_icon);
        }
    }

    private class GroupAdapter extends RecyclerView.Adapter<ViewHolder>{

        private Context mContext;
        private List<String> mGroupItemList;
        private List<Integer> mTeamIdList;                                                          //重写以传递id信息

        public GroupAdapter(List<String> groupItemList, List<Integer> teamIdList){
            mGroupItemList = groupItemList;
            mTeamIdList = teamIdList;
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
                    intent.putExtra("teamId", mTeamIdList.get(index));
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

    private void getGroups(){                                                 //得到所有群组
        FlowerTaleApiService.getInstance().doGetUserTeams().enqueue(new Callback<BaseResponse<List<SimpleTeamDTO>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<SimpleTeamDTO>>> call, Response<BaseResponse<List<SimpleTeamDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        List<SimpleTeamDTO> teams = response.body().getObject();
                        for (int i=0; i<teams.size();i++){
                            groupItemList.add(teams.get(i).getName());
                            teamIdList.add(teams.get(i).getId());
                        }
                        RecyclerView recyclerView = findViewById(R.id.group_view);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(GroupSwitchActivity.this);
                        recyclerView.setLayoutManager(layoutManager);
                        groupAdapter = new GroupAdapter(groupItemList, teamIdList);
                        recyclerView.addItemDecoration(new DividerItemDecoration(GroupSwitchActivity.this, DividerItemDecoration.VERTICAL));
                        recyclerView.setAdapter(groupAdapter);
                    } else {
                        Toast.makeText(GroupSwitchActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(GroupSwitchActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<SimpleTeamDTO>>> call, Throwable t) {
                Toast.makeText(GroupSwitchActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
