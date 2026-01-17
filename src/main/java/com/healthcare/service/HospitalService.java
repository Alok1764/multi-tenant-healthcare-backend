package com.healthcare.service;

import com.healthcare.dto.request.HospitalRequest;
import com.healthcare.dto.response.HospitalResponse;

import java.util.List;

public interface HospitalService {
    HospitalResponse createHospital(HospitalRequest request);
    HospitalResponse getHospital(Long id);
    List<HospitalResponse> getAllHospitals();
}
