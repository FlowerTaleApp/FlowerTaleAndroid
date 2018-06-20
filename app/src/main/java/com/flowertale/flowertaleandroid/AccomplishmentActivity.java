package com.flowertale.flowertaleandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.flowertale.flowertaleandroid.DTO.CompletionItemDTO;
import com.flowertale.flowertaleandroid.DTO.DiaryDTO;
import com.flowertale.flowertaleandroid.DTO.NurtureCompletionDTO;
import com.flowertale.flowertaleandroid.DTO.response.BaseResponse;
import com.flowertale.flowertaleandroid.entity.FlowerRecord;
import com.flowertale.flowertaleandroid.service.FlowerTaleApiService;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import info.abdolahi.CircularMusicProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccomplishmentActivity extends AppCompatActivity {

    private List<Integer> percentByType = new ArrayList<>();
    private int plantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accomplishment);

        initView();
    }

    private void initView(){

        plantId = getIntent().getIntExtra("plantId", -1);
        setNurtureCompletion();
    }

    private void setNurtureCompletion(){                                        //完成情况
        FlowerTaleApiService.getInstance().doGetCurrentDayNurtureCompletion().enqueue(new Callback<BaseResponse<List<NurtureCompletionDTO>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<NurtureCompletionDTO>>> call, Response<BaseResponse<List<NurtureCompletionDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        List<NurtureCompletionDTO> nurtureCompletionDTOS = response.body().getObject();
                        for (int i=0; i<nurtureCompletionDTOS.size();i++){
                            if (nurtureCompletionDTOS.get(i).getPlantId()==plantId){
                                List<CompletionItemDTO> completionItemDTOS = nurtureCompletionDTOS.get(i).getCompletionItemDTOList();
                                for (int j=0;j<4;j++){
                                    int numFinished = completionItemDTOS.get(j).getNumFinished();
                                    int numTotal = completionItemDTOS.get(j).getNumTotal();
                                    int percent =0 ;
                                    if (numFinished < numTotal){
                                        percent = (int)(((float)numFinished/(float)numTotal)*100);
                                    }else{
                                        percent = 100;
                                    }
                                    percentByType.add(percent);
                                }
                            }
                        }

                        TextView waterPercent = findViewById(R.id.water_percent);
                        waterPercent.setText(percentByType.get(0)+"%");
                        CircularMusicProgressBar waterProgress = findViewById(R.id.water_progress);
                        waterProgress.setValue(percentByType.get(0));
                        TextView fertilizePercent = findViewById(R.id.fertilize_percent);
                        fertilizePercent.setText(percentByType.get(1)+"%");
                        CircularMusicProgressBar fertilizeProgress = findViewById(R.id.fertilize_progress);
                        fertilizeProgress.setValue(percentByType.get(1));
                        TextView prunePercent = findViewById(R.id.prune_percent);
                        prunePercent.setText(percentByType.get(2)+"%");
                        CircularMusicProgressBar pruneProgress = findViewById(R.id.prune_progress);
                        pruneProgress.setValue(percentByType.get(2));
                        TextView sunshinePercent = findViewById(R.id.sunshine_percent);
                        sunshinePercent.setText(percentByType.get(3)+"%");
                        CircularMusicProgressBar sunshineProgress = findViewById(R.id.sunshine_progress);
                        sunshineProgress.setValue(percentByType.get(3));
                    } else {
                        Toast.makeText(AccomplishmentActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(AccomplishmentActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<NurtureCompletionDTO>>> call, Throwable t) {
                Toast.makeText(AccomplishmentActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
