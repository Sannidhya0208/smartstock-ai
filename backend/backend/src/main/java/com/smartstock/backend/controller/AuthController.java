package com.smartstock.backend.controller;

import com.smartstock.backend.dto.auth.AuthResponse;
import com.smartstock.backend.dto.auth.LoginRequest;
import com.smartstock.backend.dto.auth.RegisterRequest;
import com.smartstock.backend.service.AuthenticationService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(
            AuthenticationService authenticationService
    ) {

        this.authenticationService = authenticationService;

    }

    @PostMapping("/register")
    public AuthResponse register(

            @Valid
            @RequestBody RegisterRequest request

    ) {

        return authenticationService.register(request);

    }

    @PostMapping("/login")
    public AuthResponse login(

            @Valid
            @RequestBody LoginRequest request

    ) {

        return authenticationService.login(request);

    }

}