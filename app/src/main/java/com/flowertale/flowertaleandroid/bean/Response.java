package com.flowertale.flowertaleandroid.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Response<T> {

    @SerializedName(value = "Status", alternate = "status")
    private int status;

    @SerializedName(value = "Message", alternate = "message")
    private String message;

    @SerializedName(value = "Result", alternate = "result")
    private T result;
}
