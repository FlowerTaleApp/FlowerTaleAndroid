package com.flowertale.flowertaleandroid;

public class MemberItem {

    private int selfImage;
    private String name;

    public MemberItem(int selfImage, String name) {
        this.selfImage = selfImage;
        this.name = name;
    }

    public int getSelfImage() {
        return selfImage;
    }

    public void setSelfImage(int selfImage) {
        this.selfImage = selfImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
