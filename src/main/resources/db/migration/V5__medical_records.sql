-- V5: Medical Records
-- Description: Post-consultation medical records and prescriptions

-- Medical records table (multi-tenant)
CREATE TABLE medical_records (
    id BIGSERIAL PRIMARY KEY,
    hospital_id BIGINT NOT NULL,
    appointment_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    diagnosis TEXT NOT NULL,
    symptoms TEXT,
    prescriptions TEXT,
    lab_tests_recommended TEXT,
    follow_up_required BOOLEAN NOT NULL DEFAULT false,
    follow_up_date DATE,
    vital_signs JSONB, -- Store vitals like BP, temperature, etc. as JSON
    attachments JSONB, -- Store file URLs/paths as JSON array
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (hospital_id) REFERENCES hospitals(id) ON DELETE CASCADE,
    FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE RESTRICT,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE RESTRICT,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE RESTRICT,
    CONSTRAINT unique_record_per_appointment UNIQUE (appointment_id)
);

-- Create indexes
CREATE INDEX idx_medical_records_hospital_id ON medical_records(hospital_id);
CREATE INDEX idx_medical_records_appointment_id ON medical_records(appointment_id);
CREATE INDEX idx_medical_records_patient_id ON medical_records(patient_id);
CREATE INDEX idx_medical_records_doctor_id ON medical_records(doctor_id);
CREATE INDEX idx_medical_records_created_at ON medical_records(created_at);

-- Trigger for medical_records table
CREATE TRIGGER update_medical_records_updated_at
    BEFORE UPDATE ON medical_records
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();
