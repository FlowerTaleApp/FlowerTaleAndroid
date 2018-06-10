package com.flowertale.flowertaleandroid.DTO;

import lombok.Data;

@Data
public class SimpleUserDTO {

    private Integer id;
    private String username;

    public SimpleUserDTO(Integer id, String username) {
        this.id = id;
        this.username = username;
    }
}
