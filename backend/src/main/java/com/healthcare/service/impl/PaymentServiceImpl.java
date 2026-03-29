package com.healthcare.service.impl;

import com.healthcare.dto.request.PaymentRequest;
import com.healthcare.dto.response.PaymentResponse;
import com.healthcare.exception.ResourceConflictException;
import com.healthcare.exception.ResourceNotFoundException;
import com.healthcare.model.Appointment;
import com.healthcare.model.Payment;
import com.healthcare.model.enums.PaymentMethod;
import com.healthcare.model.enums.PaymentType;
import com.healthcare.model.enums.TransactionStatus;
import com.healthcare.repository.AppointmentRepository;
import com.healthcare.repository.PaymentRepository;
import com.healthcare.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;

    private static final BigDecimal CONSULTATION_FEE = new BigDecimal("500.00"); // Mock fixed fee

    @Override
    @CacheEvict(value = "payments", allEntries = true)
    public PaymentResponse processPayment(PaymentRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (!paymentRepository.findByAppointmentId(request.getAppointmentId()).isEmpty()) {
             // In real world, check if status is SUCCESS. If FAILED, allow retry.
             // For simplicity, blocking multiple attempts if any exist for now, or check status
             boolean alreadyPaid = paymentRepository.findByAppointmentId(request.getAppointmentId()).stream()
                     .anyMatch(p -> p.getPaymentStatus() == TransactionStatus.COMPLETED);
             if (alreadyPaid) {
                 throw new ResourceConflictException("Appointment is already paid");
             }
        }

        // Mock Gateway Interaction
        String transactionId = UUID.randomUUID().toString();
        
        Payment payment = Payment.builder()
                .hospital(appointment.getAppointmentSlot().getHospital())
                .appointment(appointment)
                .patient(appointment.getPatient())
                .amount(CONSULTATION_FEE)
                .paymentType(PaymentType.CONSULTATION)
                .paymentMethod(PaymentMethod.CARD) // Default or parse from request
                .paymentStatus(TransactionStatus.COMPLETED) // Mock Success
                .transactionId(transactionId)
                .paidAt(LocalDateTime.now())
                .paymentGateway("MOCK_GATEWAY")
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        return mapToResponse(savedPayment);
    }

    @Override
    @Cacheable(value = "payments", key = "#appointmentId")
    public PaymentResponse getPaymentByAppointmentId(Long appointmentId) {
        Payment payment = paymentRepository.findByAppointmentId(appointmentId).stream()
                .filter(p -> p.getPaymentStatus() == TransactionStatus.COMPLETED)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No completed payment found for this appointment"));
                
        return mapToResponse(payment);
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .appointmentId(payment.getAppointment().getId())
                .transactionId(payment.getTransactionId())
                .amount(payment.getAmount())
                .status(payment.getPaymentStatus().name())
                .paymentMethod(payment.getPaymentMethod().name())
                .paidAt(payment.getPaidAt())
                .message("Payment Processed Successfully")
                .build();
    }
}
