package com.healthcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;
    private Long appointmentId;
    private String transactionId;
    private BigDecimal amount;
    private String status;
    private String paymentMethod;
    private LocalDateTime paidAt;
    private String message;
}
