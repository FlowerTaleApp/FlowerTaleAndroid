package com.flowertale.flowertaleandroid.DTO;

import lombok.Data;

@Data
public class SimpleTeamDTO {

    private Integer id;

    private String name;

    public SimpleTeamDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
