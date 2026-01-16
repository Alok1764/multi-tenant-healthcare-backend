package com.healthcare.repository;

import com.healthcare.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    // Multi-tenant queries
    List<MedicalRecord> findByHospitalId(Long hospitalId);

    Optional<MedicalRecord> findByIdAndHospitalId(Long id, Long hospitalId);

    // Appointment-based
    Optional<MedicalRecord> findByAppointmentId(Long appointmentId);

    // Patient queries
    List<MedicalRecord> findByPatientId(Long patientId);

    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.patient.id = :patientId " +
           "AND mr.hospital.id = :hospitalId ORDER BY mr.createdAt DESC")
    List<MedicalRecord> findByPatientAndHospital(@Param("patientId") Long patientId,
                                                  @Param("hospitalId") Long hospitalId);

    // Doctor queries
    List<MedicalRecord> findByDoctorId(Long doctorId);

    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.doctor.id = :doctorId " +
           "AND mr.createdAt BETWEEN :startDate AND :endDate " +
           "ORDER BY mr.createdAt DESC")
    List<MedicalRecord> findByDoctorAndDateRange(@Param("doctorId") Long doctorId,
                                                  @Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate);

    // Follow-up queries
    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.hospital.id = :hospitalId " +
           "AND mr.followUpRequired = true AND mr.followUpDate >= :today " +
           "ORDER BY mr.followUpDate ASC")
    List<MedicalRecord> findPendingFollowUpsByHospital(@Param("hospitalId") Long hospitalId,
                                                        @Param("today") LocalDate today);

    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.patient.id = :patientId " +
           "AND mr.followUpRequired = true AND mr.followUpDate >= :today " +
           "ORDER BY mr.followUpDate ASC")
    List<MedicalRecord> findPendingFollowUpsByPatient(@Param("patientId") Long patientId,
                                                       @Param("today") LocalDate today);

    // Recent records
    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.patient.id = :patientId " +
           "ORDER BY mr.createdAt DESC")
    List<MedicalRecord> findRecentRecordsByPatient(@Param("patientId") Long patientId);

    // Search in diagnosis
    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.hospital.id = :hospitalId " +
           "AND LOWER(mr.diagnosis) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<MedicalRecord> searchByDiagnosis(@Param("hospitalId") Long hospitalId,
                                          @Param("searchTerm") String searchTerm);

    // Count queries
    long countByHospitalId(Long hospitalId);

    long countByPatientId(Long patientId);

    long countByDoctorId(Long doctorId);

    @Query("SELECT COUNT(mr) FROM MedicalRecord mr WHERE mr.hospital.id = :hospitalId " +
           "AND mr.followUpRequired = true AND mr.followUpDate >= :today")
    long countPendingFollowUps(@Param("hospitalId") Long hospitalId,
                               @Param("today") LocalDate today);
}
