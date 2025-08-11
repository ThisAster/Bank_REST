package com.example.bankcards.service;

import com.example.bankcards.dto.JwtResponseDTO;
import com.example.bankcards.dto.LoginRequestDTO;
import com.example.bankcards.dto.RegistrationRequestDTO;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JwtAuthentication;
import com.example.bankcards.security.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    // TODO: Security Config
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    private final Map<String, String> refreshStorage = new HashMap<>();

    public JwtResponseDTO registerUser(RegistrationRequestDTO registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            log.info("User with username {} already exists", registerRequest.getUsername());
            throw new RuntimeException("Username is already in use");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            log.info("User with email {} already exists", registerRequest.getEmail());
            throw new RuntimeException("Email is already in use");
        }

        if (registerRequest.getRoles() == null || registerRequest.getRoles().isEmpty()) {
            registerRequest.setRoles(Set.of(Role.USER));
        }

        User newUser = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .roles(registerRequest.getRoles())
                .build();

        userRepository.save(newUser);

        final String accessToken = jwtProvider.generateAccessToken(newUser);
        final String refreshToken = jwtProvider.generateRefreshToken(newUser);
        refreshStorage.put(newUser.getUsername(), refreshToken);

        return new JwtResponseDTO(accessToken, refreshToken);
    }

    public JwtResponseDTO loginUser(LoginRequestDTO loginRequest) {
        final User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.info("Wrong password");
            throw new RuntimeException("Wrong password");
        }

        final String accessToken = jwtProvider.generateAccessToken(user);
        final String refreshToken = jwtProvider.generateRefreshToken(user);
        refreshStorage.put(user.getUsername(), refreshToken);

        return new JwtResponseDTO(accessToken, refreshToken);
    }

    public JwtResponseDTO getAccessToken(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String username = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(username);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponseDTO(accessToken, null);
            }
        }
        throw new RuntimeException("Wrong refresh token");
    }

    public JwtResponseDTO refresh(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String username = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(username);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getUsername(), newRefreshToken);
                return new JwtResponseDTO(accessToken, newRefreshToken);
            }
        }
        throw new RuntimeException("Not valid JWT token");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}
