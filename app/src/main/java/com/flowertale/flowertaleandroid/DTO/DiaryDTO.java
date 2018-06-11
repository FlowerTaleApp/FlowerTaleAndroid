package com.flowertale.flowertaleandroid.DTO;

import java.util.Date;

import lombok.Data;

@Data
public class DiaryDTO {

    private Integer plantId;
    private String plantName;
    private String content;
    private String imageUrl;
    private Date date;
    private Integer type;
    private String username;

    public DiaryDTO(Integer plantId, String plantName, String content, String imageUrl, Date date, Integer type, String username) {
        this.plantId = plantId;
        this.plantName = plantName;
        this.content = content;
        this.imageUrl = imageUrl;
        this.date = date;
        this.type = type;
        this.username = username;
    }
}
