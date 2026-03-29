package com.healthcare.service;

import com.healthcare.dto.request.SpecializationCreateRequest;
import com.healthcare.dto.request.SpecializationUpdateRequest;
import com.healthcare.dto.response.SpecializationResponse;

import java.util.List;

public interface SpecializationService {

    SpecializationResponse create(SpecializationCreateRequest request);

    SpecializationResponse update(Long id, SpecializationUpdateRequest request);

    SpecializationResponse getById(Long id);

    List<SpecializationResponse> getAllActive();

    List<SpecializationResponse> getAllDeActive();

    void deactivate(Long id);
}
