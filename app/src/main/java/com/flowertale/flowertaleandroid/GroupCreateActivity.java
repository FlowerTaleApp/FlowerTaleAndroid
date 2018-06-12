package com.flowertale.flowertaleandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flowertale.flowertaleandroid.DTO.SimpleUserDTO;
import com.flowertale.flowertaleandroid.DTO.UserDTO;
import com.flowertale.flowertaleandroid.DTO.response.BaseResponse;
import com.flowertale.flowertaleandroid.adapter.MemberAdapter;
import com.flowertale.flowertaleandroid.entity.MemberItem;
import com.flowertale.flowertaleandroid.service.FlowerTaleApiInterface;
import com.flowertale.flowertaleandroid.service.FlowerTaleApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupCreateActivity extends AppCompatActivity {

    public static final int INVITE = 1;
    private int[] selfImages = {R.drawable.sunflower};
    /*private MemberItem[] memberItems = {new MemberItem(R.drawable.flower, "FlowerTale1"), new MemberItem(R.drawable.flower2, "FlowerTale2"),
                                        new MemberItem(R.drawable.sunflower, "FlowerTale3")};*/
    private List<MemberItem> memberItemList = new ArrayList<>();
    private MemberInvitedAdapter memberAdapter;
    private EditText groupName;
    private int mem_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);

        initView();
    }

    private void initView(){
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.group_create_title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        /*MemberItem memberAdd = new MemberItem(R.drawable.member_add, getString(R.string.member_add));
        memberItemList.add(0,memberAdd);*/
        setSelf();

    }

    /*private void initMem(){
        memberItemList.clear();
        MemberItem memberAdd = new MemberItem(R.drawable.member_add, getString(R.string.member_add));
        memberItemList.add(0,memberAdd);
        for (int i=0;i<5;i++){
            Random random = new Random();
            int index = random.nextInt(memberItems.length);
            memberItemList.add(memberItems[index]);
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_create_menu, menu);
        for(int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(getColor(R.color.mypink)), 0, spanString.length(), 0); //fix the color to white
            item.setTitle(spanString);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.create_finish:
                groupName = (EditText)findViewById(R.id.group_name);                            //创建完成
                if (groupName.getText().toString().equals("")){
                    Toast.makeText(this, "请输入群组名", Toast.LENGTH_SHORT).show();
                }else{
                    String teamName = groupName.getText().toString();
                    String description = "this is a description";
                    doCreate(teamName, description);
                    finish();
                }
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case INVITE:
                if (resultCode == RESULT_OK){
                    MemberItem memInvited = (MemberItem) data.getSerializableExtra("memInvited");   //接收邀请的人
                    if (mem_num>=10){
                        Toast.makeText(this, "人数已满", Toast.LENGTH_SHORT).show();
                    }else{
                        mem_num++;
                        memberItemList.add(memInvited);
                        memberAdapter.notifyDataSetChanged();
                    }
                }
                break;
            default:
        }
    }

    class MemberInvitedAdapter extends RecyclerView.Adapter<MemberInvitedAdapter.ViewHolder>{

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

        public MemberInvitedAdapter(List<MemberItem> memberItemList){
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

    private void setSelf(){                                                     //创建群组时先将自己加入
        FlowerTaleApiService.getInstance().getUserInfo().enqueue(new Callback<BaseResponse<UserDTO>>() {
            @Override
            public void onResponse(Call<BaseResponse<UserDTO>> call, Response<BaseResponse<UserDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        UserDTO user = response.body().getObject();
                        Random random = new Random();
                        int index = random.nextInt(selfImages.length);
                        MemberItem memberItem = new MemberItem(selfImages[index], user.getUsername());
                        memberItemList.add(memberItem);
                        mem_num++;
                        /*initMem();*/
                        /*mem_num = memberItemList.size()-1;*/
                        TextView currentMemNum = findViewById(R.id.current_mem_num);
                        currentMemNum.setText(mem_num+"/10");
                        RecyclerView recyclerView = findViewById(R.id.group_create_view);
                        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(layoutManager);
                        memberAdapter = new MemberInvitedAdapter(memberItemList);
                        recyclerView.setAdapter(memberAdapter);
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<UserDTO>> call, Throwable t) {
                Toast.makeText(GroupCreateActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void doCreate(String groupName, String description){                                //创建群组
        FlowerTaleApiService.getInstance().doCreateTeam(groupName, description).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        Toast.makeText(GroupCreateActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(GroupCreateActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
