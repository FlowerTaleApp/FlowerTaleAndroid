package com.flowertale.flowertaleandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flowertale.flowertaleandroid.bean.PlantInfoResult;
import com.flowertale.flowertaleandroid.bean.PlantOtherInfoResult;

import java.util.List;

public class PlantInfoActivity extends AppCompatActivity {

    public static final String PLANT_INFO = "plantInfo";

    private TextView titleTextView;
    private TextView nameChsTextView;
    private TextView nameLatinTextView;
    private TextView aliasNamesTextView;
    private TextView familyTextView;
    private TextView genusTextView;
    private LinearLayout imageGroupLinearLayout;
    private TextView descriptionTextView;
    private TextView valueTextView;
    private TextView characteristicTextView;
    private TextView distributionTextView;
    private TextView floweringPeriodTextView;
    private ImageView backImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_info);

        titleTextView = findViewById(R.id.plant_info_title_text_view);
        nameChsTextView = findViewById(R.id.plant_info_name_chs_text_view);
        nameLatinTextView = findViewById(R.id.plant_info_name_latin_text_view);
        aliasNamesTextView = findViewById(R.id.plant_info_alias_names_text_view);
        familyTextView = findViewById(R.id.plant_info_family_text_view);
        genusTextView = findViewById(R.id.plant_info_genus_text_view);
        imageGroupLinearLayout = findViewById(R.id.plant_info_image_group);
        descriptionTextView = findViewById(R.id.plant_info_description_text_view);
        valueTextView = findViewById(R.id.plant_info_description_text_view);
        characteristicTextView = findViewById(R.id.plant_info_characteristic_text_view);
        distributionTextView = findViewById(R.id.plant_info_distribution_text_view);
        floweringPeriodTextView = findViewById(R.id.plant_info_flowering_period_text_view);
        backImageView = findViewById(R.id.plant_info_back_image);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        PlantInfoResult plantInfoResult = (PlantInfoResult) getIntent().getSerializableExtra(PLANT_INFO);
        titleTextView.setText(plantInfoResult.getNameStd());
        nameChsTextView.setText(plantInfoResult.getNameStd());
        nameLatinTextView.setText(plantInfoResult.getNameLt());
        aliasNamesTextView.setText(plantInfoResult.getAlias());
        familyTextView.setText(plantInfoResult.getFamilyCn());
        genusTextView.setText(plantInfoResult.getGenusCn());
        descriptionTextView.setText(plantInfoResult.getDescription());
        PlantOtherInfoResult otherInfoResult = plantInfoResult.getInfo();
        valueTextView.setText(otherInfoResult.getValue());
        characteristicTextView.setText(otherInfoResult.getCharacteristic());
        distributionTextView.setText(otherInfoResult.getArea());
        floweringPeriodTextView.setText(otherInfoResult.getSeason());

        List<String> imageList;
        if (plantInfoResult.getImages().size() > 3) {
            imageList = plantInfoResult.getImages().subList(0, 3);
        } else {
            imageList = plantInfoResult.getImages();
        }
        imageGroupLinearLayout.removeAllViews();
        for (String url : imageList) {
            ImageView imageView = new ImageView(this);
            imageView.getLayoutParams().height = 100;
            imageView.getLayoutParams().width = 100;
            imageView.setAdjustViewBounds(true);
            Glide.with(getApplicationContext()).load(url).into(imageView);
            imageGroupLinearLayout.addView(imageView);
        }
    }
}
