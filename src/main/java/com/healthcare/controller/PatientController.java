package com.healthcare.controller;

import com.healthcare.dto.request.PatientAddRequest;
import com.healthcare.dto.request.PatientProfileUpdateRequest;
import com.healthcare.dto.response.PatientProfileResponse;
import com.healthcare.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "Patient Management", description = "Endpoints for managing patient profiles")
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_HOSPITAL_ADMIN')")
    @Operation(summary = "Add Patient", description = "Adding Patient from users Db to Patient db")
    public ResponseEntity<PatientProfileResponse> addPatients(@RequestBody @Valid PatientAddRequest patientAddRequest) {
        return ResponseEntity.ok(patientService.addPatients(patientAddRequest));
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    @Operation(summary = "Get my profile", description = "Retrieve current logged-in patient's profile")
    public ResponseEntity<PatientProfileResponse> getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(patientService.getPatientProfileByEmail(email));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    @Operation(summary = "Update profile", description = "Update current logged-in patient's profile")
    public ResponseEntity<PatientProfileResponse> updateProfile(
            Authentication authentication,
            @Valid @RequestBody PatientProfileUpdateRequest request) {
        String email = authentication.getName();
        return ResponseEntity.ok(patientService.updatePatientProfile(email, request));
    }
}
