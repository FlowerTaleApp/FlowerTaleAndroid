package com.flowertale.flowertaleandroid;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flowertale.flowertaleandroid.DTO.ItemDTO;
import com.flowertale.flowertaleandroid.DTO.SchemeDTO;
import com.flowertale.flowertaleandroid.DTO.response.BaseResponse;
import com.flowertale.flowertaleandroid.adapter.TimeAdapter;
import com.flowertale.flowertaleandroid.service.FlowerTaleApiService;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RaisingSchemeActivity extends AppCompatActivity {

    private static final int ADD = 1;
    private int schemeId;
    private int plantId;
    private int teamId;
    private List<String> dataset = new ArrayList<>();
    private List<Integer> schemeIdList = new ArrayList<>();
    private List<String> waterList = new ArrayList<>();
    private List<String> fertilizeList = new ArrayList<>();
    private List<String> pruneList = new ArrayList<>();
    private List<String> sunshineList = new ArrayList<>();
    private TimeAdapter waterAdapter;
    private TimeAdapter fertilizeAdapter;
    private TimeAdapter pruneAdapter;
    private TimeAdapter sunshineAdapter;
    private NiceSpinner schemeSpinner;
    private RecyclerView waterDetail;
    private RecyclerView fertilizeDetail;
    private RecyclerView pruneDetail;
    private RecyclerView sunshineDetail;
    private List<String> description = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raising_scheme);

        Intent intent = getIntent();
        schemeId = intent.getIntExtra("schemeId", -1);
        plantId = intent.getIntExtra("plantId", -1);
        teamId = intent.getIntExtra("teamId", -1);

        initView();
    }

    private void initView(){
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.raising_scheme_title));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final CardView waterFrequency = findViewById(R.id.water_scheme);
        schemeSpinner = findViewById(R.id.scheme_spinner);
        schemeSpinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(RaisingSchemeActivity.this, dataset.get(position).toString(), Toast.LENGTH_SHORT).show();
                schemeId = schemeIdList.get(position);
                setItemTodoList(plantId, schemeId);
            }
        });

        getAllScheme(plantId, schemeId);
        /*final List<String> dataset = new LinkedList<>(Arrays.asList("方案一", "方案二", "方案三", "方案四", "方案五"));*/
        FloatingActionButton addScheme = findViewById(R.id.add_scheme);
        addScheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RaisingSchemeActivity.this, SchemeAddActivity.class);
                intent.putExtra("plantId", plantId);
                startActivityForResult(intent, ADD);
            }
        });

    }

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
            case ADD:
                if (resultCode == RESULT_OK){
                    getAllScheme(plantId, schemeId);
                }
                break;
            default:
        }
    }

    private void getAllScheme(final int plantId, final int schemeId){                                 //获取所有养护方案
        FlowerTaleApiService.getInstance().doGetSchemeByPlantId(plantId).enqueue(new Callback<BaseResponse<List<SchemeDTO>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<SchemeDTO>>> call, Response<BaseResponse<List<SchemeDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        List<SchemeDTO> schemeDTOS = response.body().getObject();
                        for (int i=0;i<schemeDTOS.size();i++){
                            if (schemeDTOS.get(i).getId() == schemeId){
                                dataset.add(0,schemeDTOS.get(i).getName());
                                schemeIdList.add(0, schemeDTOS.get(i).getId());
                            }else{
                                dataset.add(schemeDTOS.get(i).getName());
                                schemeIdList.add(schemeDTOS.get(i).getId());
                            }
                        }

                        if (dataset.size()!=0){
                            schemeSpinner.attachDataSource(dataset);
                        }

                        Button switchScheme = findViewById(R.id.switch_scheme);
                        switchScheme.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String currentScheme = dataset.get(schemeSpinner.getSelectedIndex());
                                Intent intent = new Intent();
                                intent.putExtra("currentScheme", currentScheme);
                                intent.putExtra("schemeId", schemeIdList.get(schemeSpinner.getSelectedIndex()));
                                setCurrentScheme(plantId, schemeIdList.get(schemeSpinner.getSelectedIndex()));
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });

                        setItemTodoList(plantId, schemeId);

                    } else {
                        Toast.makeText(RaisingSchemeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(RaisingSchemeActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<SchemeDTO>>> call, Throwable t) {
                Toast.makeText(RaisingSchemeActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setItemTodoList(int plantId, final int schemeId){                                  //设置方案详细事项
        FlowerTaleApiService.getInstance().doGetSchemeByPlantId(plantId).enqueue(new Callback<BaseResponse<List<SchemeDTO>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<SchemeDTO>>> call, Response<BaseResponse<List<SchemeDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        List<SchemeDTO> schemeDTOS = response.body().getObject();
                        for (int i=0;i<schemeDTOS.size();i++){
                            if (schemeDTOS.get(i).getId() == schemeId){
                                List<ItemDTO> itemDTOS = schemeDTOS.get(i).getItemDTOList();
                                for (int j=0;j<itemDTOS.size();j++){
                                    switch (itemDTOS.get(j).getType()){
                                        case 0:
                                            waterList.add(itemDTOS.get(j).getTime());
                                        case 1:
                                            fertilizeList.add(itemDTOS.get(j).getTime());
                                        case 2:
                                            pruneList.add(itemDTOS.get(j).getTime());
                                        case 3:
                                            sunshineList.add(itemDTOS.get(j).getTime());
                                    }
                                }
                            }
                        }

                        TextView waterFrequency = findViewById(R.id.water_frequency);
                        waterFrequency.setText("每日"+waterList.size()+"次");
                        final CardView waterScheme = findViewById(R.id.water_scheme);
                        waterDetail = findViewById(R.id.water_scheme_detail);
                        LinearLayoutManager waterLayoutManager = new LinearLayoutManager(RaisingSchemeActivity.this);
                        waterDetail.setLayoutManager(waterLayoutManager);
                        waterAdapter = new TimeAdapter(waterList);
                        waterDetail.setAdapter(waterAdapter);
                        ImageView waterMore = findViewById(R.id.water_more);
                        waterMore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TransitionManager.beginDelayedTransition(waterScheme);
                                if (waterDetail.getVisibility() == View.GONE){
                                    waterDetail.setVisibility(View.VISIBLE);
                                }else{
                                    waterDetail.setVisibility(View.GONE);
                                }
                            }
                        });

                        TextView fertilizeFrequency = findViewById(R.id.fertilize_frequency);
                        fertilizeFrequency.setText("每日"+fertilizeList.size()+"次");
                        final CardView fertilizeScheme = findViewById(R.id.fertilize_scheme);
                        fertilizeDetail = findViewById(R.id.fertilize_scheme_detail);
                        LinearLayoutManager fertilizeLayoutManager = new LinearLayoutManager(RaisingSchemeActivity.this);
                        fertilizeDetail.setLayoutManager(fertilizeLayoutManager);
                        fertilizeAdapter = new TimeAdapter(fertilizeList);
                        fertilizeDetail.setAdapter(fertilizeAdapter);
                        ImageView fertilizeMore = findViewById(R.id.fertilize_more);
                        fertilizeMore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TransitionManager.beginDelayedTransition(fertilizeScheme);
                                if (fertilizeDetail.getVisibility() == View.GONE){
                                    fertilizeDetail.setVisibility(View.VISIBLE);
                                }else{
                                    fertilizeDetail.setVisibility(View.GONE);
                                }
                            }
                        });

                        TextView pruneFrequency = findViewById(R.id.prune_frequency);
                        pruneFrequency.setText("每日"+pruneList.size()+"次");
                        final CardView pruneScheme = findViewById(R.id.prune_scheme);
                        pruneDetail = findViewById(R.id.prune_scheme_detail);
                        LinearLayoutManager pruneLayoutManager = new LinearLayoutManager(RaisingSchemeActivity.this);
                        pruneDetail.setLayoutManager(pruneLayoutManager);
                        pruneAdapter = new TimeAdapter(pruneList);
                        pruneDetail.setAdapter(pruneAdapter);
                        ImageView pruneMore = findViewById(R.id.prune_more);
                        pruneMore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TransitionManager.beginDelayedTransition(pruneScheme);
                                if (pruneDetail.getVisibility() == View.GONE){
                                    pruneDetail.setVisibility(View.VISIBLE);
                                }else{
                                    pruneDetail.setVisibility(View.GONE);
                                }
                            }
                        });

                        TextView sunshineFrequency = findViewById(R.id.sunshine_frequency);
                        sunshineFrequency.setText("每日"+sunshineList.size()+"次");
                        final CardView sunshineScheme = findViewById(R.id.sunshine_scheme);
                        sunshineDetail = findViewById(R.id.sunshine_scheme_detail);
                        LinearLayoutManager sunshineLayoutManager = new LinearLayoutManager(RaisingSchemeActivity.this);
                        sunshineDetail.setLayoutManager(sunshineLayoutManager);
                        sunshineAdapter = new TimeAdapter(sunshineList);
                        sunshineDetail.setAdapter(sunshineAdapter);
                        ImageView sunshineMore = findViewById(R.id.sunshine_more);
                        sunshineMore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TransitionManager.beginDelayedTransition(sunshineScheme);
                                if (sunshineDetail.getVisibility() == View.GONE){
                                    sunshineDetail.setVisibility(View.VISIBLE);
                                }else{
                                    sunshineDetail.setVisibility(View.GONE);
                                }
                            }
                        });

                    } else {
                        Toast.makeText(RaisingSchemeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(RaisingSchemeActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<SchemeDTO>>> call, Throwable t) {
                Toast.makeText(RaisingSchemeActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setCurrentScheme(int plantId, int schemeId){                                   //切换方案
        FlowerTaleApiService.getInstance().adoptScheme(plantId, schemeId).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        Toast.makeText(RaisingSchemeActivity.this, "切换成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RaisingSchemeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(RaisingSchemeActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(RaisingSchemeActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
