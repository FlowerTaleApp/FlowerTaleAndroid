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
import android.widget.TextView;

import com.flowertale.flowertaleandroid.adapter.MemberAdapter;
import com.flowertale.flowertaleandroid.entity.MemberItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.flowertale.flowertaleandroid.GroupCreateActivity.INVITE;

public class FlowerMembersActivity extends AppCompatActivity {

    private static final int SCHEME = 2;
    private MemberItem[] memberItems = {new MemberItem(R.drawable.flower, "FlowerTale1"), new MemberItem(R.drawable.flower2, "FlowerTale2"),
            new MemberItem(R.drawable.sunflower, "FlowerTale3")};
    private List<MemberItem> memberItemList = new ArrayList<>();
    private MemberAdapter memberAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_members);
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

        CardView raisingScheme = findViewById(R.id.raising_scheme);
        raisingScheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlowerMembersActivity.this, RaisingSchemeActivity.class);
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

        initMem();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.members_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        memberAdapter = new MemberAdapter(memberItemList);
        recyclerView.setAdapter(memberAdapter);
    }

    private void initMem(){
        memberItemList.clear();
        //MemberItem memberAdd = new MemberItem(R.drawable.member_add, getString(R.string.member_add));
        //memberItemList.add(0,memberAdd);
        for (int i=0;i<5;i++){
            Random random = new Random();
            int index = random.nextInt(memberItems.length);
            memberItemList.add(memberItems[index]);
        }
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
            case INVITE:
                if (resultCode == RESULT_OK){
                    //新增成员
                }
                break;
            case SCHEME:
                if (resultCode == RESULT_OK){
                    String currentScheme = data.getStringExtra("currentScheme");
                    TextView raisingSchemeTitle = findViewById(R.id.raising_scheme_title);
                    raisingSchemeTitle.setText(currentScheme);
                }
                break;
            default:
        }
    }
}
