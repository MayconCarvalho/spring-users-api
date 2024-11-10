package com.users.api.controller;

import com.users.api.dto.RegisterUserDTO;
import com.users.api.dto.UserDTO;
import com.users.api.exception.BusinessException;
import com.users.api.service.IAuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final IAuthenticationService authenticationService;

    public AdminController(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    @Operation(summary = "Register a new admin user", description = "Register a new admin user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Admin User registered successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request in register a new admin user", content = {
                    @Content(mediaType = "text", schema = @Schema(implementation = String.class))
            })
    })
    public ResponseEntity<Object> registerUser(@RequestBody RegisterUserDTO registerUser) {
        try {
            UserDTO createdUser = authenticationService.registerUser(registerUser, true);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (BusinessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Get all users registered in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully", content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request in retrieve users")
    })
    public ResponseEntity<Object> getAllUsers() {
        List<UserDTO> allUsers = authenticationService.getAllUsers();
        if (allUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(allUsers);
    }
}
