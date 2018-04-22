package com.flowertale.flowertaleandroid;

import java.io.Serializable;

public class ApiRecResultItem implements Serializable {
    private String name;
    private Double cred;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCred() {
        return cred;
    }

    public void setCred(Double cred) {
        this.cred = cred;
    }
}
