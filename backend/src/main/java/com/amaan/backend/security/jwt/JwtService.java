package com.amaan.backend.security.jwt;

import com.amaan.backend.entity.User;
import java.util.UUID;

public interface JwtService {

    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    String extractEmail(String token);
    UUID extractUserId(String token);
    String extractRole(String token);
    boolean isTokenValid(String token);
    boolean isTokenExpired(String token);
}