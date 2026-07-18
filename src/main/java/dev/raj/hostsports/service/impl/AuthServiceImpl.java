package dev.raj.hostsports.service.impl;

import dev.raj.hostsports.dto.auth.AuthResponse;
import dev.raj.hostsports.dto.auth.LoginRequest;
import dev.raj.hostsports.dto.auth.RefreshTokenRequest;
import dev.raj.hostsports.dto.auth.RegisterRequest;
import dev.raj.hostsports.entity.RefreshToken;
import dev.raj.hostsports.entity.Role;
import dev.raj.hostsports.entity.User;
import dev.raj.hostsports.exception.BadRequestException;
import dev.raj.hostsports.exception.DuplicateResourceException;
import dev.raj.hostsports.repository.UserRepository;
import dev.raj.hostsports.security.JwtService;
import dev.raj.hostsports.service.AuthService;
import dev.raj.hostsports.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("An account with this email already exists");
        }

        // ADMIN accounts cannot be self-registered through the public endpoint
        if (request.getRole() == Role.ADMIN) {
            throw new BadRequestException("Cannot self-register as ADMIN");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .enabled(true)
                .build();

        User saved = userRepository.save(user);
        String token = jwtService.generateToken(saved);

        return AuthResponse.builder()
                .userId(saved.getId())
                .fullName(saved.getFullName())
                .email(saved.getEmail())
                .role(saved.getRole())
                .token(token)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .tokenType("Bearer")
                .build();
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken storedToken = refreshTokenService.findByToken(request.getRefreshToken());
        RefreshToken validToken = refreshTokenService.verifyExpiration(storedToken);
        User user = validToken.getUser();

        String newAccessToken = jwtService.generateToken(user);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .token(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .tokenType("Bearer")
                .build();
    }

    private AuthResponse buildAuthResponse(User user) {
        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .token(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .build();
    }

}