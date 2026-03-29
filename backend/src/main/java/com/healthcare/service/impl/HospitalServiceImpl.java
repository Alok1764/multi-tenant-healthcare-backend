package com.healthcare.service.impl;

import com.healthcare.dto.request.HospitalRequest;
import com.healthcare.dto.response.HospitalResponse;
import com.healthcare.exception.ResourceConflictException;
import com.healthcare.exception.ResourceNotFoundException;
import com.healthcare.model.Hospital;
import com.healthcare.repository.HospitalRepository;
import com.healthcare.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository hospitalRepository;

    @Override
    @CacheEvict(value = {"hospitals", "hospitals-all"}, allEntries = true)
    public HospitalResponse createHospital(HospitalRequest request) {
        if (hospitalRepository.existsByEmail(request.getEmail())) {
            throw new ResourceConflictException("Hospital with this email already exists");
        }

        Hospital hospital = Hospital.builder()
                .hospitalName(request.getHospitalName())
                .registrationNumber(request.getRegistrationNumber())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .postalCode(request.getPostalCode())
                .website(request.getWebsite())
                .isActive(true)
                .isVerified(false) // Needs admin verification typically
                .build();

        Hospital savedHospital = hospitalRepository.save(hospital);
        return mapToResponse(savedHospital);
    }

    @Override
    @Cacheable(value = "hospitals", key = "#id")
    public HospitalResponse getHospital(Long id) {
        return hospitalRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found"));
    }

    @Override
    @Cacheable(value = "hospitals-all")
    public List<HospitalResponse> getAllHospitals() {
        return hospitalRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private HospitalResponse mapToResponse(Hospital hospital) {
        return HospitalResponse.builder()
                .id(hospital.getId())
                .hospitalName(hospital.getHospitalName())
                .registrationNumber(hospital.getRegistrationNumber())
                .email(hospital.getEmail())
                .phoneNumber(hospital.getPhoneNumber())
                .address(hospital.getAddress())
                .city(hospital.getCity())
                .state(hospital.getState())
                .country(hospital.getCountry())
                .postalCode(hospital.getPostalCode())
                .website(hospital.getWebsite())
                .isActive(hospital.getIsActive())
                .isVerified(hospital.getIsVerified())
                .build();
    }
}
