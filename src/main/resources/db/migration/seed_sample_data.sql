-- Sample Data Seeding Script
-- This script inserts sample data for testing and development

-- Insert sample subscription plans
INSERT INTO subscription_plans (plan_name, description, monthly_price, max_doctors, max_patients, features, is_active) VALUES
('BASIC', 'Basic plan for small clinics', 999.00, 5, 100, '{"appointment_booking": true, "medical_records": true, "payment_integration": false, "analytics": false}'::jsonb, true),
('PREMIUM', 'Premium plan for medium hospitals', 2999.00, 20, 500, '{"appointment_booking": true, "medical_records": true, "payment_integration": true, "analytics": true, "multi_location": false}'::jsonb, true),
('ENTERPRISE', 'Enterprise plan for large hospitals', 9999.00, 100, 5000, '{"appointment_booking": true, "medical_records": true, "payment_integration": true, "analytics": true, "multi_location": true, "custom_branding": true}'::jsonb, true);

-- Insert sample specializations
INSERT INTO specializations (name, description, is_active) VALUES
('Cardiology', 'Heart and cardiovascular system', true),
('Dermatology', 'Skin, hair, and nails', true),
('Pediatrics', 'Medical care of infants, children, and adolescents', true),
('Orthopedics', 'Musculoskeletal system', true),
('Neurology', 'Nervous system disorders', true),
('General Medicine', 'General health and common illnesses', true),
('Gynecology', 'Women''s reproductive health', true),
('Ophthalmology', 'Eye and vision care', true),
('ENT', 'Ear, Nose, and Throat', true),
('Psychiatry', 'Mental health', true);

-- Insert sample super admin user (password: Admin@123 - hashed with BCrypt)
-- Note: In production, hash passwords using BCrypt with strength 10-12
INSERT INTO users (email, password_hash, full_name, phone_number, role, is_active, is_email_verified) VALUES
('admin@healthcare.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Super Admin', '+919876543210', 'SUPER_ADMIN', true, true);

-- Insert sample hospital admin user (password: Admin@123)
INSERT INTO users (email, password_hash, full_name, phone_number, role, is_active, is_email_verified) VALUES
('admin@cityhospital.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'City Hospital Admin', '+919876543211', 'HOSPITAL_ADMIN', true, true);

-- Insert sample hospital
INSERT INTO hospitals (hospital_name, registration_number, email, phone_number, address, city, state, country, postal_code, website, admin_user_id, is_active, is_verified) VALUES
('City General Hospital', 'REG-2024-001', 'contact@cityhospital.com', '+919876543211', '123 Main Street, Medical District', 'Mumbai', 'Maharashtra', 'India', '400001', 'https://cityhospital.com', 2, true, true);

-- Insert hospital subscription
INSERT INTO hospital_subscriptions (hospital_id, subscription_plan_id, start_date, end_date, status, auto_renew, payment_status) VALUES
(1, 2, CURRENT_DATE, CURRENT_DATE + INTERVAL '1 year', 'ACTIVE', true, 'PAID');

-- Insert sample doctor user (password: Doctor@123)
INSERT INTO users (email, password_hash, full_name, phone_number, role, is_active, is_email_verified) VALUES
('dr.sharma@cityhospital.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Dr. Rajesh Sharma', '+919876543212', 'DOCTOR', true, true);

-- Insert doctor profile
INSERT INTO doctors (hospital_id, user_id, specialization_id, license_number, qualification, experience_years, consultation_fee, bio, is_available, is_verified) VALUES
(1, 3, 1, 'MED-2015-12345', 'MBBS, MD (Cardiology)', 10, 1500.00, 'Experienced cardiologist specializing in heart disease prevention and treatment', true, true);

-- Insert sample patient user (password: Patient@123)
INSERT INTO users (email, password_hash, full_name, phone_number, role, is_active, is_email_verified) VALUES
('john.doe@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'John Doe', '+919876543213', 'PATIENT', true, true);

-- Insert patient profile
INSERT INTO patients (hospital_id, user_id, date_of_birth, gender, blood_group, address, emergency_contact_name, emergency_contact_phone, medical_history, allergies) VALUES
(1, 4, '1985-05-15', 'MALE', 'O+', '456 Park Avenue, Mumbai', 'Jane Doe', '+919876543214', 'No major medical history', 'Penicillin');

-- Insert sample appointment slots for next 7 days
INSERT INTO appointment_slots (hospital_id, doctor_id, slot_date, start_time, end_time, is_available, max_appointments, booked_appointments) VALUES
(1, 1, CURRENT_DATE, '09:00:00', '09:30:00', true, 1, 0),
(1, 1, CURRENT_DATE, '09:30:00', '10:00:00', true, 1, 0),
(1, 1, CURRENT_DATE, '10:00:00', '10:30:00', true, 1, 0),
(1, 1, CURRENT_DATE, '10:30:00', '11:00:00', true, 1, 0),
(1, 1, CURRENT_DATE, '11:00:00', '11:30:00', true, 1, 0),
(1, 1, CURRENT_DATE + 1, '09:00:00', '09:30:00', true, 1, 0),
(1, 1, CURRENT_DATE + 1, '09:30:00', '10:00:00', true, 1, 0),
(1, 1, CURRENT_DATE + 1, '10:00:00', '10:30:00', true, 1, 0);

-- Insert sample appointment
INSERT INTO appointments (hospital_id, appointment_slot_id, doctor_id, patient_id, appointment_date, appointment_time, status, patient_notes, consultation_type) VALUES
(1, 1, 1, 1, CURRENT_DATE, '09:00:00', 'SCHEDULED', 'Chest pain and irregular heartbeat', 'IN_PERSON');

-- Display inserted data summary
SELECT 'Data seeding completed successfully!' as message;

SELECT 
    'Users' as entity, COUNT(*) as count FROM users
UNION ALL
SELECT 'Subscription Plans', COUNT(*) FROM subscription_plans
UNION ALL
SELECT 'Specializations', COUNT(*) FROM specializations
UNION ALL
SELECT 'Hospitals', COUNT(*) FROM hospitals
UNION ALL
SELECT 'Hospital Subscriptions', COUNT(*) FROM hospital_subscriptions
UNION ALL
SELECT 'Doctors', COUNT(*) FROM doctors
UNION ALL
SELECT 'Patients', COUNT(*) FROM patients
UNION ALL
SELECT 'Appointment Slots', COUNT(*) FROM appointment_slots
UNION ALL
SELECT 'Appointments', COUNT(*) FROM appointments;

-- Display sample login credentials
SELECT 
    'Sample Login Credentials (Password for all: respective role + @123)' as info,
    '' as email,
    '' as role
UNION ALL
SELECT '', 'admin@healthcare.com', 'SUPER_ADMIN'
UNION ALL
SELECT '', 'admin@cityhospital.com', 'HOSPITAL_ADMIN'
UNION ALL
SELECT '', 'dr.sharma@cityhospital.com', 'DOCTOR'
UNION ALL
SELECT '', 'john.doe@email.com', 'PATIENT';
