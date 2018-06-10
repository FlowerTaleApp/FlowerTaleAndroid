package com.flowertale.flowertaleandroid.DTO.Response;

import lombok.Data;

@Data
public class BaseResponse<T> {

    private Integer status;
    private String message;
    private T object;

}
