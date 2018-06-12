package com.flowertale.flowertaleandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.flowertale.flowertaleandroid.DTO.SimpleFlowerDTO;
import com.flowertale.flowertaleandroid.DTO.response.BaseResponse;
import com.flowertale.flowertaleandroid.adapter.FlowerListAdapter;
import com.flowertale.flowertaleandroid.entity.FlowerListData;
import com.flowertale.flowertaleandroid.service.FlowerTaleApiService;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlowerListActivity extends AppCompatActivity implements View.OnClickListener {

    private List<FlowerListData> flowerList;
    private ImageView mPlantSearchBackImage;
    /**
     * 搜索结果
     */
    private TextView mPlantSearchTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_list);
        initView();
        initFlower();
    }

    private void initFlower() {
        final String name = getIntent().getStringExtra("searchKey");
        FlowerTaleApiService.getInstance().doSearchFlowersByName(name).enqueue(new Callback<BaseResponse<List<SimpleFlowerDTO>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<SimpleFlowerDTO>>> call, Response<BaseResponse<List<SimpleFlowerDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        List<SimpleFlowerDTO> simpleFlowerDTOList = response.body().getObject();
                        flowerList = simpleFlowerDTOList.stream().map(e -> new FlowerListData(e.getImageUrl(),
                                e.getNameCn(), e.getFamily() + " " + e.getGenus(), e.getCode())).collect(Collectors.toList());
                        FlowerListAdapter adapter = new FlowerListAdapter(FlowerListActivity.this, R.layout.activity_flower_list_item, flowerList);
                        ListView listView = findViewById(R.id.flower_listView);
                        listView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<SimpleFlowerDTO>>> call, Throwable t) {

            }
        });
    }

    private void initView() {
        mPlantSearchBackImage = (ImageView) findViewById(R.id.plant_search_back_image);
        mPlantSearchBackImage.setOnClickListener(this);
        mPlantSearchTitleTextView = (TextView) findViewById(R.id.plant_search_title_text_view);
        Intent intent = getIntent();
        String searchKey = intent.getStringExtra("searchKey");
        mPlantSearchTitleTextView.setText(searchKey+"搜索结果");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.plant_search_back_image:
                finish();
                break;
        }
    }
}
