package com.healthcare.service;

import com.healthcare.dto.request.RefreshTokenRequest;
import com.healthcare.dto.response.AuthenticationResponse;
import com.healthcare.dto.response.RefreshTokenResponse;
import com.healthcare.model.RefreshToken;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public interface RefreshTokenService {
    void revokeToken(RefreshToken token);
    RefreshToken createRefreshToken(String email);
    Optional<RefreshToken> findByToken(String token);
    RefreshToken verifyExpiration(RefreshToken token);
    RefreshTokenResponse refreshToken(RefreshTokenRequest request);
    void logout(String refreshToken);
}
