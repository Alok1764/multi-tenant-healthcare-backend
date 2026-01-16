package com.healthcare.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "specializations", indexes = {
        @Index(name = "idx_specializations_name", columnList = "name")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Specialization extends BaseEntity {

    @NotBlank
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Bidirectional relationship
    @OneToMany(mappedBy = "specialization", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Doctor> doctors = new ArrayList<>();

    // Helper methods
    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
        doctor.setSpecialization(this);
    }

    public void removeDoctor(Doctor doctor) {
        doctors.remove(doctor);
        doctor.setSpecialization(null);
    }
}
