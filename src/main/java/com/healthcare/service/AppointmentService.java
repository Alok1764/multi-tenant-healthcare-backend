package com.healthcare.service;

import com.healthcare.dto.request.AppointmentRequest;
import com.healthcare.dto.response.AppointmentResponse;
import com.healthcare.security.UserPrincipal;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AppointmentService {
    AppointmentResponse bookAppointment(AppointmentRequest request, String idempotencyKey, UserPrincipal userPrincipal);
    AppointmentResponse getAppointment(Long id);
    List<AppointmentResponse> getPatientAppointments(Long patientId);
    List<AppointmentResponse> getDoctorAppointments(Long doctorId);
    void cancelAppointment(Long appointmentId);
}
