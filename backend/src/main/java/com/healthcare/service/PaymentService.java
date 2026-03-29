package com.healthcare.service;

import com.healthcare.dto.request.PaymentRequest;
import com.healthcare.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request);
    PaymentResponse getPaymentByAppointmentId(Long appointmentId);
}
