package com.flowertale.flowertaleandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.flowertale.flowertaleandroid.DTO.PlantDTO;
import com.flowertale.flowertaleandroid.DTO.SimpleTeamDTO;
import com.flowertale.flowertaleandroid.DTO.UserDTO;
import com.flowertale.flowertaleandroid.DTO.response.BaseResponse;
import com.flowertale.flowertaleandroid.entity.FlowerInfoItem;
import com.flowertale.flowertaleandroid.entity.MemberItem;
import com.flowertale.flowertaleandroid.service.FlowerTaleApiService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class FlowerFragment extends Fragment {

    private SwipeRefreshLayout swipeRefresh;
    private static final int ADD = 1;
    private static final int SWITCH = 2;
    private int[] flowerImages = {R.drawable.flower, R.drawable.flower2};
    /*private FlowerInfoItem[] infoItems = { new FlowerInfoItem("桃花养成计划", R.drawable.flower, "FlowerTale"),  //测试数据
                                      new FlowerInfoItem("月季生长日记", R.drawable.flower2,"FlowerTale")};*/
    private List<FlowerInfoItem> infoItemList = new ArrayList<>();
    private List<Integer> plantIdList = new ArrayList<>();
    private InfoAdapter adapter;
    private SimpleTeamDTO currentTeam;
    private int teamId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_flower, container, false);
        setHasOptionsMenu(true);


        FloatingActionButton addFab = view.findViewById(R.id.add_fab);        //添加养护信息按钮
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FlowerAddActivity.class);
                intent.putExtra("teamId", currentTeam.getId());
                startActivityForResult(intent, ADD);
            }
        });

        setInitialGroup(view);

        swipeRefresh = view.findViewById(R.id.fragment_flower_refresh);         //刷新养护信息
        swipeRefresh.setColorSchemeResources(R.color.mistyrose);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setInitialGroup(view);
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADD:                                                                                  //新增养护信息结果
                if (resultCode == RESULT_OK) {
                    int teamId = data.getIntExtra("teamId", -1);
                    setPlantInfo(teamId);
                }
                break;
            case SWITCH:                                                                                //切换群组
                if (resultCode == RESULT_OK) {
                    int teamId = data.getIntExtra("teamId", -1);
                    groupChanged(teamId);
                }
                break;
            default:
        }
    }


    private class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView infoImage;
        TextView infoTitle;
        TextView infoName;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            infoImage = view.findViewById(R.id.info_item_image);
            infoTitle = view.findViewById(R.id.info_item_title);
            infoName = view.findViewById(R.id.info_item_name);
        }
    }

    private class InfoAdapter extends RecyclerView.Adapter<ViewHolder> {
        private Context mContext;
        private List<FlowerInfoItem> mInfoList;
        private List<Integer> mPlantIdList;
        private int mTeamId;

        public InfoAdapter(List<FlowerInfoItem> infoList, List<Integer> plantIdList, int teamId) {
            mInfoList = infoList;
            mPlantIdList = plantIdList;
            mTeamId = teamId;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (mContext == null) {
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
                    Intent intent = new Intent(mContext, FlowerDetailsActivity.class);
                    intent.putExtra("teamId", mTeamId);
                    intent.putExtra("plantId", mPlantIdList.get(index));
                    intent.putExtra(FlowerDetailsActivity.INFO_TITLE, flowerInfoItem.getTitle());
                    intent.putExtra(FlowerDetailsActivity.INFO_IMAGE_ID, flowerInfoItem.getImageId());
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mInfoList.size();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.group_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.group_add:
                Intent createIntent = new Intent(getActivity(), GroupCreateActivity.class);
                startActivity(createIntent);
                break;
            case R.id.group_change:
                Intent switchIntent = new Intent(getActivity(), GroupSwitchActivity.class);
                switchIntent.putExtra("teamName", currentTeam.getName());
                startActivityForResult(switchIntent, SWITCH);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);

    }

    private void setInitialGroup(final View view) {                                                 //钦定初始群组

        FlowerTaleApiService.getInstance().doGetUserTeams().enqueue(new Callback<BaseResponse<List<SimpleTeamDTO>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<SimpleTeamDTO>>> call, Response<BaseResponse<List<SimpleTeamDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        List<SimpleTeamDTO> teams = response.body().getObject();
                        currentTeam = teams.get(0);
                        teamId = currentTeam.getId();

                        RecyclerView recyclerView = view.findViewById(R.id.raising_info_view);        //各养护信息展示
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new InfoAdapter(infoItemList, plantIdList, currentTeam.getId());
                        recyclerView.setAdapter(adapter);

                        setPlantInfo(teamId);
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                } else {
                    Toast.makeText(getActivity(), "未知错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<SimpleTeamDTO>>> call, Throwable t) {
                Toast.makeText(getActivity(), "未知错误", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

    }

    private void setPlantInfo(int teamId) {                                          //设置展示植物信息
        FlowerTaleApiService.getInstance().doGetPlantsByTeamId(teamId).enqueue(new Callback<BaseResponse<List<PlantDTO>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<PlantDTO>>> call, Response<BaseResponse<List<PlantDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        List<PlantDTO> plantDTOS = response.body().getObject();
                        for (int i = 0; i < plantDTOS.size(); i++) {
                            Random random = new Random();
                            int index = random.nextInt(flowerImages.length);
                            infoItemList.add(i,new FlowerInfoItem(plantDTOS.get(0).getDescription(), flowerImages[index], plantDTOS.get(0).getName()));
                            plantIdList.add(i,plantDTOS.get(i).getId());
                        }
                        infoItemList.subList(plantDTOS.size(), infoItemList.size()).clear();
                        plantIdList.subList(plantDTOS.size(), plantIdList.size()).clear();
                        if (plantDTOS.size()==0) {
                            new MaterialDialog.Builder(getActivity())
                                    .content("这个群组还没有植物，快加入你所心爱的植物吧")
                                    .positiveText("确认")
                                    .show();
                        }
                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                } else {
                    Toast.makeText(getActivity(), "setPlantInfo未知错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<PlantDTO>>> call, Throwable t) {
                Toast.makeText(getActivity(), "setPlantInfoFailed", Toast.LENGTH_SHORT).show();
               /* getActivity().finish();*/
            }
        });

    }

    private void groupChanged(int teamId){
        FlowerTaleApiService.getInstance().doGetUserTeams().enqueue(new Callback<BaseResponse<List<SimpleTeamDTO>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<SimpleTeamDTO>>> call, Response<BaseResponse<List<SimpleTeamDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        List<SimpleTeamDTO> simpleTeamDTOS = response.body().getObject();
                        for (int i=0;i<simpleTeamDTOS.size();i++){
                            if (simpleTeamDTOS.get(i).getId()==teamId){
                                currentTeam.setId(teamId);
                                currentTeam.setName(simpleTeamDTOS.get(i).getName());
                            }
                        }
                        setPlantInfo(teamId);

                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                } else {
                    Toast.makeText(getActivity(), "未知错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<SimpleTeamDTO>>> call, Throwable t) {
                Toast.makeText(getActivity(), "未知错误", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });
    }
}
