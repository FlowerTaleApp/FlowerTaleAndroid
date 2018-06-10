package com.flowertale.flowertaleandroid.DTO;

import java.util.Date;

import lombok.Data;

@Data
public class UserDTO {

    private String username;
    private String email;
    private Date registrationTime;
}
