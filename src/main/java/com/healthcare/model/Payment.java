package com.healthcare.model;

import com.healthcare.model.enums.PaymentMethod;
import com.healthcare.model.enums.PaymentType;
import com.healthcare.model.enums.TransactionStatus;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "payments", indexes = {
        @Index(name = "idx_payments_hospital_id", columnList = "hospital_id"),
        @Index(name = "idx_payments_appointment_id", columnList = "appointment_id"),
        @Index(name = "idx_payments_patient_id", columnList = "patient_id"),
        @Index(name = "idx_payments_status", columnList = "payment_status"),
        @Index(name = "idx_payments_type", columnList = "payment_type"),
        @Index(name = "idx_payments_transaction_id", columnList = "transaction_id"),
        @Index(name = "idx_payments_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false, length = 50)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 50)
    private PaymentMethod paymentMethod;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 50)
    private TransactionStatus paymentStatus;

    @Column(name = "transaction_id", unique = true)
    private String transactionId;

    @Column(name = "payment_gateway", length = 100)
    private String paymentGateway;

    @Type(JsonType.class)
    @Column(name = "payment_gateway_response", columnDefinition = "json")
    private Map<String, Object> paymentGatewayResponse;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "refunded_at")
    private LocalDateTime refundedAt;

    @Column(name = "refund_reason", columnDefinition = "TEXT")
    private String refundReason;

    // Bidirectional relationship
    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private PlatformEarning platformEarning;

    // Helper method
    public void setPlatformEarning(PlatformEarning earning) {
        this.platformEarning = earning;
        if (earning != null) {
            earning.setPayment(this);
        }
    }
}
