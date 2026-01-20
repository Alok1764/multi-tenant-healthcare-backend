package com.healthcare.model;

import com.healthcare.model.enums.SubscriptionPlanName;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "subscription_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlan extends BaseEntity {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "plan_name", nullable = false, unique = true, length = 50)
    private SubscriptionPlanName planName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Positive
    @Column(name = "monthly_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyPrice;

    @NotNull
    @Positive
    @Column(name = "max_doctors", nullable = false)
    private Integer maxDoctors;

    @Positive
    @Column(name = "max_patients")
    private Integer maxPatients;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> features;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Bidirectional relationship
    @OneToMany(mappedBy = "subscriptionPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HospitalSubscription> hospitalSubscriptions = new ArrayList<>();

    // Helper methods
    public void addHospitalSubscription(HospitalSubscription subscription) {
        hospitalSubscriptions.add(subscription);
        subscription.setSubscriptionPlan(this);
    }

    public void removeHospitalSubscription(HospitalSubscription subscription) {
        hospitalSubscriptions.remove(subscription);
        subscription.setSubscriptionPlan(null);
    }
}
