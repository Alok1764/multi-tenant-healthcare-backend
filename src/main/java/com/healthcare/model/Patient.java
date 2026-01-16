package com.healthcare.model;

import com.healthcare.model.enums.BloodGroup;
import com.healthcare.model.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients",
       uniqueConstraints = {
           @UniqueConstraint(name = "unique_patient_per_hospital",
                           columnNames = {"hospital_id", "user_id"})
       },
       indexes = {
           @Index(name = "idx_patients_hospital_id", columnList = "hospital_id"),
           @Index(name = "idx_patients_user_id", columnList = "user_id"),
           @Index(name = "idx_patients_dob", columnList = "date_of_birth")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group", length = 10)
    private BloodGroup bloodGroup;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "emergency_contact_name")
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone", length = 20)
    private String emergencyContactPhone;

    @Column(name = "medical_history", columnDefinition = "TEXT")
    private String medicalHistory;

    @Column(columnDefinition = "TEXT")
    private String allergies;

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    // Bidirectional relationships
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    // Helper methods
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        appointment.setPatient(this);
    }

    public void addMedicalRecord(MedicalRecord record) {
        medicalRecords.add(record);
        record.setPatient(this);
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setPatient(this);
    }
}
