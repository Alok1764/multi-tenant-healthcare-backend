package com.healthcare.controller;

import com.healthcare.dto.request.PaymentRequest;
import com.healthcare.dto.response.PaymentResponse;
import com.healthcare.service.PaymentService;
import com.healthcare.swagger.payment.GetPaymentStatusDoc;
import com.healthcare.swagger.payment.ProcessPaymentDoc;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment Management")
public class PaymentController {

    private final PaymentService paymentService;

    @ProcessPaymentDoc
    @PostMapping("/pay")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.processPayment(request));
    }

    @GetPaymentStatusDoc
    @GetMapping("/appointment/{appointmentId}")
    @PreAuthorize("hasAnyAuthority('ROLE_PATIENT', 'ROLE_HOSPITAL_ADMIN')")
    public ResponseEntity<PaymentResponse> getPaymentStatus(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(paymentService.getPaymentByAppointmentId(appointmentId));
    }
}
