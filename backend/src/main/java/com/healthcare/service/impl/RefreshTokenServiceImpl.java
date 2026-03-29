package com.healthcare.service.impl;

import com.healthcare.dto.request.RefreshTokenRequest;
import com.healthcare.dto.response.AuthenticationResponse;
import com.healthcare.dto.response.RefreshTokenResponse;
import com.healthcare.exception.ResourceNotFoundException;
import com.healthcare.exception.TokenRefreshException;
import com.healthcare.model.RefreshToken;
import com.healthcare.model.User;
import com.healthcare.repository.RefreshTokenRepository;
import com.healthcare.repository.UserRepository;
import com.healthcare.security.JwtService;
import com.healthcare.service.RefreshTokenService;
import com.healthcare.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${application.security.jwt.refresh-token.expiration}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public RefreshToken createRefreshToken(String email) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email)));

        refreshToken.setExpiresAt(LocalDateTime.now().plusNanos(refreshTokenDurationMs * 1000000));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setIsRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getIsRevoked()) {
            throw new TokenRefreshException("Refresh token was revoked. Please login again.");
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            token.setIsRevoked(true);
            refreshTokenRepository.save(token);
            throw new TokenRefreshException("Refresh token expired. Please login again.");
        }

        return token;
    }

    @Override
    @Transactional
    public void revokeToken(RefreshToken token) {
        token.setIsRevoked(true);
        refreshTokenRepository.save(token);
    }


    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {

        RefreshToken refreshToken=findByToken(request.getRefreshToken())
                .map(this::verifyExpiration)
                .orElseThrow(()-> new TokenRefreshException("Invalid refresh Token"));

        revokeToken(refreshToken);

        String email=refreshToken.getUser().getEmail();
        String accessToken = jwtService.generateToken(userDetailsService.loadUserByUsername(email));

        RefreshToken newRefreshToken=createRefreshToken(email);

        return RefreshTokenResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(newRefreshToken.getToken())
                        .build();



    }

    @Override
    public void logout(String refreshToken) {
        RefreshToken Token=findByToken(refreshToken)
                .map(this::verifyExpiration)
                .orElseThrow(()-> new TokenRefreshException("Invalid refresh Token"));

        revokeToken(Token);
    }
}
