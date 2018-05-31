package com.flowertale.flowertaleandroid.http;

import lombok.Data;

@Data
public class ResponseData {
    private int statusCode;
    private String body;
    private Exception exception;
}
