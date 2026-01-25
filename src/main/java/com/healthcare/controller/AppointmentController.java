package com.healthcare.controller;

import com.healthcare.dto.request.AppointmentRequest;
import com.healthcare.dto.response.AppointmentResponse;
import com.healthcare.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/book")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<AppointmentResponse> bookAppointment(
            @RequestHeader("X-Idempotency-Key")String idempotencyKey,
            @RequestBody @Valid AppointmentRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.bookAppointment(request,idempotencyKey));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<AppointmentResponse> getAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointment(id));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAuthority('PATIENT') or hasAuthority('DOCTOR')")
    public ResponseEntity<List<AppointmentResponse>> getPatientAppointments(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.getPatientAppointments(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAuthority('DOCTOR')")
    public ResponseEntity<List<AppointmentResponse>> getDoctorAppointments(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.getDoctorAppointments(doctorId));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('PATIENT') or hasAuthority('DOCTOR')")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
