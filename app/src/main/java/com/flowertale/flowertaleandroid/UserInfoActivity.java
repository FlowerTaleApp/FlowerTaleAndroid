package com.flowertale.flowertaleandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.flowertale.flowertaleandroid.adapter.GroupListAdapter;
import com.flowertale.flowertaleandroid.entity.GroupListItem;

import java.util.ArrayList;
import java.util.List;

public class UserInfoActivity extends AppCompatActivity {
    private List<GroupListItem> groupListItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initGroupList();
        RecyclerView recyclerView = findViewById(R.id.user_info_group_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        GroupListAdapter adapter = new GroupListAdapter(groupListItems);
        recyclerView.setAdapter(adapter);
    }

    private void initGroupList(){
        for(int i = 1;i<=5;i++){
            GroupListItem groupListItem = new GroupListItem("群组"+i);
            groupListItems.add(groupListItem);
        }
    }
}
