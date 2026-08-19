package com.amaan.backend.services.implementation;

import com.amaan.backend.dtos.request.LoginRequest;
import com.amaan.backend.dtos.request.SignupRequest;
import com.amaan.backend.dtos.response.AuthResponse;
import com.amaan.backend.entity.Role;
import com.amaan.backend.entity.Status;
import com.amaan.backend.entity.User;
import com.amaan.backend.repository.RoleRepository;
import com.amaan.backend.repository.UserRepository;
import com.amaan.backend.security.jwt.JwtService;
import com.amaan.backend.security.userdetails.CustomUserDetails;
import com.amaan.backend.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(RoleRepository roleRepository,JwtService jwtService, UserRepository userRepository , PasswordEncoder passwordEncoder , AuthenticationManager authenticationManager) {
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
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
        authResponse.setStatus(200);
        authResponse.setMessage("Login successful");
        authResponse.setToken(accessToken);
        return authResponse;
    }

    @Override
    public AuthResponse logout(UUID userId, HttpServletResponse response) {
        return null;
    }
}
