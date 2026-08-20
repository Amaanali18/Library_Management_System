package com.amaan.backend.services;

import com.amaan.backend.dtos.request.LoginRequest;
import com.amaan.backend.dtos.request.SignupRequest;
import com.amaan.backend.dtos.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthResponse register(SignupRequest dto, HttpServletResponse response);
    AuthResponse login(LoginRequest dto, HttpServletResponse response);
    AuthResponse logout(HttpServletRequest request,HttpServletResponse response);
    AuthResponse refresh(HttpServletRequest request);
}
