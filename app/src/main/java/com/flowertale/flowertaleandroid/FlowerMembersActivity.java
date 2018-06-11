package com.flowertale.flowertaleandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flowertale.flowertaleandroid.DTO.DiaryDTO;
import com.flowertale.flowertaleandroid.DTO.PlantDTO;
import com.flowertale.flowertaleandroid.DTO.response.BaseResponse;
import com.flowertale.flowertaleandroid.adapter.MemberAdapter;
import com.flowertale.flowertaleandroid.entity.FlowerRecord;
import com.flowertale.flowertaleandroid.entity.MemberItem;
import com.flowertale.flowertaleandroid.service.FlowerTaleApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.flowertale.flowertaleandroid.GroupCreateActivity.INVITE;

public class FlowerMembersActivity extends AppCompatActivity {

    private static final int SCHEME = 2;
    private int[] selfImages = {R.drawable.flower, R.drawable.flower2, R.drawable.sunflower};
    /*private MemberItem[] memberItems = {new MemberItem(R.drawable.flower, "FlowerTale1"), new MemberItem(R.drawable.flower2, "FlowerTale2"),
            new MemberItem(R.drawable.sunflower, "FlowerTale3")};*/
    private List<MemberItem> memberItemList = new ArrayList<>();
    private MemberAdapter memberAdapter;
    private int mem_num = 0;
    private int plantId;
    private int teamId;
    private int currentSchemeId;
    private String raisingTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_members);

        Intent intent = getIntent();
        plantId = intent.getIntExtra("plantId", -1);
        teamId = intent.getIntExtra("teamId", -1);

        initView();
    }

    private void initView(){
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.group_name));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getPlantName(teamId, plantId);                                                  //设置养护计划名称
        TextView raisingName = findViewById(R.id.raising_title);
        raisingName.setText(raisingTitle);

        final Button quit = findViewById(R.id.delete_and_exit);                               //退出
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitTeam(teamId);
            }
        });

        setCurrentScheme(plantId);
        CardView raisingScheme = findViewById(R.id.raising_scheme);
        raisingScheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlowerMembersActivity.this, RaisingSchemeActivity.class);
                intent.putExtra("schemeId", currentSchemeId);
                intent.putExtra("plantId", plantId);
                intent.putExtra("teamId", teamId);
                startActivityForResult(intent, SCHEME);
            }
        });

        TextView inviteMem = findViewById(R.id.invite_members);
        inviteMem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlowerMembersActivity.this, MemberInviteActivity.class);
                startActivityForResult(intent, INVITE);
            }
        });

        /*initMem();*/
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.members_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        memberAdapter = new MemberAdapter(memberItemList);
        recyclerView.setAdapter(memberAdapter);
    }

    /*private void initMem(){
        memberItemList.clear();
        //MemberItem memberAdd = new MemberItem(R.drawable.member_add, getString(R.string.member_add));
        //memberItemList.add(0,memberAdd);
        for (int i=0;i<5;i++){
            Random random = new Random();
            int index = random.nextInt(memberItems.length);
            memberItemList.add(memberItems[index]);
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
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
            case SCHEME:
                if (resultCode == RESULT_OK){
                    currentSchemeId = data.getIntExtra("schemeId", -1);
                    String currentScheme = data.getStringExtra("currentScheme");
                    TextView raisingSchemeTitle = findViewById(R.id.raising_scheme_title);
                    raisingSchemeTitle.setText(currentScheme);
                }
                break;
            default:
        }
    }

    private void getPlantName(int teamId, final int plantId){                                     //得到养护计划名称
        FlowerTaleApiService.getInstance().doGetPlantsByTeamId(teamId).enqueue(new Callback<BaseResponse<List<PlantDTO>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<PlantDTO>>> call, Response<BaseResponse<List<PlantDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        List<PlantDTO> plantDTOS = response.body().getObject();
                        for (int i=0;i<plantDTOS.size();i++){
                            if (plantDTOS.get(i).getId()==plantId){
                                raisingTitle = plantDTOS.get(i).getDescription();
                            }
                        }
                    } else {
                        Toast.makeText(FlowerMembersActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(FlowerMembersActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<PlantDTO>>> call, Throwable t) {
                Toast.makeText(FlowerMembersActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void quitTeam(int teamId){
        FlowerTaleApiService.getInstance().doLeaveTeam(teamId).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        Toast.makeText(FlowerMembersActivity.this, "您已成功从群组中退出", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(FlowerMembersActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(FlowerMembersActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(FlowerMembersActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setCurrentScheme(int plantId){

    }
}
