package com.healthcare.model;

import com.healthcare.model.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_email", columnList = "email"),
        @Index(name = "idx_users_role", columnList = "role"),
        @Index(name = "idx_users_active", columnList = "is_active")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @NotBlank
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone_number", length = 10)
    private String phoneNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private UserRole role;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "is_email_verified", nullable = false)
    private Boolean isEmailVerified;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    // Bidirectional relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @OneToOne(mappedBy = "adminUser", cascade = CascadeType.ALL)
    private Hospital managedHospital;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Doctor doctorProfile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Patient> patientProfiles = new ArrayList<>();

    // Helper methods for bidirectional relationships
    public void addRefreshToken(RefreshToken token) {
        refreshTokens.add(token);
        token.setUser(this);
    }

    public void removeRefreshToken(RefreshToken token) {
        refreshTokens.remove(token);
        token.setUser(null);
    }
}
