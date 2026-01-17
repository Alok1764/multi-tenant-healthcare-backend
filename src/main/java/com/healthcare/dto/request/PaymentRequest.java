package com.healthcare.dto.request;

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
public class PaymentRequest {

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;

    // In a real app, amount should be fetched from the backend, 
    // but for flexibility in this mock, valid to send (or validate against DB).
    // Let's rely on backend calculation if possible, or allow it for now.
    // Actually, safer to just send appointmentId.
    
    // Payment Method (e.g. CARD, UPI)
    private String paymentMethod;
}
