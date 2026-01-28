package com.healthcare.controller;

import com.healthcare.dto.request.SpecializationCreateRequest;
import com.healthcare.dto.request.SpecializationUpdateRequest;
import com.healthcare.dto.response.SpecializationResponse;
import com.healthcare.service.SpecializationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specializations")
@RequiredArgsConstructor
public class SpecializationController {

    private final SpecializationService specializationService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_HOSPITAL_ADMIN')")
    public ResponseEntity<SpecializationResponse> create(
            @Valid @RequestBody SpecializationCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(specializationService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecializationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(specializationService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<SpecializationResponse>> getAllActive() {
        return ResponseEntity.ok(specializationService.getAllActive());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_HOSPITAL_ADMIN')")
    public ResponseEntity<SpecializationResponse> update(
            @PathVariable Long id,
            @RequestBody SpecializationUpdateRequest request){
        return ResponseEntity.ok(specializationService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_HOSPITAL_ADMIN')")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        specializationService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
