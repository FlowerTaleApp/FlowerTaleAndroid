package com.flowertale.flowertaleandroid.DTO;

import java.sql.Time;

import lombok.Data;

@Data
public class ItemDTO {

    private Integer type;

    private String time;

    public ItemDTO(Integer type, String time) {
        this.type = type;
        this.time = time;
    }
}
