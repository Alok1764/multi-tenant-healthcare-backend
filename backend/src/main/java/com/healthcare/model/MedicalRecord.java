package com.healthcare.model;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "medical_records",
       uniqueConstraints = {
           @UniqueConstraint(name = "unique_record_per_appointment",
                           columnNames = {"appointment_id"})
       },
       indexes = {
           @Index(name = "idx_medical_records_hospital_id", columnList = "hospital_id"),
           @Index(name = "idx_medical_records_appointment_id", columnList = "appointment_id"),
           @Index(name = "idx_medical_records_patient_id", columnList = "patient_id"),
           @Index(name = "idx_medical_records_doctor_id", columnList = "doctor_id"),
           @Index(name = "idx_medical_records_created_at", columnList = "created_at")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecord extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String diagnosis;

    @Column(columnDefinition = "TEXT")
    private String symptoms;

    @Column(columnDefinition = "TEXT")
    private String prescriptions;

    @Column(name = "lab_tests_recommended", columnDefinition = "TEXT")
    private String labTestsRecommended;

    @Column(name = "follow_up_required", nullable = false)
    private Boolean followUpRequired = false;

    @Column(name = "follow_up_date")
    private LocalDate followUpDate;

    @Type(JsonType.class)
    @Column(name = "vital_signs", columnDefinition = "json")
    private Map<String, Object> vitalSigns;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> attachments;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
