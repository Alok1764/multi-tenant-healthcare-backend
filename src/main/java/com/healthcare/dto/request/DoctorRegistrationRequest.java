package com.healthcare.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRegistrationRequest {

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @NotNull(message = "Consultation fee is required")
    private BigDecimal consultationFee;

    @NotBlank(message = "Bio is required")
    private String bio;
    
    // Linked User ID provided after initial user registration or passed if combined
    // For this flow, we'll assume the user is already registered or this is part of a combined flow.
    // Let's assume a 2-step process: Register User -> Onboard as Doctor
    @NotNull(message = "User ID is required")
    private Long userId;
}
