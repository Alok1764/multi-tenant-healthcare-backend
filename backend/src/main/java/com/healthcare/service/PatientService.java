package com.healthcare.service;

import com.healthcare.dto.request.PatientAddRequest;
import com.healthcare.dto.request.PatientProfileUpdateRequest;
import com.healthcare.dto.response.PatientProfileResponse;
import jakarta.validation.Valid;

public interface PatientService {
    PatientProfileResponse getPatientProfileByEmail(String email);
    PatientProfileResponse updatePatientProfile(String email, PatientProfileUpdateRequest request);
    PatientProfileResponse getPatientById(Long id);
    PatientProfileResponse addPatients(@Valid PatientAddRequest patientAddRequest);
}
