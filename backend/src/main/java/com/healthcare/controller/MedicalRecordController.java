package com.healthcare.controller;

import com.healthcare.dto.request.MedicalRecordRequest;
import com.healthcare.dto.response.MedicalRecordResponse;
import com.healthcare.service.MedicalRecordService;
import com.healthcare.swagger.medicalrecord.CreateMedicalRecordDoc;
import com.healthcare.swagger.medicalrecord.GetMedicalRecordDoc;
import com.healthcare.swagger.medicalrecord.GetPatientMedicalRecordsDoc;
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
@Tag(name = "Medical Records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @CreateMedicalRecordDoc
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<MedicalRecordResponse> createMedicalRecord(
            @Valid @RequestBody MedicalRecordRequest request
    ) {
        return new ResponseEntity<>(medicalRecordService.createMedicalRecord(request), HttpStatus.CREATED);
    }

    @GetMedicalRecordDoc
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_DOCTOR', 'ROLE_PATIENT', 'ROLE_ADMIN')")
    public ResponseEntity<MedicalRecordResponse> getMedicalRecord(@PathVariable Long id) {
        return ResponseEntity.ok(medicalRecordService.getMedicalRecord(id));
    }

    @GetPatientMedicalRecordsDoc
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyAuthority('ROLE_DOCTOR', 'ROLE_PATIENT', 'ROLE_ADMIN')")
    public ResponseEntity<List<MedicalRecordResponse>> getPatientMedicalRecords(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicalRecordService.getPatientMedicalRecords(patientId));
    }
}
