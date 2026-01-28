package com.healthcare.service.impl;

import com.healthcare.dto.request.SpecializationCreateRequest;
import com.healthcare.dto.request.SpecializationUpdateRequest;
import com.healthcare.dto.response.SpecializationResponse;
import com.healthcare.exception.ResourceConflictException;
import com.healthcare.model.Specialization;
import com.healthcare.repository.SpecializationRepository;
import com.healthcare.service.SpecializationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SpecializationServiceImpl implements SpecializationService {

    private final SpecializationRepository specializationRepository;

    @Override
    public SpecializationResponse create(SpecializationCreateRequest request) {

        if(specializationRepository.existsByNameIgnoreCase(request.getName()))
            throw new ResourceConflictException("Specialization Already exists");

        Specialization specialization=Specialization.builder()
                .name(request.getName())
                .description(request.getDescription())
                .isActive(true)
                .build();
        return  mapToResponse(specializationRepository.save(specialization));
    }

    @Override
    public SpecializationResponse update(Long id, SpecializationUpdateRequest request) {

        Specialization specialization=specializationRepository.findById(id)
                .orElseThrow(()->new ResourceConflictException("No Specialization exits with id: "+id));

        specialization.setDescription(request.getDescription());
        specialization.setIsActive(request.getIsActive());

        return mapToResponse(specialization);

    }

    @Override
    @Transactional(readOnly = true)
    public SpecializationResponse getById(Long id) {
        return mapToResponse(specializationRepository.findById(id)
                .orElseThrow(()->new ResourceConflictException("No Specialization exits with id: "+id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpecializationResponse> getAllActive() {
        return specializationRepository.findByIsActive(true)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpecializationResponse> getAllDeActive() {
        return specializationRepository.findByIsActive(false)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }



    @Override
    public void deactivate(Long id) {
        specializationRepository.deActivate(id);
    }

    private SpecializationResponse mapToResponse(Specialization specialization){
        return SpecializationResponse.builder()
                .id(specialization.getId())
                .name(specialization.getName())
                .description(specialization.getDescription())
                .isActive(specialization.getIsActive())
                .doctorCount(specialization.getDoctors().size())
                .build();
    }

}
