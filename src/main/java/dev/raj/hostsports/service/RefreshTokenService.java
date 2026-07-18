package dev.raj.hostsports.service;

import dev.raj.hostsports.entity.RefreshToken;
import dev.raj.hostsports.entity.User;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);
    RefreshToken verifyExpiration(RefreshToken token);
    RefreshToken findByToken(String token);
}
