package com.flowertale.flowertaleandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class FlowerDetailsActivity extends AppCompatActivity {

    private static final int ADD = 1;
    public static final String INFO_TITLE = "info_title";
    public static final String INFO_IMAGE_ID = "info_image_id";

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    //测试数据
    private FlowerRecord[] flowerRecords = {new FlowerRecord(R.drawable.flower, "FlowerTale", "浇水：给花儿浇了些水，显得更精神了", sdf.format(new Date())),
                                            new FlowerRecord(R.drawable.flower2,"FlowerTale1", "施肥：给花儿施了点肥，希望它能快些长大", sdf.format(new Date()))};
    private List<FlowerRecord> recordList = new ArrayList<>();
    private RecordAdapter recordAdapter;

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
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        ImageView flowerImageView = (ImageView)findViewById(R.id.flower_image_view);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(infoTitle);
        Glide.with(this).load(infoImageId).into(flowerImageView);

        initRecords();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.raising_details_view);              //养护记录展示
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recordAdapter = new RecordAdapter(recordList);
        recyclerView.setAdapter(recordAdapter);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.record_add_fab);             //添加养护记录按钮
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(FlowerDetailsActivity.this, RecordAddActivity.class);
                startActivityForResult(intent1, ADD);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ADD:
                if (resultCode == RESULT_OK){
                    Boolean water = data.getBooleanExtra("water", false);
                    Boolean fertilize = data.getBooleanExtra("fertilize",false);
                    Boolean prune = data.getBooleanExtra("prune", false);
                    Boolean sunshine = data.getBooleanExtra("sunshine", false);
                    String description = data.getStringExtra("description");
                    Log.d("FlowerDetailActivity", water+" "+fertilize+" "+prune+" "+sunshine+" "+description);
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
                startActivity(intent);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecords(){
        recordList.clear();
        for (int i = 0; i<15;i++){
            Random random = new Random();
            int index = random.nextInt(flowerRecords.length);
            recordList.add(flowerRecords[index]);
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView recordImage;
        TextView recordDesc;
        TextView recordName;
        TextView recordTime;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            recordImage = (ImageView)view.findViewById(R.id.flower_record_image);
            recordDesc = (TextView)view.findViewById(R.id.flower_record_desc);
            recordName = (TextView)view.findViewById(R.id.flower_record_name);
            recordTime = (TextView)view.findViewById(R.id.flower_record_time);
        }
    }

    private class RecordAdapter extends RecyclerView.Adapter<ViewHolder>{
        private Context mContext;
        private List<FlowerRecord> mRecordList;

        public RecordAdapter(List<FlowerRecord> recordList){
            mRecordList = recordList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (mContext == null){
                mContext = parent.getContext();
            }

            View view = LayoutInflater.from(mContext).inflate(R.layout.flower_record,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            FlowerRecord flowerRecord = mRecordList.get(position);
            holder.recordDesc.setText(flowerRecord.getDesc());
            holder.recordName.setText(flowerRecord.getName());
            holder.recordTime.setText(flowerRecord.getDate());
            Glide.with(mContext).load(flowerRecord.getRecordImage()).into(holder.recordImage);
        }

        @Override
        public int getItemCount() {
            return mRecordList.size();
        }
    }
}
