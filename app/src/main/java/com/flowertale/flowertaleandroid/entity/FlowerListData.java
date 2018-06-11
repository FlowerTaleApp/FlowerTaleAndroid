package com.flowertale.flowertaleandroid.entity;

public class FlowerListData {
    private String headImg;
    private String name;
    private String description;

    public String getHeadImg() {
        return headImg;
    }

    public String getName() {
        return name;
    }

    public FlowerListData(String headImg, String name, String description) {
        this.headImg = headImg;
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
