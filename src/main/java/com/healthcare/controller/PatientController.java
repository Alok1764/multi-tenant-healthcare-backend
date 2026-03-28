package com.healthcare.controller;

import com.healthcare.dto.request.PatientAddRequest;
import com.healthcare.dto.request.PatientProfileUpdateRequest;
import com.healthcare.dto.response.PatientProfileResponse;
import com.healthcare.service.PatientService;
import com.healthcare.swagger.patient.AddPatientDoc;
import com.healthcare.swagger.patient.GetMyProfileDoc;
import com.healthcare.swagger.patient.UpdatePatientProfileDoc;
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
@Tag(name = "Patient Management")
public class PatientController {

    private final PatientService patientService;

    @AddPatientDoc
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_HOSPITAL_ADMIN')")
    public ResponseEntity<PatientProfileResponse> addPatients(
            @RequestBody @Valid PatientAddRequest patientAddRequest
    ) {
        return ResponseEntity.ok(patientService.addPatients(patientAddRequest));
    }

    @GetMyProfileDoc
    @GetMapping("/me")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<PatientProfileResponse> getMyProfile(Authentication authentication) {
        return ResponseEntity.ok(patientService.getPatientProfileByEmail(authentication.getName()));
    }

    @UpdatePatientProfileDoc
    @PutMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<PatientProfileResponse> updateProfile(
            Authentication authentication,
            @Valid @RequestBody PatientProfileUpdateRequest request
    ) {
        return ResponseEntity.ok(patientService.updatePatientProfile(authentication.getName(), request));
    }
}
