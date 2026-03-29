package com.healthcare.repository;

import com.healthcare.model.PlatformEarning;
import com.healthcare.model.enums.SettlementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlatformEarningRepository extends JpaRepository<PlatformEarning, Long> {

    // Multi-tenant queries
    List<PlatformEarning> findByHospitalId(Long hospitalId);

    Optional<PlatformEarning> findByIdAndHospitalId(Long id, Long hospitalId);

    // Payment-based
    Optional<PlatformEarning> findByPaymentId(Long paymentId);

    // Settlement status queries
    List<PlatformEarning> findBySettlementStatus(SettlementStatus status);

    List<PlatformEarning> findByHospitalIdAndSettlementStatus(Long hospitalId, SettlementStatus status);

    @Query("SELECT pe FROM PlatformEarning pe WHERE pe.settlementStatus = 'PENDING' " +
           "ORDER BY pe.createdAt ASC")
    List<PlatformEarning> findAllPendingSettlements();

    // Date range queries
    @Query("SELECT pe FROM PlatformEarning pe WHERE pe.hospital.id = :hospitalId " +
           "AND pe.createdAt BETWEEN :startDate AND :endDate " +
           "ORDER BY pe.createdAt DESC")
    List<PlatformEarning> findByHospitalAndDateRange(@Param("hospitalId") Long hospitalId,
                                                      @Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate);

    @Query("SELECT pe FROM PlatformEarning pe WHERE pe.settlementStatus = 'SETTLED' " +
           "AND pe.settledAt BETWEEN :startDate AND :endDate " +
           "ORDER BY pe.settledAt DESC")
    List<PlatformEarning> findSettledEarningsByDateRange(@Param("startDate") LocalDateTime startDate,
                                                          @Param("endDate") LocalDateTime endDate);

    // Commission calculations
    @Query("SELECT SUM(pe.commissionAmount) FROM PlatformEarning pe " +
           "WHERE pe.settlementStatus = 'SETTLED' " +
           "AND pe.settledAt BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalCommissionByDateRange(@Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(pe.commissionAmount) FROM PlatformEarning pe " +
           "WHERE pe.hospital.id = :hospitalId AND pe.settlementStatus = 'SETTLED' " +
           "AND pe.settledAt BETWEEN :startDate AND :endDate")
    BigDecimal calculateHospitalCommissionByDateRange(@Param("hospitalId") Long hospitalId,
                                                       @Param("startDate") LocalDateTime startDate,
                                                       @Param("endDate") LocalDateTime endDate);

    // Pending amounts
    @Query("SELECT SUM(pe.commissionAmount) FROM PlatformEarning pe " +
           "WHERE pe.settlementStatus = 'PENDING'")
    BigDecimal calculateTotalPendingCommission();

    @Query("SELECT SUM(pe.hospitalAmount) FROM PlatformEarning pe " +
           "WHERE pe.hospital.id = :hospitalId AND pe.settlementStatus = 'PENDING'")
    BigDecimal calculatePendingHospitalAmount(@Param("hospitalId") Long hospitalId);

    // Count queries
    long countBySettlementStatus(SettlementStatus status);

    long countByHospitalIdAndSettlementStatus(Long hospitalId, SettlementStatus status);

    // Statistics
    @Query("SELECT pe.settlementStatus, COUNT(pe), SUM(pe.commissionAmount), SUM(pe.hospitalAmount) " +
           "FROM PlatformEarning pe GROUP BY pe.settlementStatus")
    List<Object[]> getEarningsStatistics();

    @Query("SELECT pe.settlementStatus, COUNT(pe), SUM(pe.commissionAmount), SUM(pe.hospitalAmount) " +
           "FROM PlatformEarning pe WHERE pe.hospital.id = :hospitalId " +
           "GROUP BY pe.settlementStatus")
    List<Object[]> getEarningsStatisticsByHospital(@Param("hospitalId") Long hospitalId);
}
