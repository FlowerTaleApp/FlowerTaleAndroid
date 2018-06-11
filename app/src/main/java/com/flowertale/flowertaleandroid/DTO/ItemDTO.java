package com.flowertale.flowertaleandroid.DTO;

import java.sql.Time;

import lombok.Data;

@Data
public class ItemDTO {

    private Integer type;

    private Time time;

    public ItemDTO(Integer type, Time time) {
        this.type = type;
        this.time = time;
    }
}
