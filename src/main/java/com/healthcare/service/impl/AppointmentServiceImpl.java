package com.healthcare.service.impl;

import com.healthcare.dto.request.AppointmentRequest;
import com.healthcare.dto.response.AppointmentResponse;
import com.healthcare.exception.ResourceConflictException;
import com.healthcare.exception.ResourceNotFoundException;
import com.healthcare.model.Appointment;
import com.healthcare.model.AppointmentSlot;
import com.healthcare.model.Patient;
import com.healthcare.model.enums.AppointmentStatus;
import com.healthcare.repository.AppointmentRepository;
import com.healthcare.repository.AppointmentSlotRepository;
import com.healthcare.repository.PatientRepository;
import com.healthcare.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentSlotRepository slotRepository;
    private final PatientRepository patientRepository;

    @Override
    public AppointmentResponse bookAppointment(AppointmentRequest request,String idempotencyKey) {
        try {
            return bookAppointmentInternal(request,idempotencyKey);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new ResourceConflictException("This slot was just booked by someone else. Please try another slot.");
        }
    }

    private AppointmentResponse bookAppointmentInternal(AppointmentRequest request,String idempotencyKey) {

//        Patient patient=patientRepository.findBy()

        Optional<Appointment> existingAppointment=appointmentRepository.findByPatientIdAndIdempotencyKey(request.getPatientId(),idempotencyKey);

        //prevent creating a duplicate appointment
        if(existingAppointment.isPresent()) return mapToResponse(existingAppointment.get());

        AppointmentSlot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

        if (!slot.getIsAvailable() || slot.getBookedAppointments() >= slot.getMaxAppointments()) {
            throw new ResourceConflictException("Slot is not available");
        }

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Appointment appointment = Appointment.builder()
                .hospital(slot.getHospital())
                .doctor(slot.getDoctor())
                .patient(patient)
                .appointmentSlot(slot)
                .appointmentDate(slot.getSlotDate())
                .appointmentTime(slot.getStartTime())
                .status(AppointmentStatus.BOOKED)
                .idempotencyKey(idempotencyKey)
                .patientNotes(request.getReason())
                .build();

        // Update slot availability (Managed Entity)
        slot.addAppointment(appointment);
        
        // Save
        Appointment savedAppointment = appointmentRepository.save(appointment);

        return mapToResponse(savedAppointment);
    }

    @Override
    public AppointmentResponse getAppointment(Long id) {
        return appointmentRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
    }

    @Override
    public List<AppointmentResponse> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getDoctorAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.CANCELLED) {
            appointment.setStatus(AppointmentStatus.CANCELLED);
            
            // Free up slot
            AppointmentSlot slot = appointment.getAppointmentSlot();
            slot.removeAppointment(appointment);
            
            appointmentRepository.save(appointment);
            slotRepository.save(slot);
        }
    }

    private AppointmentResponse mapToResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatient().getId())
                .patientName(appointment.getPatient().getUser().getFullName())
                .doctorId(appointment.getDoctor().getId())
                .doctorName(appointment.getDoctor().getUser().getFullName())
                .slotId(appointment.getAppointmentSlot().getId())
                .appointmentDate(appointment.getAppointmentDate())
                .startTime(appointment.getAppointmentTime())
                .endTime(appointment.getAppointmentSlot().getEndTime())
                .status(appointment.getStatus())
                .reason(appointment.getPatientNotes())
                .createdAt(appointment.getCreatedAt())
                .build();
    }
}
