package com.flowertale.flowertaleandroid.service;

import com.flowertale.flowertaleandroid.DTO.DailyPushFlowerDTO;
import com.flowertale.flowertaleandroid.DTO.DetailedFlowerDTO;
import com.flowertale.flowertaleandroid.DTO.DiaryDTO;
import com.flowertale.flowertaleandroid.DTO.InvitationDTO;
import com.flowertale.flowertaleandroid.DTO.NurtureCompletionDTO;
import com.flowertale.flowertaleandroid.DTO.PlantDTO;
import com.flowertale.flowertaleandroid.DTO.SchemeDTO;
import com.flowertale.flowertaleandroid.DTO.SimpleFlowerDTO;
import com.flowertale.flowertaleandroid.DTO.SimpleTeamDTO;
import com.flowertale.flowertaleandroid.DTO.SimpleUserDTO;
import com.flowertale.flowertaleandroid.DTO.TeamDTO;
import com.flowertale.flowertaleandroid.DTO.UserDTO;
import com.flowertale.flowertaleandroid.DTO.form.DetailedFlowerForm;
import com.flowertale.flowertaleandroid.DTO.form.DiaryForm;
import com.flowertale.flowertaleandroid.DTO.form.PlantForm;
import com.flowertale.flowertaleandroid.DTO.form.SchemeForm;
import com.flowertale.flowertaleandroid.DTO.form.SimpleFlowerForm;
import com.flowertale.flowertaleandroid.DTO.response.BaseResponse;
import com.flowertale.flowertaleandroid.DTO.response.BaseResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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

    @POST("users/actions/search")
    @FormUrlEncoded
    Call<BaseResponse<List<SimpleUserDTO>>> doSearchUsername(@Field("username") String username);

    @POST("teams")
    @FormUrlEncoded
    Call<BaseResponse> doCreateTeam(@Field("name") String name, @Field("description") String description);

    @POST("teams/actions/delete")
    @FormUrlEncoded
    Call<BaseResponse> doDeleteTeam(@Field("teamId") Integer teamId);

    @POST("teams/actions/invite")
    @FormUrlEncoded
    Call<BaseResponse> doInviteUser(@Field("username") String username, @Field("teamId") Integer teamId);

    @POST("teams/actions/join")
    @FormUrlEncoded
    Call<BaseResponse> doJoinTeam(@Field("inviteId") Integer inviteId);

    @POST("teams/actions/leave")
    @FormUrlEncoded
    Call<BaseResponse> doLeaveTeam(@Field("teamId") Integer teamId);

    @GET("teams/{id}")
    Call<BaseResponse<TeamDTO>> doGetDetailedTeam(@Path("id") Integer teamId);

    @GET("teams/invitations")
    Call<BaseResponse<List<InvitationDTO>>> doGetUnprocessedInvitations();

    @GET("teams")
    Call<BaseResponse<List<SimpleTeamDTO>>> doGetUserTeams();

    @GET("plants/teams/{id}")
    Call<BaseResponse<List<PlantDTO>>> doGetPlantsByTeamId(@Path("id") Integer teamId);

    @GET("plants/{id}/schemes")
    Call<BaseResponse<List<SchemeDTO>>> doGetSchemeByPlantId(@Path("id") Integer plantId);

    @POST("plants/schemes")
    Call<BaseResponse> doCreateScheme(@Body SchemeForm schemeForm);

    @POST("plants/actions/adopt")
    @FormUrlEncoded
    Call<BaseResponse> adoptScheme(@Field("plantId") Integer plantId, @Field("schemeId") Integer schemeId);

    @POST("plants/schemes/actions/delete")
    @FormUrlEncoded
    Call<BaseResponse> doDeleteScheme(@Field("schemeId") Integer schemeId);

    @POST("plants")
    Call<BaseResponse> doCreatePlant(@Body PlantForm plantForm);

    @POST("plants/actions/delete")
    @FormUrlEncoded
    Call<BaseResponse> doDeletePlant(@Field("plantId") Integer plantId);

    @GET("diaries/teams/{id}")
    Call<BaseResponse<List<DiaryDTO>>> doGetDiariesByTeamId(@Path("id") Integer teamId);

    @GET("diaries/plants/{id}")
    Call<BaseResponse<List<DiaryDTO>>> doGetDiariesByPlantId(@Path("id") Integer plantId);

    @GET("diaries/current")
    Call<BaseResponse<List<DiaryDTO>>> doGetCurrentDayUserRelatedDiaries();

    @GET("diaries/user")
    Call<BaseResponse<List<DiaryDTO>>> doGetUserPostedDiaries();

    @POST("diaries")
    Call<BaseResponse> doCreateDiary(@Body DiaryForm diaryForm);

    @POST("diaries/actions/delete")
    @FormUrlEncoded
    Call<BaseResponse> doDeleteDiary(@Field("diaryId") Integer diaryId);

    @GET("nurture/completion")
    Call<BaseResponse<List<NurtureCompletionDTO>>> doGetCurrentDayNurtureCompletion();

    @GET("users/actions/verify")
    Call<BaseResponse> doVerifyToken();

    @POST("flowers/search")
    @FormUrlEncoded
    Call<BaseResponse<List<SimpleFlowerDTO>>> doSearchFlowersByName(@Field("name") String name);

    @GET("flowers/today")
    Call<BaseResponse<List<DailyPushFlowerDTO>>> doGetDailyPushFlowers();

    @POST("flowers")
    @FormUrlEncoded
    Call<BaseResponse<DetailedFlowerDTO>> doGetDetailedFlower(@Field("code") String code);

    @POST("flowers/simple")
    Call<BaseResponse> doCreateSimpleFlower(@Body List<SimpleFlowerForm> simpleFlowerFormList);

    @POST("flowers/details")
    Call<BaseResponse> doCreateDetailedFlower(@Body DetailedFlowerForm detailedFlowerForm);

}
