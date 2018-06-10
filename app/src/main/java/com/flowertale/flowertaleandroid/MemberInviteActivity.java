package com.flowertale.flowertaleandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.flowertale.flowertaleandroid.GroupCreateActivity.INVITE;

public class MemberInviteActivity extends AppCompatActivity {

    private MaterialSearchBar searchBar;
    private MemberItem[] memberItems = {new MemberItem(R.drawable.flower, "FlowerTale1"), new MemberItem(R.drawable.flower2, "FlowerTale2"),
            new MemberItem(R.drawable.sunflower, "FlowerTale3")};
    private List<MemberItem> memberItemList = new ArrayList<>();
    private MemberSearchAdapter memberAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_invite);

        initView();
    }

    private void initView(){

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.member_invite_title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        searchBar = findViewById(R.id.searchBar);
        searchBar.setHint(getString(R.string.member_search_hint));
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //Toast.makeText(MemberInviteActivity.this, "Search State Changed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                Toast.makeText(MemberInviteActivity.this, searchBar.getText().toString(), Toast.LENGTH_SHORT).show();
                //显示搜索结果
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                //Toast.makeText(MemberInviteActivity.this, "Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        initMem();
        RecyclerView recyclerView = findViewById(R.id.member_search_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        memberAdapter = new MemberSearchAdapter(memberItemList);
        recyclerView.setAdapter(memberAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void initMem(){
        memberItemList.clear();
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

    private class MemberSearchAdapter extends RecyclerView.Adapter<MemberSearchAdapter.ViewHolder>{

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

        public MemberSearchAdapter(List<MemberItem> memberItemList){
            mMemberList = memberItemList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (mContext == null){
                mContext = parent.getContext();
            }

            View view = LayoutInflater.from(mContext).inflate(R.layout.member_search_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            MemberItem memberItem = mMemberList.get(position);
            holder.memberName.setText(memberItem.getName());
            Glide.with(mContext).load(memberItem.getSelfImage()).into(holder.selfImage);
        }

        @Override
        public int getItemCount() {
            return mMemberList.size();
        }

    }
}
