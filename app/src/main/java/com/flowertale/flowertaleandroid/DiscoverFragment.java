package com.flowertale.flowertaleandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flowertale.flowertaleandroid.adapter.PhotoAdapter;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class DiscoverFragment extends Fragment implements MaterialSearchBar.OnSearchActionListener {
    protected RecyclerView mRecyclerView;
    private List<Integer> bannerImages;
    private List<String> bannerTitles;
    private Banner banner;
    private List<String> lastSearches;
    private MaterialSearchBar searchBar;
    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        bannerImages = new ArrayList<>();
        bannerImages.add(R.drawable.white_rose);
        bannerImages.add(R.drawable.hyacinth);
        bannerImages.add(R.drawable.violet);
        bannerTitles = new ArrayList<>();
        bannerTitles.add("白玫瑰-不为人知的美；纯洁的爱；甘心为你付出所有；高贵；智慧；尊敬；诚实");
        bannerTitles.add("蓝色风信子-生命、高贵浓郁；恒心；贞操；彷佛见到你一样高兴");
        bannerTitles.add("紫色紫罗兰-在美梦中爱上你、对我而言你永远那么美、小心翼翼守护的爱");
        mRecyclerView = view.findViewById(R.id.recycler_view);
        banner = (Banner) view.findViewById(R.id.banner);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(bannerImages);
        banner.setBannerAnimation(Transformer.DepthPage);
        banner.setBannerTitles(bannerTitles);
        banner.isAutoPlay(true);
        banner.setDelayTime(6000);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.start();
        searchBar = (MaterialSearchBar) view.findViewById(R.id.searchBar);
        searchBar.setOnSearchActionListener(this);
        searchBar.setHint("输入关键字搜索");
        ButterKnife.bind(mainActivity);

        String[] dataSet = null;
        try {
            dataSet = mainActivity.getAssets().list("demo-pictures");
        } catch (IOException e) {
            e.printStackTrace();
        }
        PhotoAdapter adapter = new PhotoAdapter(dataSet, mainActivity);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
            }
        });
        mRecyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_BACK:
                searchBar.disableSearch();
                break;
//            case MaterialSearchBar.BUTTON_NAVIGATION:
//                drawer.openDrawer(Gravity.LEFT);
//                break;
//            case MaterialSearchBar.BUTTON_SPEECH:
//                openVoiceRecognizer();
        }
    }


    @Override
    public void onSearchConfirmed(CharSequence text) {
        Toast.makeText(mainActivity, "searchComfirm" + text, Toast.LENGTH_SHORT).show();
//        startSearch(text.toString(), true, null, true);
        Intent intent = new Intent(mainActivity ,FlowerListActivity.class);
        intent.putExtra("searchKey",text.toString());
        mainActivity.startActivity(intent);
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        String s = enabled ? "enabled" : "disabled";
        Toast.makeText(getActivity(), "Search " + s, Toast.LENGTH_SHORT).show();
    }

    class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */
            //Glide 加载图片简单用法
            Glide.with(context).load(path).into(imageView);
            //Picasso 加载图片简单用法
            //Picasso.with(context).load(path).into(imageView);
        }
    }

    private void initData(){
//        FlowerTaleApiService.getInstance().doGetDailyPushFlowers().enqueue(new Callback<BaseResponse<List<DailyPushFlowerDTO>>>() {
//            @Override
//            public void onResponse(Call<BaseResponse<List<DailyPushFlowerDTO>>> call, Response<BaseResponse<List<DailyPushFlowerDTO>>> response) {
//                if(response.body()!=null&&response.isSuccessful()){
//                    if(response.body().getStatus()==0){
//                        List<DailyPushFlowerDTO> dailyPushFlowerDTOList=response.body().getObject();
//                        bannerTitles.clear();
//                        bannerImages.clear();
//                        for (DailyPushFlowerDTO dailyPushFlowerDTO : dailyPushFlowerDTOList) {
//                            bannerTitles.add(dailyPushFlowerDTO.getMeaning());
//                            bannerImages.add(dailyPushFlowerDTO.getImageUrl());
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BaseResponse<List<DailyPushFlowerDTO>>> call, Throwable t) {
//
//            }
//        });
    }
}



