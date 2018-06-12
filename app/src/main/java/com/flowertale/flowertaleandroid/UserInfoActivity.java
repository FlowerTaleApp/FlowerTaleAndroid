package com.flowertale.flowertaleandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.flowertale.flowertaleandroid.DTO.UserDTO;
import com.flowertale.flowertaleandroid.DTO.response.BaseResponse;
import com.flowertale.flowertaleandroid.adapter.GroupListAdapter;
import com.flowertale.flowertaleandroid.entity.GroupListItem;
import com.flowertale.flowertaleandroid.entity.User;
import com.flowertale.flowertaleandroid.service.FlowerTaleApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends AppCompatActivity {
    private List<GroupListItem> groupListItems = new ArrayList<>();
    private User user = new User();
    /**
     * FlowerTale
     */
    private TextView mNavigationUserName;
    /**
     * FlowerTale
     */
    private TextView mUserName;
    /**
     * 377955050@qq.com
     */
    private TextView mUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        getUserInfo();
        initGroupList();
        RecyclerView recyclerView = findViewById(R.id.user_info_group_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        GroupListAdapter adapter = new GroupListAdapter(groupListItems);
        recyclerView.setAdapter(adapter);
    }

    private void getUserInfo(){
        // get user info
        FlowerTaleApiService.getInstance().getUserInfo().enqueue(new Callback<BaseResponse<UserDTO>>() {
            @Override
            public void onResponse(Call<BaseResponse<UserDTO>> call, Response<BaseResponse<UserDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        UserDTO userDTO = response.body().getObject();
                        user.setUserName(userDTO.getUsername());
                        user.setUserEmail(userDTO.getEmail());
                        initView();
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //TODO
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<UserDTO>> call, Throwable t) {
                //TODO
            }
        });
    }

    private void initGroupList() {
        for (int i = 1; i <= 5; i++) {
            GroupListItem groupListItem = new GroupListItem("群组" + i);
            groupListItems.add(groupListItem);

        }
    }

    private void initView() {
        mNavigationUserName = (TextView) findViewById(R.id.navigation_user_name);
        mUserName = (TextView) findViewById(R.id.user_name);
        mUserEmail = (TextView) findViewById(R.id.user_email);
        mNavigationUserName.setText(user.getUserName());
        mUserName.setText(user.getUserName());
        mUserEmail.setText(user.getUserEmail());
    }
}
