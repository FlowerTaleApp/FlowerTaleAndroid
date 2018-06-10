package com.flowertale.flowertaleandroid;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GroupCreateActivity extends AppCompatActivity {

    public static final int INVITE = 1;
    private MemberItem[] memberItems = {new MemberItem(R.drawable.flower, "FlowerTale1"), new MemberItem(R.drawable.flower2, "FlowerTale2"),
                                        new MemberItem(R.drawable.sunflower, "FlowerTale3")};
    private List<MemberItem> memberItemList = new ArrayList<>();
    private MemberAdapter memberAdapter;
    private int mem_num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);

        initView();
    }

    private void initView(){
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.group_create_title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initMem();
        mem_num = memberItemList.size()-1;
        TextView currentMemNum = (TextView)findViewById(R.id.current_mem_num);
        currentMemNum.setText(mem_num+"/10");
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.group_create_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        memberAdapter = new MemberAdapter(memberItemList);
        recyclerView.setAdapter(memberAdapter);
    }

    private void initMem(){
        memberItemList.clear();
        MemberItem memberAdd = new MemberItem(R.drawable.member_add, getString(R.string.member_add));
        memberItemList.add(0,memberAdd);
        for (int i=0;i<5;i++){
            Random random = new Random();
            int index = random.nextInt(memberItems.length);
            memberItemList.add(memberItems[index]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_create_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.create_finish:
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
                    //增加群成员
                }
                break;
            default:
        }
    }
}
