package com.flowertale.flowertaleandroid.DTO;

import lombok.Data;

@Data
public class SimpleFlowerDTO {

    private String nameCn;
    private String nameLt;
    private String family;
    private String genus;
    private String alias;
    private String imageUrl;
    private String code;

    public SimpleFlowerDTO(String nameCn, String nameLt, String family, String genus, String alias, String imageUrl, String code) {
        this.nameCn = nameCn;
        this.nameLt = nameLt;
        this.family = family;
        this.genus = genus;
        this.alias = alias;
        this.imageUrl = imageUrl;
        this.code = code;
    }
}
