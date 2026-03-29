package com.healthcare.model;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
        name = "hospital_patients",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_patient_per_hospital",
                        columnNames = {"patient_id", "hospital_id"}
                )
        },
        indexes = {
                @Index(name="idx_hospital_patient_patient_id",columnList = "patient_id"),
                @Index(name="idx_hospital_patient_hospital_id",columnList = "hospital_id"),
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalPatient extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @Column(name = "hospital_patient_identifier")
    private String hospitalPatientIdentifier;
}