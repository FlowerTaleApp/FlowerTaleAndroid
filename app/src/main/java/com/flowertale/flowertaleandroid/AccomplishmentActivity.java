package com.flowertale.flowertaleandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import info.abdolahi.CircularMusicProgressBar;

public class AccomplishmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accomplishment);

        initView();
    }

    private void initView(){

        CircularMusicProgressBar waterProgress = findViewById(R.id.water_progress);
        waterProgress.setValue(40);
        CircularMusicProgressBar fertilizeProgress = findViewById(R.id.fertilize_progress);
        fertilizeProgress.setValue(40);
        CircularMusicProgressBar pruneProgress = findViewById(R.id.prune_progress);
        pruneProgress.setValue(40);
        CircularMusicProgressBar sunshineProgress = findViewById(R.id.sunshine_progress);
        sunshineProgress.setValue(40);
    }
}
