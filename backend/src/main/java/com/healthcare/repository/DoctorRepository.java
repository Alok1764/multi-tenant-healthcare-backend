package com.healthcare.repository;

import com.healthcare.model.Doctor;
import com.healthcare.model.Hospital;
import com.healthcare.model.Specialization;
import com.healthcare.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // Multi-tenant queries
    List<Doctor> findByHospitalId(Long hospitalId);
    
    Page<Doctor> findByHospitalId(Long hospitalId, Pageable pageable);

    Optional<Doctor> findByIdAndHospitalId(Long id, Long hospitalId);

    Optional<Doctor> findByUserId(Long userId);

    Optional<Doctor> findByUserIdAndHospitalId(Long userId, Long hospitalId);

    // Specialization queries
    List<Doctor> findBySpecializationId(Long specializationId);

    List<Doctor> findByHospitalIdAndSpecializationId(Long hospitalId, Long specializationId);

    // Availability queries
    List<Doctor> findByHospitalIdAndIsAvailable(Long hospitalId, Boolean isAvailable);

    List<Doctor> findByHospitalIdAndIsVerified(Long hospitalId, Boolean isVerified);

    @Query("SELECT d FROM Doctor d WHERE d.hospital.id = :hospitalId " +
           "AND d.isAvailable = true AND d.isVerified = true")
    List<Doctor> findAvailableVerifiedDoctorsByHospital(@Param("hospitalId") Long hospitalId);
    
    @Query("SELECT d FROM Doctor d WHERE d.hospital.id = :hospitalId " +
           "AND d.isAvailable = true AND d.isVerified = true")
    Page<Doctor> findAvailableVerifiedDoctorsByHospital(@Param("hospitalId") Long hospitalId, Pageable pageable);

    // Search queries
    @Query("SELECT d FROM Doctor d JOIN d.user u WHERE d.hospital.id = :hospitalId " +
           "AND (LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(d.qualification) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Doctor> searchDoctorsByHospital(@Param("hospitalId") Long hospitalId, 
                                         @Param("searchTerm") String searchTerm);

    // Fee range queries
    @Query("SELECT d FROM Doctor d WHERE d.hospital.id = :hospitalId " +
           "AND d.consultationFee BETWEEN :minFee AND :maxFee " +
           "AND d.isAvailable = true")
    List<Doctor> findByHospitalAndFeeRange(@Param("hospitalId") Long hospitalId,
                                           @Param("minFee") BigDecimal minFee,
                                           @Param("maxFee") BigDecimal maxFee);

    // Experience queries
    @Query("SELECT d FROM Doctor d WHERE d.hospital.id = :hospitalId " +
           "AND d.experienceYears >= :minYears AND d.isAvailable = true")
    List<Doctor> findExperiencedDoctorsByHospital(@Param("hospitalId") Long hospitalId,
                                                   @Param("minYears") Integer minYears);

    // Count queries
    long countByHospitalId(Long hospitalId);

    long countByHospitalIdAndIsAvailable(Long hospitalId, Boolean isAvailable);

    @Query("SELECT COUNT(d) FROM Doctor d WHERE d.hospital.id = :hospitalId " +
           "AND d.specialization.id = :specializationId AND d.isAvailable = true")
    long countAvailableDoctorsBySpecialization(@Param("hospitalId") Long hospitalId,
                                               @Param("specializationId") Long specializationId);

    // License check
    boolean existsByHospitalIdAndLicenseNumber(Long hospitalId, String licenseNumber);
}
