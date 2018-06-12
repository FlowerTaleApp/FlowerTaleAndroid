package com.flowertale.flowertaleandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.flowertale.flowertaleandroid.DTO.InvitationDTO;
import com.flowertale.flowertaleandroid.DTO.response.BaseResponse;
import com.flowertale.flowertaleandroid.Interface.OnProcessInvitationListener;
import com.flowertale.flowertaleandroid.adapter.InvatationRecordAdapter;
import com.flowertale.flowertaleandroid.entity.InvitationRecord;
import com.flowertale.flowertaleandroid.service.FlowerTaleApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitationRecordActivity extends AppCompatActivity implements View.OnClickListener {
    private List<InvitationRecord> invitationRecordList = new ArrayList<>();
    private ImageView mPlantSearchBackImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_invitation_list);
        initView();
        initRecords();
    }

    private void initRecords() {
        FlowerTaleApiService.getInstance().doGetUnprocessedInvitations().enqueue(new Callback<BaseResponse<List<InvitationDTO>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<InvitationDTO>>> call, Response<BaseResponse<List<InvitationDTO>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 0) {
                    List<InvitationDTO> invitationDTOList = response.body().getObject();
                    invitationRecordList = invitationDTOList.stream().map(e -> new InvitationRecord(e.getId(), e.getInviterName(),
                            e.getTeamName())).collect(Collectors.toList());
                }
                RecyclerView recyclerView = findViewById(R.id.group_invitation_list_view);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                InvatationRecordAdapter adapter = new InvatationRecordAdapter(invitationRecordList);
                adapter.setOnProcessInvitationListener(new OnProcessInvitationListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "Invitation accepted!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(getApplicationContext(), "Invitation rejected!", Toast.LENGTH_SHORT).show();
                    }
                });
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<BaseResponse<List<InvitationDTO>>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
        InvitationRecord record1 = new InvitationRecord(1,"爸爸", "幸福一家人");
        invitationRecordList.add(record1);
        InvitationRecord record2 = new InvitationRecord(2,"妈妈", "幸福的一家");
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
