package dev.raj.hostsports.service;

import dev.raj.hostsports.dto.auth.AuthResponse;
import dev.raj.hostsports.dto.auth.LoginRequest;
import dev.raj.hostsports.dto.auth.RefreshTokenRequest;
import dev.raj.hostsports.dto.auth.RegisterRequest;
import dev.raj.hostsports.exception.BadRequestException;


public interface AuthService {
    AuthResponse register(RegisterRequest request) throws BadRequestException;
    AuthResponse login(LoginRequest request) throws BadRequestException;
    AuthResponse refreshToken(RefreshTokenRequest request);
}
