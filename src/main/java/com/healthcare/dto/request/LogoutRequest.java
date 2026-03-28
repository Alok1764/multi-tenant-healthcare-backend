package com.healthcare.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LogoutRequest{

    @NotBlank(message = "Refresh token is required")
    String refreshToken;
}
