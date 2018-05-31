package com.flowertale.flowertaleandroid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flowertale.flowertaleandroid.bean.RecognitionResult;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class RecognitionResultActivity extends AppCompatActivity {
    private static final String TAG = "RecognitionResultActivity";

    private RecyclerView mRecyclerView;
    private List<RecognitionResult> mItemList = new ArrayList<>();
    private ImageView backImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition_result);
        fetchResults();
        initView();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new RecognitionResultAdapter(mItemList));
        backImage = findViewById(R.id.back_image);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fetchResults() {
        mItemList = (List<RecognitionResult>) getIntent()
                .getSerializableExtra(RecogniseFragment.API_RECOGNITION_RESULT);
        //Log.i(TAG, mItemList.toString());
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView plantImage;
        private TextView familyText;
        private TextView genusText;
        private TextView chsNameText;
        private TextView latinNameText;
        private TextView credibilityText;
        private TextView aliasNameText;

        public ViewHolder(View itemView) {
            super(itemView);
            plantImage = itemView.findViewById(R.id.plant_image);
            familyText = itemView.findViewById(R.id.family_text);
            genusText = itemView.findViewById(R.id.genus_text);
            chsNameText = itemView.findViewById(R.id.plant_name_text);
            latinNameText = itemView.findViewById(R.id.plant_name_latin_text);
            credibilityText = itemView.findViewById(R.id.credibility_text);
            aliasNameText = itemView.findViewById(R.id.plant_name_alias_text);
        }
    }

    private class RecognitionResultAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<RecognitionResult> itemList;

        public RecognitionResultAdapter(List<RecognitionResult> itemList) {
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recognition_result_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            RecognitionResult item = itemList.get(position);
            holder.familyText.setText(item.getFamily());
            holder.genusText.setText(item.getGenus());
            holder.chsNameText.setText(item.getName());
            holder.aliasNameText.setText(item.getAliasName());
            holder.latinNameText.setText(item.getLatinName());
            Glide.with(getApplicationContext()).load(item.getImageUrl()).into(holder.plantImage);
            String score = (int) item.getScore() + "%";
            holder.credibilityText.setText(score);
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }
}
