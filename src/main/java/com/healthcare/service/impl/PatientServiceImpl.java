package com.healthcare.service.impl;

import com.healthcare.dto.request.PatientProfileRequest;
import com.healthcare.dto.response.PatientProfileResponse;
import com.healthcare.exception.ResourceNotFoundException;
import com.healthcare.model.Patient;
import com.healthcare.model.User;
import com.healthcare.repository.PatientRepository;
import com.healthcare.repository.UserRepository;
import com.healthcare.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @Override
    public PatientProfileResponse getPatientProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Ensure patient record exists for user (if not, maybe auto-create or throw error)
        // For now, let's assume registration creates a basic patient record or we create on fly
        // But our current flow creates Patient only on booking if not exists?
        // Actually UserService usually creates User. Patient entity is separate.
        // Let's rely on finding by User ID.
        
        Patient patient = patientRepository.findByUserId(user.getId())
                 .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found. Please book an appointment to initialize your profile."));

        return mapToResponse(patient);
    }

    @Override
    public PatientProfileResponse updatePatientProfile(String email, PatientProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Patient patient = patientRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));

        // Update User details (common fields)
        user.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(user);

        // Update Patient specific details
        patient.setAddress(request.getAddress());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setBloodGroup(request.getBloodGroup());
        
        Patient savedPatient = patientRepository.save(patient);
        return mapToResponse(savedPatient);
    }

    @Override
    public PatientProfileResponse getPatientById(Long id) {
        return patientRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
    }

    private PatientProfileResponse mapToResponse(Patient patient) {
        return PatientProfileResponse.builder()
                .id(patient.getId())
                .userId(patient.getUser().getId())
                .fullName(patient.getUser().getFullName())
                .email(patient.getUser().getEmail())
                .phoneNumber(patient.getUser().getPhoneNumber())
                .address(patient.getAddress())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .bloodGroup(patient.getBloodGroup())
                .active(patient.getUser().isActive())
                .build();
    }
}
