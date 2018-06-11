package com.flowertale.flowertaleandroid.DTO;

import java.util.List;

import lombok.Data;

@Data
public class PlantDTO {

    private Integer id;
    private String name;
    private String description;
    private List<SchemeDTO> schemeDTO;

    public PlantDTO(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
