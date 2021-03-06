package com.flowertale.flowertaleandroid;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.flowertale.flowertaleandroid.DTO.PlantDTO;
import com.flowertale.flowertaleandroid.DTO.form.PlantForm;
import com.flowertale.flowertaleandroid.DTO.response.BaseResponse;
import com.flowertale.flowertaleandroid.entity.FlowerInfoItem;
import com.flowertale.flowertaleandroid.service.FlowerTaleApiService;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlowerAddActivity extends AppCompatActivity {

    private Intent intent = new Intent();
    public static final int FLOWER_TITLE = 1;
    public static final int FLOWER_MEMBER = 2;
    public int count;
    private LinearLayout flowerTypeLayout;
    private LinearLayout flowerTitleLayout;
    private LinearLayout flowerMemberLayout;
    private String flowerName;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case FLOWER_TITLE:
                    flowerTypeLayout.setVisibility(View.GONE);
                    flowerTitleLayout.setVisibility(View.VISIBLE);
                    count++;
                    break;
                case FLOWER_MEMBER:
                    flowerTitleLayout.setVisibility(View.GONE);
                    flowerMemberLayout.setVisibility(View.VISIBLE);
                    count++;
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_add);
        count = 0;
        initView();
    }

    private void initView(){
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        flowerTypeLayout = findViewById(R.id.flower_type_layout);
        flowerTitleLayout = findViewById(R.id.flower_title_layout);
        flowerMemberLayout = findViewById(R.id.flower_member_layout);
        flowerTypeLayout.setVisibility(View.VISIBLE);
        flowerTitleLayout.setVisibility(View.GONE);
        flowerMemberLayout.setVisibility(View.GONE);

        //FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.done_fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.flower_add_menu,menu);
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
            case R.id.next:
                if (count==0){
                    item.setIcon(R.drawable.ic_done_pink);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            TextInputEditText textInputEditText = findViewById(R.id.flower_type);
                            /*intent.putExtra("flowerType", textInputEditText.getText().toString());*/

                            flowerName = textInputEditText.getText().toString();
                            Message message = new Message();
                            message.what = FLOWER_TITLE;
                            handler.sendMessage(message);
                        }
                    }).start();
                }else if (count==1){
                    item.setIcon(R.drawable.ic_done);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            TextInputEditText textInputEditText = findViewById(R.id.flower_title);
                            /*intent.putExtra("flowerTitle", textInputEditText.getText().toString());*/

                            PlantForm plantForm = new PlantForm();
                            plantForm.setDescription(textInputEditText.getText().toString());
                            plantForm.setName(flowerName);
                            int teamId = getIntent().getIntExtra("teamId",-1);
                            plantForm.setTeamId(teamId);

                            doCreate(plantForm);
                            /*Message message = new Message();
                            message.what = FLOWER_MEMBER;
                            handler.sendMessage(message);*/
                        }
                    }).start();
                }else if(count==2){
                    TextInputEditText textInputEditText = findViewById(R.id.flower_member);
                    intent.putExtra("flowerMember", textInputEditText.getText().toString());
                    Log.d("FlowerAddActivity", textInputEditText.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void doCreate(final PlantForm plantForm){
        FlowerTaleApiService.getInstance().doCreatePlant(plantForm).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        intent.putExtra("teamId", plantForm.getTeamId());
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(FlowerAddActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(FlowerAddActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(FlowerAddActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
