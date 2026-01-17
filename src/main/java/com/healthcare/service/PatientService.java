package com.healthcare.service;

import com.healthcare.dto.request.PatientProfileRequest;
import com.healthcare.dto.response.PatientProfileResponse;

public interface PatientService {
    PatientProfileResponse getPatientProfileByEmail(String email);
    PatientProfileResponse updatePatientProfile(String email, PatientProfileRequest request);
    PatientProfileResponse getPatientById(Long id);
}
