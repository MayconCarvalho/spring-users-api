package com.example.demo.service;

import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.LoginUserDTO;
import com.example.demo.dto.RegisterUserDTO;
import com.example.demo.dto.UserDTO;

import java.util.List;

public interface IAuthenticationService {

    UserDTO registerUser(RegisterUserDTO registerUser, boolean isAdmin);
    LoginResponse authenticateUser(LoginUserDTO loginUser);
    List<UserDTO> getAllUsers();
}
