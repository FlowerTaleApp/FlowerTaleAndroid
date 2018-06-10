package com.flowertale.flowertaleandroid.service;

import android.preference.PreferenceManager;

import com.flowertale.flowertaleandroid.constant.TokenConstant;
import com.flowertale.flowertaleandroid.util.ContextUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
}
