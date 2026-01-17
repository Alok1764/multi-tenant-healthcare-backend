package com.healthcare.service.impl;

import com.healthcare.dto.request.MedicalRecordRequest;
import com.healthcare.dto.response.MedicalRecordResponse;
import com.healthcare.exception.ResourceConflictException;
import com.healthcare.exception.ResourceNotFoundException;
import com.healthcare.model.Appointment;
import com.healthcare.model.MedicalRecord;
import com.healthcare.model.enums.AppointmentStatus;
import com.healthcare.repository.AppointmentRepository;
import com.healthcare.repository.MedicalRecordRepository;
import com.healthcare.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public MedicalRecordResponse createMedicalRecord(MedicalRecordRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        // Validation: Verify appointment is not cancelled
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new ResourceConflictException("Cannot create medical record for a cancelled appointment");
        }
        
        // Validation: Check if record already exists
        if (medicalRecordRepository.findByAppointmentId(request.getAppointmentId()).isPresent()) {
             throw new ResourceConflictException("Medical record already exists for this appointment");
        }

        MedicalRecord medicalRecord = MedicalRecord.builder()
                .hospital(appointment.getAppointmentSlot().getHospital())
                .appointment(appointment)
                .patient(appointment.getPatient())
                .doctor(appointment.getDoctor())
                .diagnosis(request.getDiagnosis())
                .prescriptions(request.getPrescription())
                .notes(request.getNotes()) // Mapping notes
                // We use 'treatment' from request as part of notes or separate if entity allowed
                // For now, let's append treatment to notes if present
                .symptoms(request.getTreatment()) // Using symptoms field for treatment plan for now as per DTO mismatch
                .build();

        if (request.getTreatment() != null) {
            medicalRecord.setSymptoms("Treatment Plan: " + request.getTreatment());
        }

        MedicalRecord savedRecord = medicalRecordRepository.save(medicalRecord);
        
        // Update appointment status to COMPLETED if not already
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            appointment.setStatus(AppointmentStatus.COMPLETED);
            appointmentRepository.save(appointment);
        }

        return mapToResponse(savedRecord);
    }

    @Override
    public MedicalRecordResponse getMedicalRecord(Long id) {
        return medicalRecordRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found"));
    }

    @Override
    public List<MedicalRecordResponse> getPatientMedicalRecords(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private MedicalRecordResponse mapToResponse(MedicalRecord record) {
        return MedicalRecordResponse.builder()
                .id(record.getId())
                .appointmentId(record.getAppointment().getId())
                .patientId(record.getPatient().getId())
                .patientName(record.getPatient().getUser().getFullName())
                .doctorId(record.getDoctor().getId())
                .doctorName(record.getDoctor().getUser().getFullName())
                .diagnosis(record.getDiagnosis())
                .prescription(record.getPrescriptions())
                .treatment(record.getSymptoms()) // Mapping back
                .notes(record.getNotes())
                .createdAt(record.getCreatedAt())
                .build();
    }
}
