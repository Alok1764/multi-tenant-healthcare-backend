package com.healthcare.controller;

import com.healthcare.dto.request.LoginRequest;
import com.healthcare.dto.request.RefreshTokenRequest;
import com.healthcare.dto.request.UserRegistrationRequest;
import com.healthcare.dto.response.AuthenticationResponse;
import com.healthcare.dto.response.RefreshTokenResponse;
import com.healthcare.dto.response.UserResponse;
import com.healthcare.model.RefreshToken;
import com.healthcare.security.CustomUserDetailsService;
import com.healthcare.security.JwtService;
import com.healthcare.service.RefreshTokenService;
import com.healthcare.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Valid UserRegistrationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody @Valid LoginRequest loginRequest
    ) {
      return ResponseEntity.ok(userService.loginUser(loginRequest));
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            @RequestBody @Valid RefreshTokenRequest request
    ) {
        return ResponseEntity.ok(refreshTokenService.refreshToken(request));
    }
}
