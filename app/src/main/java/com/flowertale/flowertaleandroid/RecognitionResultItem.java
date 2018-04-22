package com.flowertale.flowertaleandroid;

public class RecognitionResultItem {
    private String name;
    private String nameEng;
    private String classificationInfo;
    private double cred;

    public double getCred() {
        return cred;
    }

    public void setCred(double cred) {
        this.cred = cred;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEng() {
        return nameEng;
    }

    public void setNameEng(String nameEng) {
        this.nameEng = nameEng;
    }

    public String getClassificationInfo() {
        return classificationInfo;
    }

    public void setClassificationInfo(String classificationInfo) {
        this.classificationInfo = classificationInfo;
    }
}
