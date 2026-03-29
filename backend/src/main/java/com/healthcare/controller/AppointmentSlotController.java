package com.healthcare.controller;

import com.healthcare.dto.request.AppointmentSlotRequest;
import com.healthcare.dto.response.AppointmentSlotResponse;
import com.healthcare.service.AppointmentSlotService;
import com.healthcare.swagger.appointment.*;
import com.healthcare.swagger.appointmentslot.CreateSlotDoc;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointment-slots")
@RequiredArgsConstructor
@Tag(name = "Appointment Slots")
public class AppointmentSlotController {

    private final AppointmentSlotService appointmentSlotService;

    @CreateSlotDoc
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_HOSPITAL_ADMIN')")
    public ResponseEntity<AppointmentSlotResponse> createSlot(
            @RequestBody @Valid AppointmentSlotRequest appointmentSlotRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(appointmentSlotService.createSlot(appointmentSlotRequest));
    }
}
