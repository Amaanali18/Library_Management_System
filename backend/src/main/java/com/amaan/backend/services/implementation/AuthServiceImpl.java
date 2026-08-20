package com.amaan.backend.services.implementation;

import com.amaan.backend.dtos.request.LoginRequest;
import com.amaan.backend.dtos.request.SignupRequest;
import com.amaan.backend.dtos.response.AuthResponse;
import com.amaan.backend.entity.RefreshToken;
import com.amaan.backend.entity.Role;
import com.amaan.backend.entity.Status;
import com.amaan.backend.entity.User;
import com.amaan.backend.repository.RefreshTokenRepository;
import com.amaan.backend.repository.RoleRepository;
import com.amaan.backend.repository.UserRepository;
import com.amaan.backend.security.jwt.JwtAuthenticationFilter;
import com.amaan.backend.security.jwt.JwtService;
import com.amaan.backend.security.userdetails.CustomUserDetails;
import com.amaan.backend.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public AuthServiceImpl(RoleRepository roleRepository,JwtService jwtService,RefreshTokenRepository refreshTokenRepository, UserRepository userRepository , PasswordEncoder passwordEncoder , AuthenticationManager authenticationManager , JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Override
    public AuthResponse register(SignupRequest dto, HttpServletResponse response) {
        AuthResponse authResponse = new AuthResponse();
        if (userRepository.existsByEmail(dto.getEmail())) {
            authResponse.setStatus(409);
            authResponse.setMessage("Email is already registered");
            authResponse.setToken(null);
            return authResponse;
        }
        Role role = roleRepository.findByName("USER").orElseThrow(() -> new IllegalStateException("USER role not found"));
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPassword(hashedPassword);
        user.setStatus(Status.ACTIVE);
        user.setRole(role);
        userRepository.save(user);
        authResponse.setStatus(201);
        authResponse.setMessage("User has been registered successfully");
        authResponse.setToken(null);
        return authResponse;
    }

    @Override
    public AuthResponse login(LoginRequest dto, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Login Failed");
        authResponse.setStatus(403);
        authResponse.setToken(null);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if(userDetails==null) return authResponse;
        User user = userDetails.getUser();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        RefreshToken rf = new RefreshToken();
        rf.setToken(refreshToken);
        rf.setUser(user);
        rf.setExpiresAt(jwtService.expiresRefresh());
        refreshTokenRepository.save(rf);
        Cookie cookie = new Cookie("token", refreshToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(
                (int) Duration.between(Instant.now(), jwtService.expiresRefresh()).getSeconds()
        );
        response.addCookie(cookie);
        authResponse.setStatus(200);
        authResponse.setMessage("Login successful");
        authResponse.setToken(accessToken);
        return authResponse;
    }

    @Override
    public AuthResponse logout(UUID userId, HttpServletResponse response) {
        return null;
    }

    @Override
    public AuthResponse refresh(HttpServletRequest request) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setStatus(403);
        authResponse.setMessage("Refresh Failed");
        authResponse.setToken(null);
        String token = jwtAuthenticationFilter.extractRefreshToken(request);
        if (token == null || !jwtService.isTokenValid(token)) {
            return authResponse;
        }
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElse(null);
        if (refreshToken==null || refreshToken.getRevoked() || refreshToken.getExpiresAt().isBefore(Instant.now())) {
            return authResponse;
        }
        User user = refreshToken.getUser();
        if(user == null) return authResponse;
        String accessToken = jwtService.generateAccessToken(user);
        authResponse.setToken(accessToken);
        authResponse.setStatus(200);
        authResponse.setMessage("Refresh successful");
        return authResponse;
    }
}
