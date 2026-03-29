package com.healthcare.controller;

import com.healthcare.dto.request.AvailabilityRequest;
import com.healthcare.dto.request.DoctorRegistrationRequest;
import com.healthcare.dto.response.DoctorResponse;
import com.healthcare.service.DoctorService;
import com.healthcare.swagger.doctor.GetAllDoctorsDoc;
import com.healthcare.swagger.doctor.GetDoctorProfileDoc;
import com.healthcare.swagger.doctor.OnboardDoctorDoc;
import com.healthcare.swagger.doctor.SetAvailabilityDoc;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctor Management")
public class DoctorController {

    private final DoctorService doctorService;

    @OnboardDoctorDoc
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_HOSPITAL_ADMIN')")
    public ResponseEntity<DoctorResponse> onboardDoctor(
            @RequestBody @Valid DoctorRegistrationRequest request
    ) {
        return ResponseEntity.ok(doctorService.onboardDoctor(request));
    }

    @GetAllDoctorsDoc
    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ResponseEntity.ok(doctorService.getAllDoctors(PageRequest.of(pageNo, pageSize)));
    }

    @GetDoctorProfileDoc
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorProfile(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorProfile(id));
    }

    @SetAvailabilityDoc
    @PostMapping("/availability")
    @PreAuthorize("hasAuthority('DOCTOR')")
    public ResponseEntity<Void> setAvailability(
            @RequestBody @Valid AvailabilityRequest request
    ) {
        doctorService.setAvailability(request);
        return ResponseEntity.ok().build();
    }
}
