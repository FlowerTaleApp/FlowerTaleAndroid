package com.flowertale.flowertaleandroid.DTO;

import java.util.List;

import lombok.Data;

@Data
public class PlantDTO {

    private Integer id;
    private String name;
    private String description;
    private Integer schemeId;
    private List<SchemeDTO> schemeDTO;

    public PlantDTO(Integer id, String name, String description, Integer schemeId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.schemeId = schemeId;
    }
}
