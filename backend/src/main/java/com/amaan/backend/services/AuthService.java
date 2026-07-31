package com.amaan.backend.services;

import com.amaan.backend.helpers.dtos.LoginRequest;
import com.amaan.backend.helpers.dtos.SignupRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import java.util.UUID;

public interface AuthService {
    ResponseEntity<?> register(SignupRequest dto, HttpServletResponse response);
    ResponseEntity<?> login(LoginRequest dto, HttpServletResponse response);
    ResponseEntity<?> logout(UUID userId, HttpServletResponse response);
}
