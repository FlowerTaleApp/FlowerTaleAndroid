package com.flowertale.flowertaleandroid.DTO.form;

import lombok.Data;

import java.util.List;

@Data
public class DetailedFlowerForm {

    private String code;
    private String nameCn;
    private String nameLt;
    private String family;
    private String genus;
    private String alias;
    private String description;
    private String poem;
    private String value;
    private String meaning;
    private String distribution;
    private String nameHistory;
    private String nurtureTech;
    private String characteristic;
    private String floweringPeriod;
    private List<String> imageUrlList;
}
