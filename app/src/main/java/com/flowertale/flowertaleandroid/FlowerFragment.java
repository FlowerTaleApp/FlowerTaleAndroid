package com.flowertale.flowertaleandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlowerFragment extends Fragment {

    private FlowerInfoItem[] infoItems = { new FlowerInfoItem("桃花养成计划", R.drawable.flower, "FlowerTale"),  //测试数据
                                      new FlowerInfoItem("月季生长日记", R.drawable.flower2,"FlowerTale")};
    private List<FlowerInfoItem> infoItemList = new ArrayList<>();
    private InfoAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_flower,container,false);

        FloatingActionButton addFab = (FloatingActionButton)view.findViewById(R.id.add_fab);        //添加养护信息按钮
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FlowerAddActivity.class);
                startActivity(intent);
            }
        });

        initInfoItems();
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.raising_info_view);        //各养护信息展示
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new InfoAdapter(infoItemList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void initInfoItems(){
        infoItemList.clear();
        for (int i = 0; i<15;i++){
            Random random = new Random();
            int index = random.nextInt(infoItems.length);
            infoItemList.add(infoItems[index]);
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView infoImage;
        TextView infoTitle;
        TextView infoName;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            infoImage = (ImageView)view.findViewById(R.id.info_item_image);
            infoTitle = (TextView)view.findViewById(R.id.info_item_title);
            infoName = (TextView)view.findViewById(R.id.info_item_name);
        }
    }

    private class InfoAdapter extends RecyclerView.Adapter<ViewHolder>{
        private Context mContext;
        private List<FlowerInfoItem> mInfoList;

        public InfoAdapter(List<FlowerInfoItem> infoList){
            mInfoList = infoList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (mContext == null){
                mContext = parent.getContext();
            }
            View view = LayoutInflater.from(mContext).inflate(R.layout.flower_info_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            FlowerInfoItem infoItem = mInfoList.get(position);
            holder.infoName.setText(infoItem.getName());
            holder.infoTitle.setText(infoItem.getTitle());
            Glide.with(mContext).load(infoItem.getImageId()).into(holder.infoImage);
            final int index = position;
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FlowerInfoItem flowerInfoItem = mInfoList.get(index);
                    Intent intent = new Intent(mContext,FlowerDetailsActivity.class);
                    intent.putExtra(FlowerDetailsActivity.INFO_TITLE, flowerInfoItem.getTitle());
                    intent.putExtra(FlowerDetailsActivity.INFO_IMAGE_ID,flowerInfoItem.getImageId());
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mInfoList.size();
        }
    }
}
