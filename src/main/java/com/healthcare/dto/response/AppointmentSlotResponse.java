package com.healthcare.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class AppointmentSlotResponse {

    private Long id;
    private Long hospitalId;
    private Long doctorId;
    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isAvailable;
    private Integer maxAppointments;
    private Integer bookedAppointments;
}
