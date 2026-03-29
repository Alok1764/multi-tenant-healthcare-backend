package com.healthcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {

    private Long id;
    private Long userId;
    private String fullName;
    private String licenseNumber;
    private String specialization;
    private BigDecimal consultationFee;
    private String bio;
    private Boolean isVerified;
}
