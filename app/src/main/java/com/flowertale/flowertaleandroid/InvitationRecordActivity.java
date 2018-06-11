package com.flowertale.flowertaleandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.flowertale.flowertaleandroid.adapter.InvatationRecordAdapter;
import com.flowertale.flowertaleandroid.entity.InvitationRecord;

import java.util.ArrayList;
import java.util.List;

public class InvitationRecordActivity extends AppCompatActivity implements View.OnClickListener {
    private List<InvitationRecord> invitationRecordList = new ArrayList<>();
    private ImageView mPlantSearchBackImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_invitation_list);
        initView();
        initRecords();
        RecyclerView recyclerView = findViewById(R.id.group_invitation_list_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        InvatationRecordAdapter adapter = new InvatationRecordAdapter(invitationRecordList);
        recyclerView.setAdapter(adapter);
    }

    private void initRecords() {
        InvitationRecord record1 = new InvitationRecord("爸爸", "幸福一家人");
        invitationRecordList.add(record1);
        InvitationRecord record2 = new InvitationRecord("妈妈", "幸福的一家");
        invitationRecordList.add(record2);
    }

    private void initView() {
        mPlantSearchBackImage = (ImageView) findViewById(R.id.plant_search_back_image);
        mPlantSearchBackImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.plant_search_back_image:
                finish();
                break;
        }
    }
}
