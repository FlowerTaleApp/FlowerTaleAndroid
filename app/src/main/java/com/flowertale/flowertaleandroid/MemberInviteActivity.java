package com.flowertale.flowertaleandroid;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flowertale.flowertaleandroid.DTO.SimpleUserDTO;
import com.flowertale.flowertaleandroid.DTO.response.BaseResponse;
import com.flowertale.flowertaleandroid.constant.TokenConstant;
import com.flowertale.flowertaleandroid.entity.MemberItem;
import com.flowertale.flowertaleandroid.service.FlowerTaleApiService;
import com.flowertale.flowertaleandroid.util.ContextUtil;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberInviteActivity extends AppCompatActivity {

    private MaterialSearchBar searchBar;
    private List<MemberItem> memberItemList = new ArrayList<>();
    private int[] selfImages = {R.drawable.sunflower};
    private MemberSearchAdapter memberAdapter;
    private ProgressBar mProgressBar;

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

        createProgressBar();

        searchBar = findViewById(R.id.searchBar);
        searchBar.setHint(getString(R.string.member_search_hint));
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //Toast.makeText(MemberInviteActivity.this, "Search State Changed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //Toast.makeText(MemberInviteActivity.this, searchBar.getText().toString(), Toast.LENGTH_SHORT).show();
                doSearch();
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                //Toast.makeText(MemberInviteActivity.this, "Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        /*initMem();*/
        RecyclerView recyclerView = findViewById(R.id.member_search_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        memberAdapter = new MemberSearchAdapter(memberItemList);
        recyclerView.setAdapter(memberAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    /*private void initMem(){
        memberItemList.clear();
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

    private class MemberSearchAdapter extends RecyclerView.Adapter<MemberSearchAdapter.ViewHolder>{

        private Context mContext;
        private List<MemberItem> mMemberList;

        class ViewHolder extends RecyclerView.ViewHolder{

            private ImageView selfImage;
            private TextView memberName;
            private ImageView sendInvitation;

            public ViewHolder(View view){
                super(view);

                selfImage = view.findViewById(R.id.self_image);
                memberName = view.findViewById(R.id.member_name);
                sendInvitation = view.findViewById(R.id.send_invitation);
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

            final MemberItem memberItem = mMemberList.get(position);
            holder.memberName.setText(memberItem.getName());
            Glide.with(mContext).load(memberItem.getSelfImage()).into(holder.selfImage);

            holder.sendInvitation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("memInvited", memberItem);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mMemberList.size();
        }

    }

    private void doSearch(){
        showProgressBar();
        String searchName = searchBar.getText().toString();
        FlowerTaleApiService.getInstance().doSearchUsername(searchName).enqueue(new Callback<BaseResponse<List<SimpleUserDTO>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<SimpleUserDTO>>> call, Response<BaseResponse<List<SimpleUserDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        List<SimpleUserDTO> simpleUserDTOList=response.body().getObject();
                        memberItemList.clear();
                        for (int i=0; i<simpleUserDTOList.size(); i++){
                            Random random = new Random();
                            int index = random.nextInt(selfImages.length);
                            memberItemList.add(new MemberItem(selfImages[index], simpleUserDTOList.get(i).getUsername()));
                        }
                        memberAdapter.notifyDataSetChanged();
                        hideProgressBar();
                    } else {
                        hideProgressBar();
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    hideProgressBar();
                    Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<SimpleUserDTO>>> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(MemberInviteActivity.this, "无法搜索", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void createProgressBar(){
        CoordinatorLayout rootLayout= findViewById(R.id.root_layout);
        CoordinatorLayout.LayoutParams layoutParams=
                new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity= Gravity.CENTER;
        mProgressBar=new ProgressBar(this);
        mProgressBar.setLayoutParams(layoutParams);
        mProgressBar.setVisibility(View.INVISIBLE);
        rootLayout.addView(mProgressBar);
    }

}
