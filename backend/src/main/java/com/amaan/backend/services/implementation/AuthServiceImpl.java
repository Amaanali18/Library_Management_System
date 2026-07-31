package com.amaan.backend.services.implementation;

import com.amaan.backend.helpers.dtos.LoginRequest;
import com.amaan.backend.helpers.dtos.SignupRequest;
import com.amaan.backend.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class AuthServiceImpl implements AuthService {
    @Override
    public ResponseEntity<?> register(SignupRequest dto, HttpServletResponse response) {
        return null;
    }

    @Override
    public ResponseEntity<?> login(LoginRequest dto, HttpServletResponse response) {
        return null;
    }

    @Override
    public ResponseEntity<?> logout(UUID userId, HttpServletResponse response) {
        return null;
    }
}
