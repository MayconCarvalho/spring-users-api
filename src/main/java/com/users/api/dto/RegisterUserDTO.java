package com.users.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RegisterUserDTO implements Serializable {

    private String email;
    private String password;
    private String fullName;
}
