package com.amaan.backend.security.jwt;

import com.amaan.backend.entity.User;
import io.jsonwebtoken.Claims;

import java.time.Instant;
import java.util.UUID;

public interface JwtService {
    Claims extractClaims(String token);
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    String extractEmail(String token);
    UUID extractUserId(String token);
    String extractRole(String token);
    boolean isTokenValid(String token);
    boolean isTokenExpired(String token);
    Instant expiresRefresh();
    Instant expiresAccess();
}