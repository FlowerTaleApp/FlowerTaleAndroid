package com.flowertale.flowertaleandroid.DTO.form;

import lombok.Data;

@Data
public class SimpleFlowerForm {

    private String nameCn;
    private String nameLt;
    private String alias;
    private String family;
    private String genus;
    private String imageUrl;
    private String code;

    public SimpleFlowerForm(String nameCn, String nameLt, String alias, String family, String genus, String imageUrl, String code) {
        this.nameCn = nameCn;
        this.nameLt = nameLt;
        this.alias = alias;
        this.family = family;
        this.genus = genus;
        this.imageUrl = imageUrl;
        this.code = code;
    }
}
