package com.users.api.controller;

import com.users.api.dto.LoginResponse;
import com.users.api.dto.LoginUserDTO;
import com.users.api.dto.RegisterUserDTO;
import com.users.api.dto.UserDTO;
import com.users.api.exception.BusinessException;
import com.users.api.service.IAuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final IAuthenticationService authenticationService;

    public AuthenticationController(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    @Operation(summary = "Register a new user", description = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request in register a new user", content = {
                    @Content(mediaType = "text", schema = @Schema(implementation = String.class))
            })
    })
    public ResponseEntity<Object> registerUser(@RequestBody RegisterUserDTO registerUser) {
        try {
            UserDTO createdUser = authenticationService.registerUser(registerUser, false);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (BusinessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate a user", description = "Authenticate a created user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request in authenticate a user", content = {
                    @Content(mediaType = "text", schema = @Schema(implementation = String.class))
            })
    })
    public ResponseEntity<Object> authenticateUser(@RequestBody LoginUserDTO loginUser) {
        try {
            LoginResponse registeredUser = authenticationService.authenticateUser(loginUser);
            return ResponseEntity.status(HttpStatus.OK).body(registeredUser);
        } catch (BusinessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}
