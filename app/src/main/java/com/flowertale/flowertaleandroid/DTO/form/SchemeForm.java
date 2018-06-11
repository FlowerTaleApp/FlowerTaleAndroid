package com.flowertale.flowertaleandroid.DTO.form;

import java.util.List;

import lombok.Data;

@Data
public class SchemeForm {

    private String name;

    private String description;

    private Integer plantId;

    private List<ItemForm> itemFormList;
}
