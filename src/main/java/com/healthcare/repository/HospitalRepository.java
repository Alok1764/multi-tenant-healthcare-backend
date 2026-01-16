package com.healthcare.repository;

import com.healthcare.model.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    Optional<Hospital> findByEmail(String email);

    Optional<Hospital> findByRegistrationNumber(String registrationNumber);

    Optional<Hospital> findByAdminUserId(Long adminUserId);

    List<Hospital> findByIsActive(Boolean isActive);
    
    Page<Hospital> findByIsActive(Boolean isActive, Pageable pageable);

    List<Hospital> findByIsVerified(Boolean isVerified);
    
    Page<Hospital> findByIsVerified(Boolean isVerified, Pageable pageable);

    List<Hospital> findByCity(String city);
    
    Page<Hospital> findByCity(String city, Pageable pageable);

    List<Hospital> findByState(String state);
    
    Page<Hospital> findByState(String state, Pageable pageable);

    // Combined queries
    List<Hospital> findByIsActiveAndIsVerified(Boolean isActive, Boolean isVerified);
    
    Page<Hospital> findByIsActiveAndIsVerified(Boolean isActive, Boolean isVerified, Pageable pageable);

    List<Hospital> findByCityAndIsActive(String city, Boolean isActive);
    
    Page<Hospital> findByCityAndIsActive(String city, Boolean isActive, Pageable pageable);

    // Search queries
    @Query("SELECT h FROM Hospital h WHERE " +
           "(LOWER(h.hospitalName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(h.city) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(h.state) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND h.isActive = true AND h.isVerified = true")
    List<Hospital> searchActiveVerifiedHospitals(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT h FROM Hospital h WHERE " +
           "(LOWER(h.hospitalName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(h.city) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(h.state) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND h.isActive = true AND h.isVerified = true")
    Page<Hospital> searchActiveVerifiedHospitals(@Param("searchTerm") String searchTerm, Pageable pageable);

    // Count queries
    long countByIsActive(Boolean isActive);

    long countByIsVerified(Boolean isVerified);

    @Query("SELECT COUNT(h) FROM Hospital h WHERE h.isActive = true AND h.isVerified = true")
    long countActiveVerifiedHospitals();

    // Existence checks
    boolean existsByEmail(String email);

    boolean existsByRegistrationNumber(String registrationNumber);
}
