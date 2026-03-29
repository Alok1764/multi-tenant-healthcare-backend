package com.healthcare.service.impl;

import com.healthcare.dto.request.PatientAddRequest;
import com.healthcare.dto.request.PatientProfileUpdateRequest;
import com.healthcare.dto.response.PatientProfileResponse;
import com.healthcare.exception.ResourceNotFoundException;
import com.healthcare.model.Hospital;
import com.healthcare.model.Patient;
import com.healthcare.model.User;
import com.healthcare.model.enums.BloodGroup;
import com.healthcare.model.enums.Gender;
import com.healthcare.repository.HospitalRepository;
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
    private final HospitalRepository hospitalRepository;

    @Override
    public PatientProfileResponse getPatientProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Patient patient = patientRepository.findByUserId(user.getId())
                 .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found. Please book an appointment to initialize your profile."));

        return mapToResponse(patient);
    }

    @Override
    public PatientProfileResponse updatePatientProfile(String email, PatientProfileUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));

        user.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(user);

        // Update Patient specific details
        patient.setAddress(request.getAddress());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(Gender.valueOf(request.getGender()));
        patient.setBloodGroup(BloodGroup.valueOf(request.getBloodGroup()));
        
        Patient savedPatient = patientRepository.save(patient);
        return mapToResponse(savedPatient);
    }

    @Override
    public PatientProfileResponse getPatientById(Long id) {
        return patientRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
    }

    @Override
    public PatientProfileResponse addPatients(PatientAddRequest patientAddRequest) {

        User user = userRepository.findById(patientAddRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found, Please first assign as user"));


        Patient patient = Patient.builder()
                .user(user)
                .dateOfBirth(patientAddRequest.getDateOfBirth())
                .gender(patientAddRequest.getGender())
                .bloodGroup(patientAddRequest.getBloodGroup())
                .address(patientAddRequest.getAddress())
                .emergencyContactName(patientAddRequest.getEmergencyContactName())
                .emergencyContactPhone(patientAddRequest.getEmergencyContactPhone())
                .medicalHistory(patientAddRequest.getMedicalHistory())
                .allergies(patientAddRequest.getAllergies())
                .profileImageUrl(null)
                .build();

        Patient savedPatient = patientRepository.save(patient);

        return mapToResponse(savedPatient);
    }



    private PatientProfileResponse mapToResponse(Patient patient) {
        return PatientProfileResponse.builder()
                .id(patient.getId())
                .fullName(patient.getUser().getFullName())
                .email(patient.getUser().getEmail())
                .phoneNumber(patient.getUser().getPhoneNumber())
                .address(patient.getAddress())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(String.valueOf(patient.getGender()))
                .bloodGroup(String.valueOf(patient.getBloodGroup()))
                .emergencyContactName(patient.getEmergencyContactName())
                .emergencyContactPhone(patient.getEmergencyContactPhone())
                .medicalHistory(patient.getMedicalHistory())
                .allergies(patient.getAllergies())
                .active(patient.getUser().getIsActive())
                .build();
    }
}
