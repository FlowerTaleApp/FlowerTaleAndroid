package com.flowertale.flowertaleandroid;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class SchemeAddActivity extends AppCompatActivity {

    private List<String> frequencySet = new LinkedList<>(Arrays.asList("日", "月"));
    private List<Integer> rateSet = new LinkedList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_add);

        initView();
    }

    private void initView(){
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.scheme_add));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final NiceSpinner waterFrequency = findViewById(R.id.water_frequency);                      //浇水
        NiceSpinner waterRate = findViewById(R.id.water_rate);
        waterFrequency.attachDataSource(frequencySet);
        waterRate.attachDataSource(rateSet);

        Button addWaterTime = findViewById(R.id.add_water_time);
        addWaterTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (waterFrequency.getSelectedIndex()==1){
                    Calendar now = Calendar.getInstance();
                    new DatePickerDialog(
                            SchemeAddActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    Toast.makeText(SchemeAddActivity.this, year+"-"+month+"-"+dayOfMonth, Toast.LENGTH_SHORT).show();
                                }
                            },
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    ).show();
                }else{
                    Calendar now = Calendar.getInstance();
                    new TimePickerDialog(
                            SchemeAddActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    Toast.makeText(SchemeAddActivity.this, hourOfDay+"-"+minute, Toast.LENGTH_SHORT).show();
                                }
                            },
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            true
                    ).show();
                }
            }
        });

        final NiceSpinner fertilizeFrequency = findViewById(R.id.fertilize_frequency);                      //施肥
        NiceSpinner fertilizeRate = findViewById(R.id.fertilize_rate);
        fertilizeFrequency.attachDataSource(frequencySet);
        fertilizeRate.attachDataSource(rateSet);

        Button addFertilizeTime = findViewById(R.id.add_fertilize_time);
        addFertilizeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fertilizeFrequency.getSelectedIndex()==1){
                    Calendar now = Calendar.getInstance();
                    new DatePickerDialog(
                            SchemeAddActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    Toast.makeText(SchemeAddActivity.this, year+"-"+month+"-"+dayOfMonth, Toast.LENGTH_SHORT).show();
                                }
                            },
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    ).show();
                }else{
                    Calendar now = Calendar.getInstance();
                    new TimePickerDialog(
                            SchemeAddActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    Toast.makeText(SchemeAddActivity.this, hourOfDay+"-"+minute, Toast.LENGTH_SHORT).show();
                                }
                            },
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            true
                    ).show();
                }
            }
        });

        final NiceSpinner pruneFrequency = findViewById(R.id.prune_frequency);                      //剪枝
        NiceSpinner pruneRate = findViewById(R.id.prune_rate);
        pruneFrequency.attachDataSource(frequencySet);
        pruneRate.attachDataSource(rateSet);

        Button addPruneTime = findViewById(R.id.add_prune_time);
        addPruneTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pruneFrequency.getSelectedIndex()==1){
                    Calendar now = Calendar.getInstance();
                    new DatePickerDialog(
                            SchemeAddActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    Toast.makeText(SchemeAddActivity.this, year+"-"+month+"-"+dayOfMonth, Toast.LENGTH_SHORT).show();
                                }
                            },
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    ).show();
                }else{
                    Calendar now = Calendar.getInstance();
                    new TimePickerDialog(
                            SchemeAddActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    Toast.makeText(SchemeAddActivity.this, hourOfDay+"-"+minute, Toast.LENGTH_SHORT).show();
                                }
                            },
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            true
                    ).show();
                }
            }
        });

        final NiceSpinner sunshineFrequency = findViewById(R.id.sunshine_frequency);                      //光照
        NiceSpinner sunshineRate = findViewById(R.id.sunshine_rate);
        sunshineFrequency.attachDataSource(frequencySet);
        sunshineRate.attachDataSource(rateSet);

        Button addSunshineTime = findViewById(R.id.add_sunshine_time);
        addPruneTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sunshineFrequency.getSelectedIndex()==1){
                    Calendar now = Calendar.getInstance();
                    new DatePickerDialog(
                            SchemeAddActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    Toast.makeText(SchemeAddActivity.this, year+"-"+month+"-"+dayOfMonth, Toast.LENGTH_SHORT).show();
                                }
                            },
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    ).show();
                }else{
                    Calendar now = Calendar.getInstance();
                    new TimePickerDialog(
                            SchemeAddActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    Toast.makeText(SchemeAddActivity.this, hourOfDay+"-"+minute, Toast.LENGTH_SHORT).show();
                                }
                            },
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            true
                    ).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scheme_create_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.create_finish:
                //新建完成
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

}
