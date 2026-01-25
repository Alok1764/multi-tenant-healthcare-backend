package com.healthcare.service;

import com.healthcare.dto.request.AppointmentRequest;
import com.healthcare.dto.response.AppointmentResponse;

import java.util.List;

public interface AppointmentService {
    AppointmentResponse bookAppointment(AppointmentRequest request,String idempotencyKey);
    AppointmentResponse getAppointment(Long id);
    List<AppointmentResponse> getPatientAppointments(Long patientId);
    List<AppointmentResponse> getDoctorAppointments(Long doctorId);
    void cancelAppointment(Long appointmentId);
}
