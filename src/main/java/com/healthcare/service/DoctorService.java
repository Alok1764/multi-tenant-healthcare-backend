package com.healthcare.service;

import com.healthcare.dto.request.AvailabilityRequest;
import com.healthcare.dto.request.DoctorRegistrationRequest;
import com.healthcare.dto.response.DoctorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DoctorService {
    DoctorResponse onboardDoctor(DoctorRegistrationRequest request);
    DoctorResponse getDoctorProfile(Long doctorId);
    void setAvailability(AvailabilityRequest request);
    List<DoctorResponse> getAllDoctors(Pageable pageable);
}
