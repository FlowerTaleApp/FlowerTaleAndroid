package com.flowertale.flowertaleandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.flowertale.flowertaleandroid.adapter.FlowerListAdapter;
import com.flowertale.flowertaleandroid.entity.FlowerListData;

import java.util.ArrayList;
import java.util.List;

public class FlowerListActivity extends AppCompatActivity implements View.OnClickListener {

    private List<FlowerListData> flowerList = new ArrayList<>();
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
        FlowerListAdapter adapter = new FlowerListAdapter(FlowerListActivity.this, R.layout.activity_flower_list_item, flowerList);
        ListView listView = findViewById(R.id.flower_listView);
        listView.setAdapter(adapter);
    }

    private void initFlower() {
        for (int i = 0; i < 2; i++) {
            FlowerListData flower = new FlowerListData("", "紫罗兰", "这个是描述");
            flowerList.add(flower);
        }
        FlowerListData flower2 = new FlowerListData("", "紫罗兰2", "这个是描述2");
        flowerList.add(flower2);
        FlowerListData flower3 = new FlowerListData("", "紫罗兰3", "这个是描述3");
        flowerList.add(flower3);
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
