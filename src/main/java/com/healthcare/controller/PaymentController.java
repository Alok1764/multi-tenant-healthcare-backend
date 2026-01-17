package com.healthcare.controller;

import com.healthcare.dto.request.PaymentRequest;
import com.healthcare.dto.response.PaymentResponse;
import com.healthcare.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment Management", description = "Endpoints for processing payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/pay")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    @Operation(summary = "Process Appointment Payment", description = "Pay for an existing appointment")
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.processPayment(request));
    }

    @GetMapping("/appointment/{appointmentId}")
    @PreAuthorize("hasAnyAuthority('ROLE_PATIENT', 'ROLE_UADMIN')")
    @Operation(summary = "Get Payment Details", description = "Get payment info for a specific appointment")
    public ResponseEntity<PaymentResponse> getPaymentStatus(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(paymentService.getPaymentByAppointmentId(appointmentId));
    }
}
