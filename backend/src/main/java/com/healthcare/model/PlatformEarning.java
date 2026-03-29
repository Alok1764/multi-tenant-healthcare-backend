package com.healthcare.model;

import com.healthcare.model.enums.SettlementStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "platform_earnings", indexes = {
        @Index(name = "idx_platform_earnings_hospital_id", columnList = "hospital_id"),
        @Index(name = "idx_platform_earnings_payment_id", columnList = "payment_id"),
        @Index(name = "idx_platform_earnings_status", columnList = "settlement_status"),
        @Index(name = "idx_platform_earnings_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformEarning extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false, unique = true)
    private Payment payment;

    @NotNull
    @PositiveOrZero
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @NotNull
    @PositiveOrZero
    @Column(name = "commission_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal commissionPercentage;

    @NotNull
    @PositiveOrZero
    @Column(name = "commission_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal commissionAmount;

    @NotNull
    @PositiveOrZero
    @Column(name = "hospital_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal hospitalAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "settlement_status", nullable = false, length = 50)
    private SettlementStatus settlementStatus;

    @Column(name = "settled_at")
    private LocalDateTime settledAt;

    @Column(name = "settlement_reference")
    private String settlementReference;

    // Business logic method to calculate amounts
    public void calculateAmounts() {
        if (totalAmount != null && commissionPercentage != null) {
            this.commissionAmount = totalAmount
                    .multiply(commissionPercentage)
                    .divide(BigDecimal.valueOf(100));
            this.hospitalAmount = totalAmount.subtract(commissionAmount);
        }
    }
}
