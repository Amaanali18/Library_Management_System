package com.amaan.backend.security.jwt;

import com.amaan.backend.entity.User;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.UUID;

public class JwtServiceImpl implements JwtService {

    private final SecretKey key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtServiceImpl(@Value("jwt.secret") SecretKey key,@Value("jwt.expiration-ms-access") long accessTokenExpiration,@Value("jwt.expiration-ms-refresh") long refreshTokenExpiration) {
        this.key = key;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    @Override
    public String generateAccessToken(User user) {
        return "";
    }

    @Override
    public String generateRefreshToken(User user) {
        return "";
    }

    @Override
    public String extractEmail(String token) {
        return "";
    }

    @Override
    public UUID extractUserId(String token) {
        return null;
    }

    @Override
    public String extractRole(String token) {
        return "";
    }

    @Override
    public boolean isTokenValid(String token) {
        return false;
    }

    @Override
    public boolean isTokenExpired(String token) {
        return false;
    }
}
