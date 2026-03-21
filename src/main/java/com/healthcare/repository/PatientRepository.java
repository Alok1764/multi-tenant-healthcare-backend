package com.healthcare.repository;

import com.healthcare.model.Patient;
import com.healthcare.model.enums.BloodGroup;
import com.healthcare.model.enums.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Multi-tenant queries
//    List<Patient> findByHospitalId(Long hospitalId);
//
//    Page<Patient> findByHospitalId(Long hospitalId, Pageable pageable);
//
//    Optional<Patient> findByIdAndHospitalId(Long id, Long hospitalId);
//
//    Optional<Patient> findByUserIdAndHospitalId(Long userId, Long hospitalId);


    Optional<Patient> findByUserId(Long userId);

//    // Demographic queries
//    List<Patient> findByHospitalIdAndGender(Long hospitalId, Gender gender);
//
//    List<Patient> findByHospitalIdAndBloodGroup(Long hospitalId, BloodGroup bloodGroup);
//
//    // Search queries
//    @Query("SELECT p FROM Patient p JOIN p.user u WHERE p.hospital.id = :hospitalId " +
//           "AND (LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
//           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
//           "OR LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
//    List<Patient> searchPatientsByHospital(@Param("hospitalId") Long hospitalId,
//                                           @Param("searchTerm") String searchTerm);
//
//    // Age-based queries
//    @Query("SELECT p FROM Patient p WHERE p.hospital.id = :hospitalId " +
//           "AND p.dateOfBirth BETWEEN :startDate AND :endDate")
//    List<Patient> findByHospitalAndAgeRange(@Param("hospitalId") Long hospitalId,
//                                            @Param("startDate") LocalDate startDate,
//                                            @Param("endDate") LocalDate endDate);
//
//    // Patients with allergies
//    @Query("SELECT p FROM Patient p WHERE p.hospital.id = :hospitalId " +
//           "AND p.allergies IS NOT NULL AND p.allergies != ''")
//    List<Patient> findPatientsWithAllergiesByHospital(@Param("hospitalId") Long hospitalId);
//
//    // Count queries
//    long countByHospitalId(Long hospitalId);
//
//    long countByHospitalIdAndGender(Long hospitalId, Gender gender);
//
//    long countByHospitalIdAndBloodGroup(Long hospitalId, BloodGroup bloodGroup);
//
//    // Existence check
//    boolean existsByHospitalIdAndUserId(Long hospitalId, Long userId);
}
