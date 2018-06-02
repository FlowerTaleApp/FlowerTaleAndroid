package com.flowertale.flowertaleandroid;

public class FlowerRecord {
    private int recordImage;
    private String name;
    /*private String water;
    private String fertilize;
    private String prune;
    private String sunshine;*/
    private String desc;
    private String date;

    public int getRecordImage() {
        return recordImage;
    }

    public void setRecordImage(int recordImage) {
        this.recordImage = recordImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /*public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getFertilize() {
        return fertilize;
    }

    public void setFertilize(String fertilize) {
        this.fertilize = fertilize;
    }

    public String getPrune() {
        return prune;
    }

    public void setPrune(String prune) {
        this.prune = prune;
    }

    public String getSunshine() {
        return sunshine;
    }

    public void setSunshine(String sunshine) {
        this.sunshine = sunshine;
    }*/

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public FlowerRecord(int recordImage, String name, String desc, String date) {
        this.recordImage = recordImage;
        this.name = name;
        this.desc = desc;
        this.date = date;
    }
}
