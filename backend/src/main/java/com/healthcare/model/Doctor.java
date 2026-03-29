package com.healthcare.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "doctors", 
       uniqueConstraints = {
           @UniqueConstraint(name = "unique_doctor_license_per_hospital", 
                           columnNames = {"hospital_id", "license_number"})
       },
       indexes = {
           @Index(name = "idx_doctors_hospital_id", columnList = "hospital_id"),
           @Index(name = "idx_doctors_user_id", columnList = "user_id"),
           @Index(name = "idx_doctors_specialization_id", columnList = "specialization_id"),
           @Index(name = "idx_doctors_available", columnList = "is_available")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization_id", nullable = false)
    private Specialization specialization;

    @NotBlank
    @Column(name = "license_number", nullable = false, length = 100)
    private String licenseNumber;

    @NotBlank
    @Column(nullable = false)
    private String qualification;

    @PositiveOrZero
    @Column(name = "experience_years")
    private Integer experienceYears;

    @NotNull
    @Column(name = "consultation_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal consultationFee;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    //for now let it be true
    @Builder.Default
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified;

    // Bidirectional relationships
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AppointmentSlot> appointmentSlots = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    // Helper methods
    public void addAppointmentSlot(AppointmentSlot slot) {
        appointmentSlots.add(slot);
        slot.setDoctor(this);
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        appointment.setDoctor(this);
    }

    public void addMedicalRecord(MedicalRecord record) {
        medicalRecords.add(record);
        record.setDoctor(this);
    }
}
