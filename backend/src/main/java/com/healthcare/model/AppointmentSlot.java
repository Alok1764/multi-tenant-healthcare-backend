package com.healthcare.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "appointment_slots",
       uniqueConstraints = {
           @UniqueConstraint(name = "unique_doctor_slot",
                           columnNames = {"doctor_id", "slot_date", "start_time"})
       },
       indexes = {
           @Index(name = "idx_appointment_slots_hospital_id", columnList = "hospital_id"),
           @Index(name = "idx_appointment_slots_doctor_id", columnList = "doctor_id"),
           @Index(name = "idx_appointment_slots_date", columnList = "slot_date"),
           @Index(name = "idx_appointment_slots_available", columnList = "is_available")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentSlot extends BaseEntity {

    @Version
    private Long version;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @NotNull
    @Column(name = "slot_date", nullable = false)
    private LocalDate slotDate;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    @NotNull
    @Positive
    @Column(name = "max_appointments", nullable = false)
    private Integer maxAppointments;

    @NotNull
    @PositiveOrZero
    @Column(name = "booked_appointments", nullable = false)
    private Integer bookedAppointments;

    // Bidirectional relationship
    @OneToMany(mappedBy = "appointmentSlot", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Appointment> appointments = new ArrayList<>();

    // Helper methods
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        appointment.setAppointmentSlot(this);
        this.bookedAppointments++;
        if (this.bookedAppointments >= this.maxAppointments) {
            this.isAvailable = false;
        }
    }

    public void removeAppointment(Appointment appointment) {
        if(bookedAppointments <=0) return;

        appointments.remove(appointment);
        appointment.setAppointmentSlot(null);
        this.bookedAppointments--;
        this.isAvailable = this.bookedAppointments < this.maxAppointments;
    }
}
