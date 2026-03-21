package com.healthcare.repository;

import com.healthcare.model.HospitalPatient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalPatientRepository extends JpaRepository<HospitalPatient,Long> {
}
