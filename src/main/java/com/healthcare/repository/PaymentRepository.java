package com.healthcare.repository;

import com.healthcare.model.Payment;
import com.healthcare.model.enums.PaymentType;
import com.healthcare.model.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Multi-tenant queries
    List<Payment> findByHospitalId(Long hospitalId);

    Optional<Payment> findByIdAndHospitalId(Long id, Long hospitalId);

    // Transaction lookup
    Optional<Payment> findByTransactionId(String transactionId);

    // Patient queries
    List<Payment> findByPatientId(Long patientId);

    @Query("SELECT p FROM Payment p WHERE p.patient.id = :patientId " +
           "AND p.hospital.id = :hospitalId ORDER BY p.createdAt DESC")
    List<Payment> findByPatientAndHospital(@Param("patientId") Long patientId,
                                           @Param("hospitalId") Long hospitalId);

    // Appointment-based
    List<Payment> findByAppointmentId(Long appointmentId);

    // Status queries
    List<Payment> findByPaymentStatus(TransactionStatus status);

    List<Payment> findByHospitalIdAndPaymentStatus(Long hospitalId, TransactionStatus status);

    @Query("SELECT p FROM Payment p WHERE p.hospital.id = :hospitalId " +
           "AND p.paymentStatus = 'PENDING' " +
           "AND p.createdAt < :cutoffTime")
    List<Payment> findPendingPaymentsOlderThan(@Param("hospitalId") Long hospitalId,
                                               @Param("cutoffTime") LocalDateTime cutoffTime);

    // Type queries
    List<Payment> findByPaymentType(PaymentType type);

    List<Payment> findByHospitalIdAndPaymentType(Long hospitalId, PaymentType type);

    // Date range queries
    @Query("SELECT p FROM Payment p WHERE p.hospital.id = :hospitalId " +
           "AND p.paidAt BETWEEN :startDate AND :endDate " +
           "AND p.paymentStatus = 'COMPLETED' " +
           "ORDER BY p.paidAt DESC")
    List<Payment> findCompletedPaymentsByDateRange(@Param("hospitalId") Long hospitalId,
                                                    @Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate);

    // Revenue calculations
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.hospital.id = :hospitalId " +
           "AND p.paymentStatus = 'COMPLETED' " +
           "AND p.paidAt BETWEEN :startDate AND :endDate")
    BigDecimal calculateRevenueByDateRange(@Param("hospitalId") Long hospitalId,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.hospital.id = :hospitalId " +
           "AND p.paymentStatus = 'COMPLETED' AND p.paymentType = :type " +
           "AND p.paidAt BETWEEN :startDate AND :endDate")
    BigDecimal calculateRevenueByTypeAndDateRange(@Param("hospitalId") Long hospitalId,
                                                   @Param("type") PaymentType type,
                                                   @Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);

    // Count queries
    long countByHospitalIdAndPaymentStatus(Long hospitalId, TransactionStatus status);

    long countByPatientIdAndPaymentStatus(Long patientId, TransactionStatus status);

    // Statistics
    @Query("SELECT p.paymentStatus, COUNT(p), SUM(p.amount) FROM Payment p " +
           "WHERE p.hospital.id = :hospitalId GROUP BY p.paymentStatus")
    List<Object[]> getPaymentStatsByHospital(@Param("hospitalId") Long hospitalId);

    @Query("SELECT p.paymentType, COUNT(p), SUM(p.amount) FROM Payment p " +
           "WHERE p.hospital.id = :hospitalId AND p.paymentStatus = 'COMPLETED' " +
           "GROUP BY p.paymentType")
    List<Object[]> getRevenueByTypeForHospital(@Param("hospitalId") Long hospitalId);
}
