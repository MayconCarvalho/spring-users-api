package com.users.api.mapper;

import com.users.api.dto.LoginResponse;
import com.users.api.dto.UserDTO;
import com.users.api.entity.User;

import java.util.List;

public class UserMapper {

    public static LoginResponse entityToLoginResponse(User user) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setEmail(user.getEmail());
        loginResponse.setFullName(user.getFullName());
        loginResponse.setRoles(user.getRoles().stream().map(role -> role.getName().getValue()).toList());
        return loginResponse;
    }

    public static UserDTO entityToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setFullName(user.getFullName());
        userDTO.setRoles(user.getRoles().stream().map(role -> role.getName().getValue()).toList());
        return userDTO;
    }

    public static List<UserDTO> entityToUserDTOList(List<User> users) {
        return users.stream().map(UserMapper::entityToUserDTO).toList();
    }
}
