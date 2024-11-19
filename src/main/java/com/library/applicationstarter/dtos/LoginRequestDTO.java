package com.library.applicationstarter.dtos;

import lombok.Data;

@Data
public class LoginRequestDTO {

    private String username;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
}
