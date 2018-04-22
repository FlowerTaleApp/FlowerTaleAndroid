package com.flowertale.flowertaleandroid;

import android.content.Context;

import com.baidu.aip.imageclassify.AipImageClassify;

public class AipImageClassifyClient {

    private static AipImageClassify sAipImageClassify;

    private static final String APP_ID = "11136849";
    private static final String API_KEY = "RfYpN2OCh6IVsi2Z23zYFEcT";
    private static final String SECRET_KEY = "zLiaGxcZGuRzrffHcRfEDmhLrw3cee9m";

    public static AipImageClassify get(Context context) {
        if (sAipImageClassify == null) {
            sAipImageClassify=new AipImageClassify(APP_ID,API_KEY,SECRET_KEY);
        }
        return sAipImageClassify;
    }
}
