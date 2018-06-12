package com.flowertale.flowertaleandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flowertale.flowertaleandroid.PlantInfoActivity;
import com.flowertale.flowertaleandroid.R;
import com.flowertale.flowertaleandroid.entity.FlowerListData;

import java.util.List;

public class FlowerListAdapter extends ArrayAdapter<FlowerListData>{
    private int resourceId;

    public FlowerListAdapter(@NonNull Context context, int resource, @NonNull List<FlowerListData> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final FlowerListData flower = getItem(position);
        View view;
        if(convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        }else {
            view = convertView;
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PlantInfoActivity.class);
                intent.putExtra(PlantInfoActivity.PLANT_CODE, flower.getCode());
                intent.putExtra(PlantInfoActivity.SOURCE, PlantInfoActivity.FROM_DISCOVER);
                getContext().startActivity(intent);
            }
        });
        ImageView headImg = view.findViewById(R.id.flower_list_head_img);
        TextView name = view.findViewById(R.id.flower_list_name);
        TextView description = view.findViewById(R.id.flower_list_description);
        name.setText(flower.getName());
        description.setText(flower.getDescription());
        Glide.with(getContext()).load(flower.getHeadImg()).into(headImg);
        return view;
    }
}
