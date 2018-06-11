package com.flowertale.flowertaleandroid.DTO.form;

import lombok.Data;

@Data
public class DiaryForm {

    private Integer plantId;

    private String content;

    private String imageUrl;

    private Integer type;
}
