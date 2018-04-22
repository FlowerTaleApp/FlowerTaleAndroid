package com.flowertale.flowertaleandroid;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class RecognitionResultActivity extends AppCompatActivity {
    private static final String TAG = "RecognitionResultActivity";

    private RecyclerView mRecyclerView;
    private List<RecognitionResultItem> mItemList=new ArrayList<>();
    private ImageView backImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition_result);
        initView();
        fetchResults();
    }

    private void initView() {
        mRecyclerView=findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new RecognitionResultAdapter(mItemList));
        backImage=findViewById(R.id.back_image);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView plantImage;
        private TextView classificationText;
        private TextView nameText;
        private TextView nameEngText;
        private TextView credibilityText;

        public ViewHolder(View itemView) {
            super(itemView);
            plantImage=itemView.findViewById(R.id.plant_image);
            classificationText =itemView.findViewById(R.id.classification_text);
            nameText =itemView.findViewById(R.id.plant_name__text);
            nameEngText =itemView.findViewById(R.id.plant_name_eng_text);
            credibilityText=itemView.findViewById(R.id.credibility_text);
        }
    }

    private class RecognitionResultAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<RecognitionResultItem> itemList;

        public RecognitionResultAdapter(List<RecognitionResultItem> itemList) {
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recognition_result_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            RecognitionResultItem item= itemList.get(position);
            holder.classificationText.setText(item.getClassificationInfo());
            holder.nameText.setText(item.getName());
            holder.nameEngText.setText(item.getNameEng());
            NumberFormat format=NumberFormat.getPercentInstance();
            format.setMinimumFractionDigits(2);
            holder.credibilityText.setText(format.format(item.getCred()));
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }

    private void fetchResults() {
        List<ApiRecResultItem> apiRecResultItems=(List<ApiRecResultItem>) getIntent()
                .getSerializableExtra(RecogniseFragment.API_RECOGNITION_RESULT);
        Log.i(TAG,apiRecResultItems.toString());
        for (ApiRecResultItem apiRecResultItem : apiRecResultItems) {
            RecognitionResultItem item = new RecognitionResultItem();
            item.setCred(apiRecResultItem.getCred());
            item.setName(apiRecResultItem.getName());
            mItemList.add(item);
        }
    }
}
