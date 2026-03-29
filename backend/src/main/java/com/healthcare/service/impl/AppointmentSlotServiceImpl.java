package com.healthcare.service.impl;

import com.healthcare.dto.request.AppointmentSlotRequest;
import com.healthcare.dto.response.AppointmentSlotResponse;
import com.healthcare.exception.ResourceConflictException;
import com.healthcare.model.AppointmentSlot;
import com.healthcare.model.Doctor;
import com.healthcare.model.Hospital;
import com.healthcare.repository.AppointmentSlotRepository;
import com.healthcare.repository.DoctorRepository;
import com.healthcare.repository.HospitalRepository;
import com.healthcare.service.AppointmentSlotService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentSlotServiceImpl implements AppointmentSlotService {

    private final AppointmentSlotRepository appointmentSlotRepository;
    private final HospitalRepository hospitalRepository;
    private final DoctorRepository doctorRepository;

    @Override
    @Transactional
    public AppointmentSlotResponse createSlot(AppointmentSlotRequest appointmentSlotRequest) {

        Hospital hospital=hospitalRepository.findById(appointmentSlotRequest.getHospitalId())
                .orElseThrow(()->new ResourceConflictException("No hospital found with this id: "+appointmentSlotRequest.getHospitalId()));
        Doctor doctor=doctorRepository.findById(appointmentSlotRequest.getDoctorId())
                .orElseThrow(()->new ResourceConflictException("No Doctor found with this id: "+appointmentSlotRequest.getDoctorId()));

        AppointmentSlot slot = AppointmentSlot.builder()
                .doctor(doctor)
                .hospital(hospital)
                .slotDate(appointmentSlotRequest.getSlotDate())
                .startTime(appointmentSlotRequest.getStartTime())
                .endTime(appointmentSlotRequest.getEndTime())
                .maxAppointments(1)
                .bookedAppointments(0)
                .isAvailable(true)
                .build();
        try{
            return mapToResponse(appointmentSlotRepository.save(slot));
        } catch (DataIntegrityViolationException ex) {
            throw  new DataIntegrityViolationException( "Appointment slot already exists for this doctor and time");
        }



    }

    private AppointmentSlotResponse mapToResponse(AppointmentSlot slot) {
        return AppointmentSlotResponse.builder()
                .id(slot.getId())
                .doctorId(slot.getDoctor().getId())
                .hospitalId(slot.getHospital().getId())
                .slotDate(slot.getSlotDate())
                .startTime(slot.getStartTime())
                .endTime(slot.getEndTime())
                .isAvailable(slot.getIsAvailable())
                .build();
    }
}
