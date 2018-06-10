package com.flowertale.flowertaleandroid;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.List;

public class MemberInviteActivity extends AppCompatActivity {

    private MaterialSearchBar searchBar;


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
}
