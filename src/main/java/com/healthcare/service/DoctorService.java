package com.healthcare.service;

import com.healthcare.dto.request.DoctorRegistrationRequest;
import com.healthcare.dto.response.DoctorResponse;

public interface DoctorService {
    DoctorResponse onboardDoctor(DoctorRegistrationRequest request);
    DoctorResponse getDoctorProfile(Long doctorId);
}
