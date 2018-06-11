package com.flowertale.flowertaleandroid.Enum;

import lombok.Getter;

@Getter
public enum NurtureType implements CodeEnum {

    WATER(0, "浇水"),
    FERTILIZE(1, "施肥"),
    PRUNE(2, "修剪"),
    SUNBATHE(3, "光照");

    private Integer code;
    private String message;

    NurtureType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
