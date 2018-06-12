package com.flowertale.flowertaleandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flowertale.flowertaleandroid.DTO.DiaryDTO;
import com.flowertale.flowertaleandroid.DTO.PlantDTO;
import com.flowertale.flowertaleandroid.DTO.response.BaseResponse;
import com.flowertale.flowertaleandroid.Enum.NurtureType;
import com.flowertale.flowertaleandroid.adapter.TimeLineAdapter;
import com.flowertale.flowertaleandroid.entity.FlowerInfoItem;
import com.flowertale.flowertaleandroid.entity.FlowerRecord;
import com.flowertale.flowertaleandroid.service.FlowerTaleApiService;
import com.flowertale.flowertaleandroid.util.EnumUtil;
import com.github.clans.fab.FloatingActionMenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlowerDetailsActivity extends AppCompatActivity {

    private static final int ADD = 1;
    public static final String INFO_TITLE = "info_title";
    public static final String INFO_IMAGE_ID = "info_image_id";

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    private SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat minSdf = new SimpleDateFormat("hh:mm");

    private int[] flowerImages = {R.drawable.flower, R.drawable.flower2};

    //测试数据
    /*private FlowerRecord[] timeLineRecords = {new FlowerRecord(R.drawable.flower,"FlowerTale1", 0, "给花儿浇了些水，显得更精神了", sdf.format(new Date())),
                                              new FlowerRecord(R.drawable.flower2,"FlowerTale2", 1, "给花儿施了点肥，希望它能快些长大", sdf.format(new Date()))};*/
    private List<FlowerRecord> recordList = new ArrayList<>();
    private TimeLineAdapter recordAdapter;
    private int teamId;
    private int plantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_details);
        initView();

    }

    private void initView(){
        Intent intent = getIntent();
        String infoTitle = intent.getStringExtra(INFO_TITLE);
        int infoImageId = intent.getIntExtra(INFO_IMAGE_ID,0);
        teamId = intent.getIntExtra("teamId", -1);
        plantId = intent.getIntExtra("plantId", -1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        ImageView flowerImageView = findViewById(R.id.flower_image_view);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(infoTitle);
        Glide.with(this).load(infoImageId).into(flowerImageView);
        AppBarLayout appBarLayout = findViewById(R.id.appBar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //some other code here
                ViewCompat.setElevation(appBarLayout, 8);
            }
        });

        FloatingActionMenu floatingActionMenu = findViewById(R.id.floating_action_menu);        //浮动菜单
        floatingActionMenu.setClosedOnTouchOutside(true);


        com.github.clans.fab.FloatingActionButton publishRecord = findViewById(R.id.record_add_fab);
        publishRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent publishIntent = new Intent(FlowerDetailsActivity.this, RecordAddActivity.class);
                publishIntent.putExtra("plantId", plantId);
                startActivityForResult(publishIntent, ADD);
            }
        });

        com.github.clans.fab.FloatingActionButton viewAccomplishment = findViewById(R.id.accomplishment);
        viewAccomplishment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent = new Intent(FlowerDetailsActivity.this, AccomplishmentActivity.class);
                viewIntent.putExtra("plantId", plantId);
                startActivity(viewIntent);
            }
        });

        /*initRecords();*/
        RecyclerView recyclerView = findViewById(R.id.raising_details_view);              //养护记录展示
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recordAdapter = new TimeLineAdapter(recordList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recordAdapter);
        doGetDiaries(plantId);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ADD:
                if (resultCode == RESULT_OK){
                    /*Boolean water = data.getBooleanExtra("water", false);
                    Boolean fertilize = data.getBooleanExtra("fertilize",false);
                    Boolean prune = data.getBooleanExtra("prune", false);
                    Boolean sunshine = data.getBooleanExtra("sunshine", false);
                    String description = data.getStringExtra("description");
                    Log.d("FlowerDetailActivity", water+" "+fertilize+" "+prune+" "+sunshine+" "+description);*/
                    plantId = data.getIntExtra("plantId", -1);
                }
                break;
            default:
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.flower_details_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.member:                                                                   //查看群组信息
                Intent intent = new Intent(FlowerDetailsActivity.this,FlowerMembersActivity.class);
                intent.putExtra("teamId", teamId);
                intent.putExtra("plantId", plantId);
                startActivity(intent);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    /*private void initRecords(){
        recordList.clear();
        for (int i = 0; i<15;i++){
            Random random = new Random();
            int index = random.nextInt(timeLineRecords.length);
            recordList.add(timeLineRecords[index]);
        }
    }*/

    private void doGetDiaries(int plantId){                                                 //获取当前植物动态
        FlowerTaleApiService.getInstance().doGetDiariesByPlantId(plantId).enqueue(new Callback<BaseResponse<List<DiaryDTO>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<DiaryDTO>>> call, Response<BaseResponse<List<DiaryDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        List<DiaryDTO> diaryDTOS = response.body().getObject();
                        for (int i=0;i<diaryDTOS.size();i++){
                            Random random = new Random();
                            int index = random.nextInt(flowerImages.length);
                            recordList.add(new FlowerRecord(flowerImages[index], diaryDTOS.get(i).getUsername(), diaryDTOS.get(i).getType(),
                                                            diaryDTOS.get(i).getContent(), sdf.format(diaryDTOS.get(i).getDate())));
                        }
                        recordAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(FlowerDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(FlowerDetailsActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<DiaryDTO>>> call, Throwable t) {
                Toast.makeText(FlowerDetailsActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
