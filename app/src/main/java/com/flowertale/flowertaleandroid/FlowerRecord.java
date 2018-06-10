package com.flowertale.flowertaleandroid;

public class FlowerRecord {
    private int recordImage;
    private String name;
    /*private String water;
    private String fertilize;
    private String prune;
    private String sunshine;*/
    private int type;
    private String desc;
    private String date;

    public FlowerRecord(int recordImage, String name, int type, String desc, String date) {
        this.recordImage = recordImage;
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.date = date;
    }

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

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
