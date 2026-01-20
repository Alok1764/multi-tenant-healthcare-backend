package com.healthcare.model;

import com.healthcare.model.enums.AppointmentStatus;
import com.healthcare.model.enums.ConsultationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointments", indexes = {
        @Index(name = "idx_appointments_hospital_id", columnList = "hospital_id"),
        @Index(name = "idx_appointments_slot_id", columnList = "appointment_slot_id"),
        @Index(name = "idx_appointments_doctor_id", columnList = "doctor_id"),
        @Index(name = "idx_appointments_patient_id", columnList = "patient_id"),
        @Index(name = "idx_appointments_date", columnList = "appointment_date"),
        @Index(name = "idx_appointments_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_slot_id", nullable = false)
    private AppointmentSlot appointmentSlot;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull
    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @NotNull
    @Column(name = "appointment_time", nullable = false)
    private LocalTime appointmentTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AppointmentStatus status;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @Column(name = "patient_notes", columnDefinition = "TEXT")
    private String patientNotes;

    @Column(name = "doctor_notes", columnDefinition = "TEXT")
    private String doctorNotes;

    @Enumerated(EnumType.STRING)
    @Column(name = "consultation_type", length = 50)
    private ConsultationType consultationType;

    // Bidirectional relationships
    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private MedicalRecord medicalRecord;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    // Helper methods
    public void setMedicalRecord(MedicalRecord record) {
        this.medicalRecord = record;
        if (record != null) {
            record.setAppointment(this);
        }
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setAppointment(this);
    }
}
