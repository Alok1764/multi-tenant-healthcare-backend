package com.healthcare.repository;

import com.healthcare.model.User;
import com.healthcare.model.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Basic queries
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(UserRole role);
    
    Page<User> findByRole(UserRole role, Pageable pageable);

    List<User> findByIsActive(Boolean isActive);
    
    Page<User> findByIsActive(Boolean isActive, Pageable pageable);

    // Combined queries
    List<User> findByRoleAndIsActive(UserRole role, Boolean isActive);
    
    Page<User> findByRoleAndIsActive(UserRole role, Boolean isActive, Pageable pageable);

    Optional<User> findByEmailAndIsActive(String email, Boolean isActive);

    // Custom queries
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.isEmailVerified = true AND u.isActive = true")
    List<User> findActiveVerifiedUsersByRole(@Param("role") UserRole role);
    
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.isEmailVerified = true AND u.isActive = true")
    Page<User> findActiveVerifiedUsersByRole(@Param("role") UserRole role, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.lastLoginAt > :since")
    List<User> findRecentlyActiveUsers(@Param("since") LocalDateTime since);
    
    @Query("SELECT u FROM User u WHERE u.lastLoginAt > :since")
    Page<User> findRecentlyActiveUsers(@Param("since") LocalDateTime since, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.isEmailVerified = false")
    List<User> findUnverifiedUsers();
    
    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.isEmailVerified = false")
    Page<User> findUnverifiedUsers(Pageable pageable);

    // Count queries
    long countByRole(UserRole role);

    long countByIsActive(Boolean isActive);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.isActive = true")
    long countActiveUsersByRole(@Param("role") UserRole role);

    // Search queries
    @Query("SELECT u FROM User u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> searchUsers(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> searchUsers(@Param("searchTerm") String searchTerm, Pageable pageable);
}
