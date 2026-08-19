package com.amaan.backend.services;

import com.amaan.backend.dtos.request.LoginRequest;
import com.amaan.backend.dtos.request.SignupRequest;
import com.amaan.backend.dtos.response.AuthResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import java.util.UUID;

public interface AuthService {
    AuthResponse register(SignupRequest dto, HttpServletResponse response);
    ResponseEntity<?> login(LoginRequest dto, HttpServletResponse response);
    ResponseEntity<?> logout(UUID userId, HttpServletResponse response);
}
