package com.flowertale.flowertaleandroid.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class PlantOtherInfoResult implements Serializable {

    @SerializedName("xgsc")
    private String poem;

    @SerializedName("jzgy")
    private String value;

    @SerializedName("hyyy")
    private String meaning;

    @SerializedName("fbdq")
    private String area;

    @SerializedName("mcll")
    private String nameHistory;

    @SerializedName("yhjs")
    private String flowering;

    @SerializedName("bxtz")
    private String characteristic;

    @SerializedName("hksj")
    private String season;
}
