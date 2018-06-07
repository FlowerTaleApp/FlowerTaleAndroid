package com.flowertale.flowertaleandroid.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class RecognitionResult implements Serializable {

    @SerializedName("Name")
    private String name;

    @SerializedName("LatinName")
    private String latinName;

    @SerializedName("AliasName")
    private String aliasName;

    @SerializedName("AliasList")
    private List<String> AliasList;

    @SerializedName("Family")
    private String family;

    @SerializedName("Genus")
    private String genus;

    @SerializedName("Score")
    private float score;

    @SerializedName("ImageUrl")
    private String imageUrl;

    @SerializedName("InfoCode")
    private String infoCode;
}
