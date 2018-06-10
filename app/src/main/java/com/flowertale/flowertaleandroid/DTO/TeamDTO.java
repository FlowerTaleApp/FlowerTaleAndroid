package com.flowertale.flowertaleandroid.DTO;

import java.util.List;

import lombok.Data;

@Data
public class TeamDTO {

    private Integer id;
    private String name;
    private String description;
    private List<SimpleUserDTO> memberList;
}
