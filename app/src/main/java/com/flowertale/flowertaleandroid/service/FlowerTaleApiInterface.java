package com.flowertale.flowertaleandroid.service;

import com.flowertale.flowertaleandroid.DTO.Response.BaseResponse;
import com.flowertale.flowertaleandroid.DTO.UserDTO;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface FlowerTaleApiInterface {

    @GET("users")
    Call<BaseResponse<UserDTO>> getUserInfo();

    @POST("users/actions/login")
    @FormUrlEncoded
    Call<BaseResponse<String>> doLogin(@Field("username") String username, @Field("password") String password);

    @POST("users/actions/register")
    @FormUrlEncoded
    Call<BaseResponse<String>> doRegister(@Field("username") String username, @Field("password") String password, @Field("email") String email);

    @GET("users/actions/logout")
    Call<BaseResponse> doLogout();
}
