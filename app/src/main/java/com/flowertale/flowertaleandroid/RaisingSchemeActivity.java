package com.flowertale.flowertaleandroid;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RaisingSchemeActivity extends AppCompatActivity {

    private static final int ADD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raising_scheme);

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
        final NiceSpinner schemeSpinner = findViewById(R.id.scheme_spinner);
        final List<String> dataset = new LinkedList<>(Arrays.asList("方案一", "方案二", "方案三", "方案四", "方案五"));
        schemeSpinner.attachDataSource(dataset);
        schemeSpinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(RaisingSchemeActivity.this, dataset.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Button switchScheme = findViewById(R.id.switch_scheme);
        switchScheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentScheme = dataset.get(schemeSpinner.getSelectedIndex());
                Intent intent = new Intent();
                intent.putExtra("currentScheme", currentScheme);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        final CardView waterScheme = findViewById(R.id.water_scheme);
        final RecyclerView waterDetail = findViewById(R.id.water_scheme_detail);
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
        final CardView fertilizeScheme = findViewById(R.id.fertilize_scheme);
        final RecyclerView fertilizeDetail = findViewById(R.id.fertilize_scheme_detail);
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
        final CardView pruneScheme = findViewById(R.id.prune_scheme);
        final RecyclerView pruneDetail = findViewById(R.id.prune_scheme_detail);
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
        final CardView sunshineScheme = findViewById(R.id.sunshine_scheme);
        final RecyclerView sunshineDetail = findViewById(R.id.sunshine_scheme_detail);
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

        FloatingActionButton addScheme = findViewById(R.id.add_scheme);
        addScheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RaisingSchemeActivity.this, SchemeAddActivity.class);
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
                    //新增方案刷新
                }
                break;
            default:
        }
    }
}
