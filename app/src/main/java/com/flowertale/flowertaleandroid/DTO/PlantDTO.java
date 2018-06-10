package com.flowertale.flowertaleandroid.DTO;

import lombok.Data;

@Data
public class PlantDTO {

    private Integer id;
    private String name;
    private String description;
    private SchemeDTO schemeDTO;
}
