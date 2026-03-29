package com.healthcare.controller;

import com.healthcare.dto.request.LoginRequest;
import com.healthcare.dto.request.LogoutRequest;
import com.healthcare.dto.request.RefreshTokenRequest;
import com.healthcare.dto.request.UserRegistrationRequest;
import com.healthcare.dto.response.AuthenticationResponse;
import com.healthcare.dto.response.RefreshTokenResponse;
import com.healthcare.service.RefreshTokenService;
import com.healthcare.service.UserService;
import com.healthcare.swagger.auth.LoginDoc;
import com.healthcare.swagger.auth.LogoutDoc;
import com.healthcare.swagger.auth.RefreshTokenDoc;
import com.healthcare.swagger.auth.RegisterDoc;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @RegisterDoc
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid UserRegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(request));
    }

    @LoginDoc
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @RefreshTokenDoc
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        return ResponseEntity.ok(refreshTokenService.refreshToken(request));
    }

    @LogoutDoc
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request) {
        refreshTokenService.logout(request.getRefreshToken());
        return ResponseEntity.noContent().build();
    }
}
