package com.healthcare.repository;

import com.healthcare.model.Hospital;
import com.healthcare.model.HospitalSubscription;
import com.healthcare.model.SubscriptionPlan;
import com.healthcare.model.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalSubscriptionRepository extends JpaRepository<HospitalSubscription, Long> {

    List<HospitalSubscription> findByHospital(Hospital hospital);

    List<HospitalSubscription> findByHospitalId(Long hospitalId);

    List<HospitalSubscription> findByStatus(SubscriptionStatus status);

    List<HospitalSubscription> findBySubscriptionPlan(SubscriptionPlan plan);

    // Get active subscription for a hospital
    @Query("SELECT hs FROM HospitalSubscription hs WHERE hs.hospital.id = :hospitalId " +
           "AND hs.status = 'ACTIVE' AND hs.endDate >= :today ORDER BY hs.endDate DESC")
    Optional<HospitalSubscription> findActiveSubscriptionByHospitalId(
            @Param("hospitalId") Long hospitalId,
            @Param("today") LocalDate today);

    // Find expiring subscriptions
    @Query("SELECT hs FROM HospitalSubscription hs WHERE hs.status = 'ACTIVE' " +
           "AND hs.endDate BETWEEN :startDate AND :endDate")
    List<HospitalSubscription> findExpiringSubscriptions(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // Find expired subscriptions that need status update
    @Query("SELECT hs FROM HospitalSubscription hs WHERE hs.status = 'ACTIVE' " +
           "AND hs.endDate < :today")
    List<HospitalSubscription> findExpiredActiveSubscriptions(@Param("today") LocalDate today);

    // Count by status
    long countByStatus(SubscriptionStatus status);

    @Query("SELECT COUNT(hs) FROM HospitalSubscription hs WHERE hs.status = 'ACTIVE' " +
           "AND hs.endDate >= :today")
    long countCurrentlyActiveSubscriptions(@Param("today") LocalDate today);
}
