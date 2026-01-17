package com.healthcare.controller;

import com.healthcare.dto.request.AvailabilityRequest;
import com.healthcare.dto.request.DoctorRegistrationRequest;
import com.healthcare.dto.response.DoctorResponse;
import com.healthcare.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<DoctorResponse> onboardDoctor(
            @RequestBody @Valid DoctorRegistrationRequest request
    ) {
        return ResponseEntity.ok(doctorService.onboardDoctor(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorProfile(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorProfile(id));
    }

    @PostMapping("/availability")
    @PreAuthorize("hasAuthority('DOCTOR')")
    public ResponseEntity<Void> setAvailability(
            @RequestBody @Valid AvailabilityRequest request
    ) {
        doctorService.setAvailability(request);
        return ResponseEntity.ok().build();
    }
}
