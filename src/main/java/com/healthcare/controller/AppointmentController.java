package com.healthcare.controller;

import com.healthcare.dto.request.AppointmentRequest;
import com.healthcare.dto.response.AppointmentResponse;
import com.healthcare.security.UserPrincipal;
import com.healthcare.service.AppointmentService;
import com.healthcare.swagger.appointment.BookAppointmentDoc;
import com.healthcare.swagger.appointment.CancelAppointmentDoc;
import com.healthcare.swagger.appointment.GetAppointmentDoc;
import com.healthcare.swagger.appointment.GetDoctorAppointmentsDoc;
import com.healthcare.swagger.appointment.GetPatientAppointmentsDoc;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointment Management")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @BookAppointmentDoc
    @PostMapping("/book")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<AppointmentResponse> bookAppointment(
            @RequestHeader("X-Idempotency-Key") String idempotencyKey,
            @RequestBody @Valid AppointmentRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(appointmentService.bookAppointment(request, idempotencyKey, userPrincipal));
    }

    @GetAppointmentDoc
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<AppointmentResponse> getAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointment(id));
    }

    @GetPatientAppointmentsDoc
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAuthority('PATIENT') or hasAuthority('DOCTOR')")
    public ResponseEntity<List<AppointmentResponse>> getPatientAppointments(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.getPatientAppointments(patientId));
    }

    @GetDoctorAppointmentsDoc
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAuthority('DOCTOR')")
    public ResponseEntity<List<AppointmentResponse>> getDoctorAppointments(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.getDoctorAppointments(doctorId));
    }

    @CancelAppointmentDoc
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('PATIENT') or hasAuthority('DOCTOR')")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
