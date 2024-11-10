package com.users.api.service;

import com.users.api.dto.LoginResponse;
import com.users.api.dto.LoginUserDTO;
import com.users.api.dto.RegisterUserDTO;
import com.users.api.dto.UserDTO;

import java.util.List;

public interface IAuthenticationService {

    UserDTO registerUser(RegisterUserDTO registerUser, boolean isAdmin);
    LoginResponse authenticateUser(LoginUserDTO loginUser);
    List<UserDTO> getAllUsers();
}
