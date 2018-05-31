package com.flowertale.flowertaleandroid.recognise;

import com.flowertale.flowertaleandroid.http.ResponseData;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HblPlantApiService {

    private static final String BASE_URL = "http://plantgw.nongbangzhu.cn/";
    private static final String APP_CODE = "";
    private static OkHttpClient client = new OkHttpClient();

    public static ResponseData recognise(String imageBase64) {
        String apiUrl = BASE_URL + "plant/recognize2";
        RequestBody requestBody = new FormBody.Builder().add("img_base64", imageBase64).build();
        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization", "APPCODE " + APP_CODE)
                .post(requestBody)
                .build();
        return post(request);
    }

    public static ResponseData info(String code) {
        String apiUrl = BASE_URL + "plant/info";
        RequestBody requestBody = new FormBody.Builder().addEncoded("code", code).build();
        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization", "APPCODE " + APP_CODE)
                .post(requestBody)
                .build();
        return post(request);
    }

    private static ResponseData post(Request request) {
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResponseData baseResponse = new ResponseData();
        baseResponse.setStatusCode(response.code());
        try {
            baseResponse.setBody(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baseResponse;
    }

    public static ResponseData testRecognise() {
        ResponseData responseData = new ResponseData();
        responseData.setStatusCode(200);
        responseData.setBody("{\"Status\":0,\"Message\":\"OK\",\"Result\":[{\"Score\":\"67.44\",\"AliasList\":[],\"Genus\":\"杜鹃属\",\"InfoCode\":\"7dqMI5aOfyzZt5nZ\",\"AliasName\":\"\",\"Family\":\"杜鹃花科\",\"ImageUrl\":\"https://api.aiplants.cn/resource/1/%E7%BA%A2%E6%A3%95%E6%9D%9C%E9%B9%83/2089243.jpg\",\"LatinName\":\"Rhododendron rubiginosum\",\"Name\":\"红棕杜鹃\"},{\"Score\":\"3.43\",\"AliasList\":[],\"Genus\":\"报春苣苔属\",\"InfoCode\":\"LRafLuP2VldOY6os\",\"AliasName\":\"\",\"Family\":\"苦苣苔科\",\"ImageUrl\":\"https://api.aiplants.cn/resource/1/%E8%9A%82%E8%9D%97%E4%B8%83/1304349.jpg\",\"LatinName\":\"Primulina fimbrisepala\",\"Name\":\"蚂蟥七\"},{\"Score\":\"3.33\",\"AliasList\":[],\"Genus\":\"无柱兰属\",\"InfoCode\":\"wAT18j4IlaY6zBqf\",\"AliasName\":\"\",\"Family\":\"兰科\",\"ImageUrl\":\"https://api.aiplants.cn/resource/1/%E5%A4%A7%E8%8A%B1%E6%97%A0%E6%9F%B1%E5%85%B0/2007546.jpg\",\"LatinName\":\"Amitostigma pinguicula\",\"Name\":\"大花无柱兰\"},{\"Score\":\"2.45\",\"AliasList\":[],\"Genus\":\"杜鹃属\",\"InfoCode\":\"qgHJL1jEKmRuhMS2\",\"AliasName\":\"\",\"Family\":\"杜鹃花科\",\"ImageUrl\":\"https://api.aiplants.cn/resource/1/%E5%A4%A7%E7%8E%8B%E6%9D%9C%E9%B9%83/2088437.jpg\",\"LatinName\":\"Rhododendron rex\",\"Name\":\"大王杜鹃\"},{\"Score\":\"1.79\",\"AliasList\":[],\"Genus\":\"岩白菜属\",\"InfoCode\":\"PiRaWAmreLjZH1xf\",\"AliasName\":\"\",\"Family\":\"虎耳草科\",\"ImageUrl\":\"https://api.aiplants.cn/resource/1/%E5%8E%9A%E5%8F%B6%E5%B2%A9%E7%99%BD%E8%8F%9C/2614834.jpg\",\"LatinName\":\"Bergenia crassifolia\",\"Name\":\"厚叶岩白菜\"}]}");
        return responseData;
    }

}
