package com.healthcare.controller;

import com.healthcare.dto.request.HospitalRequest;
import com.healthcare.dto.response.HospitalResponse;
import com.healthcare.service.HospitalService;
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
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
@Tag(name = "Hospital Management", description = "Endpoints for managing hospitals (Admin)")
public class HospitalController {

    private final HospitalService hospitalService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_HOSPITAL_ADMIN')")
    @Operation(summary = "Create Hospital", description = "Register a new hospital (Admin only)")
    public ResponseEntity<HospitalResponse> createHospital(@Valid @RequestBody HospitalRequest request) {
        return new ResponseEntity<>(hospitalService.createHospital(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Hospital", description = "Get hospital details by ID")
    public ResponseEntity<HospitalResponse> getHospital(@PathVariable Long id) {
        return ResponseEntity.ok(hospitalService.getHospital(id));
    }

    @GetMapping
    @Operation(summary = "Get All Hospitals", description = "List all registered hospitals")
    public ResponseEntity<List<HospitalResponse>> getAllHospitals() {
        return ResponseEntity.ok(hospitalService.getAllHospitals());
    }
}
