package com.flowertale.flowertaleandroid;

public class FlowerInfoItem {
    private String title;
    private int imageId;
    private String name;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FlowerInfoItem(String title, int imageId, String name) {
        this.title = title;
        this.imageId = imageId;
        this.name = name;
    }
}
