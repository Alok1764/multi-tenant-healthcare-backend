package com.healthcare.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentSlotRequest {

    @NotNull
    private Long hospitalId;

    @NotNull
    private Long doctorId;

    @NotNull
    private LocalDate slotDate;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

//    @Positive
//    private Integer maxAppointments;
}
