package com.healthcare.service;

import com.healthcare.dto.request.UserRegistrationRequest;
import com.healthcare.dto.response.UserResponse;

import java.util.Optional;

public interface UserService {
    UserResponse registerUser(UserRegistrationRequest request);
    Optional<UserResponse> findByEmail(String email);
    boolean existsByEmail(String email);
}
