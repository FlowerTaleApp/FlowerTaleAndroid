package com.flowertale.flowertaleandroid.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class PlantInfoResult implements Serializable {

    @SerializedName("nameStd")
    private String nameStd;

    @SerializedName("nameLt")
    private String nameLt;

    @SerializedName("familyCn")
    private String familyCn;

    @SerializedName("genusCn")
    private String genusCn;

    @SerializedName("alias")
    private String alias;

    @SerializedName("description")
    private String description;

    @SerializedName("info")
    private PlantOtherInfoResult info;

    @SerializedName("images")
    private List<String> images;
}
