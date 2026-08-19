package com.amaan.backend.controller;

import com.amaan.backend.dtos.request.LoginRequest;
import com.amaan.backend.dtos.request.SignupRequest;
import com.amaan.backend.dtos.response.AuthResponse;
import com.amaan.backend.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public AuthResponse auth(@RequestBody @Valid SignupRequest signupRequest, HttpServletResponse  response) {
        return authService.register(signupRequest,response);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse  response) {
        return authService.login(loginRequest,response);
    }

}
