package com.flowertale.flowertaleandroid.DTO;

import java.util.Date;

import lombok.Data;

@Data
public class DiaryDTO {

    private String content;
    private String imageUrl;
    private Date date;
    private Integer type;
    private String username;
}
