package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginUserDTO implements Serializable {

    private String email;
    private String password;
}
