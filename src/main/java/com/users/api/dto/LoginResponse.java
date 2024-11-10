package com.users.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoginResponse {

    private String email;
    private String fullName;
    private List<String> roles;
    private String token;
    private long expiresIn;
}
