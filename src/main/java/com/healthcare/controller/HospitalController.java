package com.healthcare.controller;

import com.healthcare.dto.request.HospitalRequest;
import com.healthcare.dto.response.HospitalResponse;
import com.healthcare.service.HospitalService;
import com.healthcare.swagger.hospital.CreateHospitalDoc;
import com.healthcare.swagger.hospital.GetAllHospitalsDoc;
import com.healthcare.swagger.hospital.GetHospitalDoc;
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
@Tag(name = "Hospital Management")
public class HospitalController {

    private final HospitalService hospitalService;

    @CreateHospitalDoc
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_HOSPITAL_ADMIN')")
    public ResponseEntity<HospitalResponse> createHospital(@Valid @RequestBody HospitalRequest request) {
        return new ResponseEntity<>(hospitalService.createHospital(request), HttpStatus.CREATED);
    }

    @GetHospitalDoc
    @GetMapping("/{id}")
    public ResponseEntity<HospitalResponse> getHospital(@PathVariable Long id) {
        return ResponseEntity.ok(hospitalService.getHospital(id));
    }

    @GetAllHospitalsDoc
    @GetMapping
    public ResponseEntity<List<HospitalResponse>> getAllHospitals() {
        return ResponseEntity.ok(hospitalService.getAllHospitals());
    }
}
