package com.flowertale.flowertaleandroid.DTO;

import java.util.List;

import lombok.Data;

@Data
public class SchemeDTO {

    private Integer id;
    private String name;
    private String description;
    private List<ItemDTO> itemDTOList;

    public SchemeDTO(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
