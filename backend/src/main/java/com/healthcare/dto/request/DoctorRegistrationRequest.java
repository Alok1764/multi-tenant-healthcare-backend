package com.healthcare.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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

    @NotBlank(message = "Qualification is required")
    private String qualification;

    @PositiveOrZero(message = "Experience years must be positive or zero")
    private Integer experienceYears;

    @NotNull(message = "Consultation fee is required")
    private BigDecimal consultationFee;

    @NotBlank(message = "Bio is required")
    private String bio;
    
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Hospital ID is required")
    private Long hospitalId;
}
