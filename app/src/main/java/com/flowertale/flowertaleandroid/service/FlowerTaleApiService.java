package com.flowertale.flowertaleandroid.service;

import android.preference.PreferenceManager;
import android.widget.Toast;

import com.flowertale.flowertaleandroid.DTO.form.DetailedFlowerForm;
import com.flowertale.flowertaleandroid.DTO.form.SimpleFlowerForm;
import com.flowertale.flowertaleandroid.DTO.response.BaseResponse;
import com.flowertale.flowertaleandroid.bean.PlantInfoResult;
import com.flowertale.flowertaleandroid.bean.RecognitionResult;
import com.flowertale.flowertaleandroid.constant.TokenConstant;
import com.flowertale.flowertaleandroid.util.ContextUtil;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlowerTaleApiService {

    private static String baseUrl = "http://47.100.253.251/flowertale/";

    private static FlowerTaleApiInterface flowerTaleApiInterfaceInstance = null;

    public static FlowerTaleApiInterface getInstance() {
        Interceptor tokenInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request tokenRequest;
                String token = PreferenceManager.getDefaultSharedPreferences(ContextUtil.getContext())
                        .getString(TokenConstant.TOKEN, null);
                if (token == null) {
                    return chain.proceed(originalRequest);
                }
                tokenRequest = originalRequest.newBuilder().header(TokenConstant.TOKEN, token).build();
                return chain.proceed(tokenRequest);
            }
        };
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(tokenInterceptor)
                .build();
        if (flowerTaleApiInterfaceInstance == null) {
            synchronized (FlowerTaleApiInterface.class) {
                if (flowerTaleApiInterfaceInstance == null) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    flowerTaleApiInterfaceInstance = retrofit.create(FlowerTaleApiInterface.class);
                }
            }
        }
        return flowerTaleApiInterfaceInstance;
    }

    public static void saveSimpleFlower(List<RecognitionResult> resultList) {
        List<SimpleFlowerForm> flowerFormList = resultList.stream().map(e -> new SimpleFlowerForm(e.getName(),
                e.getLatinName(), e.getAliasName(), e.getFamily(), e.getGenus(), e.getImageUrl(), e.getInfoCode()))
                .collect(Collectors.toList());
        FlowerTaleApiService.getInstance().doCreateSimpleFlower(flowerFormList).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
//                    Toast.makeText(ContextUtil.getContext(), "Yep!", Toast.LENGTH_SHORT).show();
                    System.out.println("Simple : save");
                } else {
//                    Toast.makeText(ContextUtil.getContext(), "Ops...", Toast.LENGTH_SHORT).show();
                    System.out.println("Simple : not save");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
//                Toast.makeText(ContextUtil.getContext(), "Failed...", Toast.LENGTH_SHORT).show();
                System.out.println("Simple : failed!");
            }
        });
    }

    public static void saveDetailedFlower(PlantInfoResult plantInfoResult, String code) {
        DetailedFlowerForm detailedFlowerForm = new DetailedFlowerForm();
        detailedFlowerForm.setCode(code);
        detailedFlowerForm.setNameCn(plantInfoResult.getNameStd());
        detailedFlowerForm.setNameLt(plantInfoResult.getNameLt());
        detailedFlowerForm.setAlias(plantInfoResult.getAlias());
        detailedFlowerForm.setFamily(plantInfoResult.getFamilyCn());
        detailedFlowerForm.setGenus(plantInfoResult.getGenusCn());
        detailedFlowerForm.setDescription(plantInfoResult.getDescription());
        detailedFlowerForm.setImageUrlList(plantInfoResult.getImages());
        if (plantInfoResult.getInfo() != null) {
            detailedFlowerForm.setCharacteristic(plantInfoResult.getInfo().getCharacteristic());
            detailedFlowerForm.setDistribution(plantInfoResult.getInfo().getArea());
            detailedFlowerForm.setNameHistory(plantInfoResult.getInfo().getNameHistory());
            detailedFlowerForm.setPoem(plantInfoResult.getInfo().getPoem());
            detailedFlowerForm.setFloweringPeriod(plantInfoResult.getInfo().getFlowering());
            detailedFlowerForm.setNurtureTech(plantInfoResult.getInfo().getFlowering());
            detailedFlowerForm.setMeaning(plantInfoResult.getInfo().getMeaning());
            detailedFlowerForm.setValue(plantInfoResult.getInfo().getValue());
        }
        FlowerTaleApiService.getInstance().doCreateDetailedFlower(detailedFlowerForm).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    System.out.println("Detail : save");
                } else {
                    System.out.println("Detail : not save");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                System.out.println("Detail : failed!");
            }
        });
    }
}
