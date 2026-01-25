package com.healthcare.service.impl;

import com.healthcare.dto.request.DoctorRegistrationRequest;
import com.healthcare.dto.response.DoctorResponse;
import com.healthcare.exception.ResourceConflictException;
import com.healthcare.exception.ResourceNotFoundException;
import com.healthcare.dto.request.AvailabilityRequest;
import com.healthcare.model.Doctor;
import com.healthcare.model.User;
import com.healthcare.model.AppointmentSlot;
import com.healthcare.model.enums.UserRole;
import com.healthcare.repository.DoctorRepository;
import com.healthcare.repository.UserRepository;
import com.healthcare.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final com.healthcare.repository.SpecializationRepository specializationRepository;
    private final com.healthcare.repository.HospitalRepository hospitalRepository;

    @Override
    public DoctorResponse onboardDoctor(DoctorRegistrationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));

        if (doctorRepository.findByUserId(user.getId()).isPresent()) {
            throw new ResourceConflictException("Doctor profile already exists for user ID: " + request.getUserId());
        }

        // Ensure user has DOCTOR role
        if (!user.getRole().equals(UserRole.ROLE_DOCTOR)) {
             user.setRole(UserRole.ROLE_DOCTOR);
             userRepository.save(user);
        }

        Doctor doctor = Doctor.builder()
                .user(user)
                .hospital(hospitalRepository.findById(request.getHospitalId())
                        .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with ID: " + request.getHospitalId())))
                .licenseNumber(request.getLicenseNumber())
                .specialization(specializationRepository.findByName(request.getSpecialization())
                        .orElseThrow(() -> new ResourceNotFoundException("Specialization not found: " + request.getSpecialization())))
                .qualification(request.getQualification())
                .experienceYears(request.getExperienceYears())
                .consultationFee(request.getConsultationFee())
                .bio(request.getBio())
                .isVerified(false) // Verified by admin later
                .build();

        Doctor savedDoctor = doctorRepository.save(doctor);
        return mapToResponse(savedDoctor);
    }

    @Override
    public DoctorResponse getDoctorProfile(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));
    }

    private DoctorResponse mapToResponse(Doctor doctor) {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .userId(doctor.getUser().getId())
                .fullName(doctor.getUser().getFullName())
                .licenseNumber(doctor.getLicenseNumber())
                .specialization(doctor.getSpecialization().getName())
                .consultationFee(doctor.getConsultationFee())
                .bio(doctor.getBio())
                .isVerified(doctor.getIsVerified())
                .build();
    }

    @Override
    public void setAvailability(AvailabilityRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + request.getDoctorId()));

        LocalDateTime currentSlotStart = request.getStartTime();
        LocalDateTime endTime = request.getEndTime();
        int duration = request.getSlotDurationMinutes();

        while (currentSlotStart.plusMinutes(duration).isBefore(endTime) || currentSlotStart.plusMinutes(duration).isEqual(endTime)) {
            AppointmentSlot slot =AppointmentSlot.builder()
                    .hospital(doctor.getHospital())
                    .doctor(doctor)
                    .slotDate(currentSlotStart.toLocalDate())
                    .startTime(currentSlotStart.toLocalTime())
                    .endTime(currentSlotStart.toLocalTime().plusMinutes(duration))
                    .isAvailable(true)
                    .maxAppointments(1)
                    .bookedAppointments(0)
                    .build();

            doctor.addAppointmentSlot(slot);
            currentSlotStart = currentSlotStart.plusMinutes(duration);
        }
        doctorRepository.save(doctor);
    }
}
