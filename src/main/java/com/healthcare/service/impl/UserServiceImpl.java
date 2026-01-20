package com.healthcare.service.impl;

import com.healthcare.dto.request.LoginRequest;
import com.healthcare.dto.request.UserRegistrationRequest;
import com.healthcare.dto.response.AuthenticationResponse;
import com.healthcare.dto.response.UserResponse;
import com.healthcare.exception.ResourceConflictException;
import com.healthcare.exception.ResourceNotFoundException;
import com.healthcare.model.RefreshToken;
import com.healthcare.model.User;
import com.healthcare.repository.UserRepository;
import com.healthcare.security.CustomUserDetailsService;
import com.healthcare.security.JwtService;
import com.healthcare.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final RefreshTokenServiceImpl refreshTokenService;

    @Override
    public AuthenticationResponse registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceConflictException("User with email " + request.getEmail() + " already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .isActive(true) // Active by default for now
                .isEmailVerified(false)
                .build();

        User savedUser = userRepository.save(user);

        UserResponse userResponse=mapToResponse(savedUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername(userResponse.getEmail());

        String jwtToken = jwtService.generateToken(userDetails.getUsername());
        RefreshToken refreshToken=refreshTokenService.createRefreshToken(userResponse.getEmail());

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .user(userResponse)
                .build();

    }

    @Override
    public AuthenticationResponse loginUser(LoginRequest loginRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        String jwtToken = jwtService.generateToken(userDetails.getUsername());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginRequest.getEmail());

        UserResponse userResponse = findByEmail(loginRequest.getEmail())
                .orElseThrow();

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .user(userResponse)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponse> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }




    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .isEmailVerified(user.getIsEmailVerified())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
