-- V4: Appointments and Slots
-- Description: Doctor availability slots and appointment booking system

-- Appointment slots table (multi-tenant)
CREATE TABLE appointment_slots (
    id BIGSERIAL PRIMARY KEY,
    hospital_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    slot_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT true,
    max_appointments INTEGER NOT NULL DEFAULT 1 CHECK (max_appointments > 0),
    booked_appointments INTEGER NOT NULL DEFAULT 0 CHECK (booked_appointments >= 0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (hospital_id) REFERENCES hospitals(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE,
    CONSTRAINT check_end_time_after_start CHECK (end_time > start_time),
    CONSTRAINT check_booked_not_exceed_max CHECK (booked_appointments <= max_appointments),
    CONSTRAINT unique_doctor_slot UNIQUE (doctor_id, slot_date, start_time)
);

-- Create indexes
CREATE INDEX idx_appointment_slots_hospital_id ON appointment_slots(hospital_id);
CREATE INDEX idx_appointment_slots_doctor_id ON appointment_slots(doctor_id);
CREATE INDEX idx_appointment_slots_date ON appointment_slots(slot_date);
CREATE INDEX idx_appointment_slots_available ON appointment_slots(is_available);

-- Trigger for appointment_slots table
CREATE TRIGGER update_appointment_slots_updated_at
    BEFORE UPDATE ON appointment_slots
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Appointments table (multi-tenant)
CREATE TABLE appointments (
    id BIGSERIAL PRIMARY KEY,
    hospital_id BIGINT NOT NULL,
    appointment_slot_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status VARCHAR(50) NOT NULL CHECK (status IN ('SCHEDULED', 'CONFIRMED', 'COMPLETED', 'CANCELLED', 'NO_SHOW')),
    cancellation_reason TEXT,
    patient_notes TEXT,
    doctor_notes TEXT,
    consultation_type VARCHAR(50) CHECK (consultation_type IN ('IN_PERSON', 'VIDEO', 'PHONE')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (hospital_id) REFERENCES hospitals(id) ON DELETE CASCADE,
    FOREIGN KEY (appointment_slot_id) REFERENCES appointment_slots(id) ON DELETE RESTRICT,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE RESTRICT,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE RESTRICT
);

-- Create indexes
CREATE INDEX idx_appointments_hospital_id ON appointments(hospital_id);
CREATE INDEX idx_appointments_slot_id ON appointment_slots(id);
CREATE INDEX idx_appointments_doctor_id ON appointments(doctor_id);
CREATE INDEX idx_appointments_patient_id ON appointments(patient_id);
CREATE INDEX idx_appointments_date ON appointments(appointment_date);
CREATE INDEX idx_appointments_status ON appointments(status);

-- Trigger for appointments table
CREATE TRIGGER update_appointments_updated_at
    BEFORE UPDATE ON appointments
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();
