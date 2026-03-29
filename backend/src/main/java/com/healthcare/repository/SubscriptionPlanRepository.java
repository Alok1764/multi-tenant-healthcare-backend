package com.healthcare.repository;

import com.healthcare.model.SubscriptionPlan;
import com.healthcare.model.enums.SubscriptionPlanName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {

    Optional<SubscriptionPlan> findByPlanName(SubscriptionPlanName planName);

    List<SubscriptionPlan> findByIsActive(Boolean isActive);

    @Query("SELECT sp FROM SubscriptionPlan sp WHERE sp.isActive = true ORDER BY sp.monthlyPrice ASC")
    List<SubscriptionPlan> findAllActivePlansOrderedByPrice();

    boolean existsByPlanName(SubscriptionPlanName planName);
}
