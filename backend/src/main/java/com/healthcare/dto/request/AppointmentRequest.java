package com.healthcare.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {

    @NotNull(message = "Slot ID is required")
    private Long slotId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;
    
    // Optional: Reason for visit
    private String reason;
}
