package com.flowertale.flowertaleandroid.entity;

public class FlowerListData {
    private String headImg;
    private String name;
    private String description;
    private String code;

    public String getHeadImg() {
        return headImg;
    }

    public String getName() {
        return name;
    }

    public FlowerListData(String headImg, String name, String description, String code) {
        this.headImg = headImg;
        this.name = name;
        this.description = description;
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }
}
