package com.flowertale.flowertaleandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flowertale.flowertaleandroid.DTO.DetailedFlowerDTO;
import com.flowertale.flowertaleandroid.DTO.response.BaseResponse;
import com.flowertale.flowertaleandroid.bean.PlantInfoResult;
import com.flowertale.flowertaleandroid.recognise.OnRecogniseListener;
import com.flowertale.flowertaleandroid.recognise.PlantInfoAsynTask;
import com.flowertale.flowertaleandroid.service.FlowerTaleApiService;
import com.flowertale.flowertaleandroid.util.ContextUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlantInfoActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String PLANT_CODE = "plant_code";

    public static final Integer FROM_RECOGNIZE = 0;

    public static final Integer FROM_DISCOVER = 1;

    public static final String SOURCE = "source";

    private ImageView mPlantInfoBackImage;
    private TextView mPlantInfoTitleTextView;
    /**
     * 花的中文名
     */
    private TextView mPlantInfoNameChsTextView;
    /**
     * 别名
     */
    private TextView mPlantInfoAliasNamesTextView;
    /**
     * 花的拉丁名
     */
    private TextView mPlantInfoNameLatinTextView;
    /**
     * 花属
     */
    private TextView mPlantInfoFamilyTextView;
    /**
     * 花科
     */
    private TextView mPlantInfoGenusTextView;
    /**
     * 价值
     */
    private TextView mPlantInfoValueTextView;
    /**
     * 特征
     */
    private TextView mPlantInfoCharTextView;
    /**
     * 分布
     */
    private TextView mPlantInfoDistribuionTextView;
    /**
     * 花期
     */
    private TextView mPlantInfoSeasonTextView;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_info);
        initView();
        String code = getIntent().getStringExtra(PLANT_CODE);
        if (getIntent().getIntExtra(SOURCE, -1) == FROM_RECOGNIZE) {
            PlantInfoAsynTask plantInfoAsynTask = new PlantInfoAsynTask();
            plantInfoAsynTask.setOnRecogniseListener(new OnRecogniseListener<PlantInfoResult>() {
                @Override
                public void onSuccess(PlantInfoResult result) {
                    Glide.with(getApplicationContext()).load(result.getImages().get(0)).into(imageView);
                    mPlantInfoTitleTextView.setText(result.getNameStd());
                    mPlantInfoNameLatinTextView.setText(result.getNameLt());
                    mPlantInfoNameChsTextView.setText(result.getNameStd());
                    mPlantInfoAliasNamesTextView.setText(result.getAlias());
                    mPlantInfoDistribuionTextView.setText(result.getDescription());
                    mPlantInfoGenusTextView.setText(result.getGenusCn());
                    mPlantInfoFamilyTextView.setText(result.getFamilyCn());
                    if (result.getInfo() != null) {
                        mPlantInfoValueTextView.setText(result.getInfo().getValue());
                        mPlantInfoSeasonTextView.setText(result.getInfo().getSeason());
                        mPlantInfoDistribuionTextView.setText(result.getInfo().getArea());
                        mPlantInfoCharTextView.setText(result.getInfo().getCharacteristic());
                    }
                }

                @Override
                public void onFailure() {
                    Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
                }
            });
            plantInfoAsynTask.execute(code);
        } else if (getIntent().getIntExtra(SOURCE, -1) == FROM_DISCOVER) {
            FlowerTaleApiService.getInstance().doGetDetailedFlower(code).enqueue(new Callback<BaseResponse<DetailedFlowerDTO>>() {
                @Override
                public void onResponse(Call<BaseResponse<DetailedFlowerDTO>> call, Response<BaseResponse<DetailedFlowerDTO>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getStatus() == 0) {
                            DetailedFlowerDTO result = response.body().getObject();
                            Glide.with(ContextUtil.getContext()).load(result.getImageUrlList().get(0)).into(imageView);
                            mPlantInfoTitleTextView.setText(result.getNameCn());
                            mPlantInfoNameLatinTextView.setText(result.getNameLt());
                            mPlantInfoNameChsTextView.setText(result.getNameCn());
                            mPlantInfoAliasNamesTextView.setText(result.getAlias());
                            mPlantInfoDistribuionTextView.setText(result.getDescription());
                            mPlantInfoGenusTextView.setText(result.getGenus());
                            mPlantInfoFamilyTextView.setText(result.getFamily());
                            mPlantInfoValueTextView.setText(result.getValue());
                            mPlantInfoSeasonTextView.setText(result.getFloweringPeriod());
                            mPlantInfoDistribuionTextView.setText(result.getDistribution());
                            mPlantInfoCharTextView.setText(result.getCharacteristic());
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<DetailedFlowerDTO>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getPlaintInfo(){
        //TODO
    }

    private void initView() {
        mPlantInfoBackImage = (ImageView) findViewById(R.id.plant_info_back_image);
        mPlantInfoBackImage.setOnClickListener(this);
        mPlantInfoTitleTextView = (TextView) findViewById(R.id.plant_info_title_text_view);
        mPlantInfoNameChsTextView = (TextView) findViewById(R.id.plant_info_name_chs_text_view);
        mPlantInfoAliasNamesTextView = (TextView) findViewById(R.id.plant_info_alias_names_text_view);
        mPlantInfoNameLatinTextView = (TextView) findViewById(R.id.plant_info_name_latin_text_view);
        mPlantInfoFamilyTextView = (TextView) findViewById(R.id.plant_info_family_text_view);
        mPlantInfoGenusTextView = (TextView) findViewById(R.id.plant_info_genus_text_view);
        mPlantInfoValueTextView = (TextView) findViewById(R.id.plant_info_value_text_view);
        mPlantInfoCharTextView = (TextView) findViewById(R.id.plant_info_char_text_view);
        mPlantInfoDistribuionTextView = (TextView) findViewById(R.id.plant_info_distribuion_text_view);
        mPlantInfoSeasonTextView = (TextView) findViewById(R.id.plant_info_season_text_view);
        imageView = findViewById(R.id.plant_info_image);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.plant_info_back_image:
                finish();
                break;
        }
    }


}
