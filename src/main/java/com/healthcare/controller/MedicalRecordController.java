package com.healthcare.controller;

import com.healthcare.dto.request.MedicalRecordRequest;
import com.healthcare.dto.response.MedicalRecordResponse;
import com.healthcare.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
@Tag(name = "Medical Records", description = "Endpoints for managing medical records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @Operation(summary = "Create a medical record", description = "Create a new medical record for an appointment (Doctors only)")
    public ResponseEntity<MedicalRecordResponse> createMedicalRecord(@Valid @RequestBody MedicalRecordRequest request) {
        MedicalRecordResponse response = medicalRecordService.createMedicalRecord(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_DOCTOR', 'ROLE_PATIENT', 'ROLE_ADMIN')")
    @Operation(summary = "Get medical record by ID", description = "Retrieve a specific medical record")
    public ResponseEntity<MedicalRecordResponse> getMedicalRecord(@PathVariable Long id) {
        return ResponseEntity.ok(medicalRecordService.getMedicalRecord(id));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyAuthority('ROLE_DOCTOR', 'ROLE_PATIENT', 'ROLE_ADMIN')")
    @Operation(summary = "Get patient medical records", description = "Retrieve all medical records for a specific patient")
    public ResponseEntity<List<MedicalRecordResponse>> getPatientMedicalRecords(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicalRecordService.getPatientMedicalRecords(patientId));
    }
}
