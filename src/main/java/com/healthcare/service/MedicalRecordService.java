package com.healthcare.service;

import com.healthcare.dto.request.MedicalRecordRequest;
import com.healthcare.dto.response.MedicalRecordResponse;

import java.util.List;

public interface MedicalRecordService {
    MedicalRecordResponse createMedicalRecord(MedicalRecordRequest request);
    MedicalRecordResponse getMedicalRecord(Long id);
    List<MedicalRecordResponse> getPatientMedicalRecords(Long patientId);
}
