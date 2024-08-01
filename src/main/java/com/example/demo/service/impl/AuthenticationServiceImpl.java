package com.example.demo.service.impl;

import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.LoginUserDTO;
import com.example.demo.dto.RegisterUserDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.enums.RoleEnum;
import com.example.demo.exception.BusinessException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.IAuthenticationService;
import com.example.demo.service.JwtTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthenticationServiceImpl implements IAuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public AuthenticationServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                                     PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                                     JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public UserDTO registerUser(RegisterUserDTO registerUser, boolean isAdmin) {
        Optional<User> userExists = userRepository.findByEmail(registerUser.getEmail());
        if (userExists.isPresent()) {
            throw new BusinessException("User already exists");
        }
        User newUser = new User();
        newUser.setFullName(registerUser.getFullName());
        newUser.setEmail(registerUser.getEmail());
        newUser.setRoles(getRolesUser(isAdmin));
        newUser.setIsAdmin(isAdmin);
        newUser.setPassword(passwordEncoder.encode(registerUser.getPassword()));

        newUser = userRepository.save(newUser);
        return UserMapper.entityToUserDTO(newUser);
    }

    private Set<Role> getRolesUser(boolean isAdmin) {
        if (isAdmin) {
            return Set.of(getRoleUser(), getRoleAdmin());
        }
        return Set.of(getRoleUser());
    }

    private Role getRoleUser() {
        Optional<Role> roleOptional = roleRepository.findByName(RoleEnum.ROLE_USER);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        }

        Role role = new Role();
        role.setName(RoleEnum.ROLE_USER);
        return roleRepository.save(role);
    }

    private Role getRoleAdmin() {
        Optional<Role> roleOptional = roleRepository.findByName(RoleEnum.ROLE_ADMIN);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        }

        Role role = new Role();
        role.setName(RoleEnum.ROLE_ADMIN);
        return roleRepository.save(role);
    }

    @Override
    public LoginResponse authenticateUser(LoginUserDTO loginUser) {
        Optional<User> authenticatedUser = userRepository.findByEmail(loginUser.getEmail());
        if (authenticatedUser.isEmpty()) {
            throw new BusinessException("User not found");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getEmail(),
                        loginUser.getPassword()
                )
        );

        String jwtToken = jwtTokenService.generateToken(authenticatedUser.get());
        LoginResponse userLogin = UserMapper.entityToLoginResponse(authenticatedUser.get());
        userLogin.setToken(jwtToken);
        userLogin.setExpiresIn(jwtTokenService.getExpirationTime());

        return userLogin;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return UserMapper.entityToUserDTOList(users);
    }
}
