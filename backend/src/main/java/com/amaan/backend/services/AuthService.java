package com.amaan.backend.services;

import com.amaan.backend.dtos.request.LoginRequest;
import com.amaan.backend.dtos.request.SignupRequest;
import com.amaan.backend.dtos.response.AuthResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;

public interface AuthService {
    AuthResponse register(SignupRequest dto, HttpServletResponse response);
    AuthResponse login(LoginRequest dto, HttpServletResponse response);
    AuthResponse logout(UUID userId, HttpServletResponse response);
}
