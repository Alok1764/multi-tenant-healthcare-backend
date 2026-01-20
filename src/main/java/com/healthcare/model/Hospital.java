package com.healthcare.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hospitals", indexes = {
        @Index(name = "idx_hospitals_email", columnList = "email"),
        @Index(name = "idx_hospitals_active", columnList = "is_active"),
        @Index(name = "idx_hospitals_city", columnList = "city")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hospital extends BaseEntity {

    @NotBlank
    @Column(name = "hospital_name", nullable = false)
    private String hospitalName;

    @Column(name = "registration_number", unique = true, length = 100)
    private String registrationNumber;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String city;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String state;

    @Builder.Default
    @Column(nullable = false, length = 100)
    private String country = "India";

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(length = 255)
    private String website;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_user_id", unique = true)
    private User adminUser;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified;

    // Bidirectional relationships
    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HospitalSubscription> subscriptions = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Doctor> doctors = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Patient> patients = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AppointmentSlot> appointmentSlots = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PlatformEarning> platformEarnings = new ArrayList<>();

    // Helper methods
    public void addSubscription(HospitalSubscription subscription) {
        subscriptions.add(subscription);
        subscription.setHospital(this);
    }

    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
        doctor.setHospital(this);
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
        patient.setHospital(this);
    }

    public void addAppointmentSlot(AppointmentSlot slot) {
        appointmentSlots.add(slot);
        slot.setHospital(this);
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        appointment.setHospital(this);
    }

    public void addMedicalRecord(MedicalRecord record) {
        medicalRecords.add(record);
        record.setHospital(this);
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setHospital(this);
    }

    public void addPlatformEarning(PlatformEarning earning) {
        platformEarnings.add(earning);
        earning.setHospital(this);
    }
}
