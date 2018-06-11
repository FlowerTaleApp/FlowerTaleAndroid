package com.flowertale.flowertaleandroid.Enum;

import lombok.Getter;

@Getter
public enum InviteStatus implements CodeEnum {

    PROCESSED(0, "已处理"),
    UNPROCESSED(1, "未处理"),;


    private Integer code;
    private String message;

    InviteStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
