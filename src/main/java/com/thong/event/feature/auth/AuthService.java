package com.thong.event.feature.auth;

import com.thong.event.feature.auth.dto.JwtResponse;
import com.thong.event.feature.auth.dto.LoginRequest;
import com.thong.event.feature.auth.dto.RefreshTokenRequest;
import com.thong.event.feature.auth.dto.RegisterRequest;

public interface AuthService {
    JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    JwtResponse login(LoginRequest loginRequest);
    void register(RegisterRequest registerRequest);
}
