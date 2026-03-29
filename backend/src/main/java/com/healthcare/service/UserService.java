package com.healthcare.service;

import com.healthcare.dto.request.LoginRequest;
import com.healthcare.dto.request.UserRegistrationRequest;
import com.healthcare.dto.response.AuthenticationResponse;
import com.healthcare.dto.response.UserResponse;

import java.util.Optional;

public interface UserService {
    AuthenticationResponse registerUser(UserRegistrationRequest request);
    AuthenticationResponse loginUser(LoginRequest loginRequest);
    Optional<UserResponse> findByEmail(String email);
    boolean existsByEmail(String email);
}
