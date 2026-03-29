package com.healthcare.service;

import com.healthcare.dto.request.AppointmentSlotRequest;
import com.healthcare.dto.response.AppointmentSlotResponse;


public interface AppointmentSlotService {

    AppointmentSlotResponse createSlot(AppointmentSlotRequest appointmentSlotRequest);

}
