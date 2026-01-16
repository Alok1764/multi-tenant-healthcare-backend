-- Quick Setup Script for Healthcare Database
-- Run this script to create all tables in order

-- First, create the database (run this separately if needed)
-- CREATE DATABASE healthcare_db;

-- Connect to the database
\c healthcare_db;

-- Execute migrations in order
\i V1__users_and_authentication.sql
\i V2__hospitals_and_subscriptions.sql
\i V3__doctors_and_patients.sql
\i V4__appointments_and_slots.sql
\i V5__medical_records.sql
\i V6__payments_and_earnings.sql

-- Verify tables were created
SELECT table_name 
FROM information_schema.tables 
WHERE table_schema = 'public' 
ORDER BY table_name;

-- Show table counts
SELECT 
    'users' as table_name, COUNT(*) as row_count FROM users
UNION ALL
SELECT 'refresh_tokens', COUNT(*) FROM refresh_tokens
UNION ALL
SELECT 'subscription_plans', COUNT(*) FROM subscription_plans
UNION ALL
SELECT 'hospitals', COUNT(*) FROM hospitals
UNION ALL
SELECT 'hospital_subscriptions', COUNT(*) FROM hospital_subscriptions
UNION ALL
SELECT 'specializations', COUNT(*) FROM specializations
UNION ALL
SELECT 'doctors', COUNT(*) FROM doctors
UNION ALL
SELECT 'patients', COUNT(*) FROM patients
UNION ALL
SELECT 'appointment_slots', COUNT(*) FROM appointment_slots
UNION ALL
SELECT 'appointments', COUNT(*) FROM appointments
UNION ALL
SELECT 'medical_records', COUNT(*) FROM medical_records
UNION ALL
SELECT 'payments', COUNT(*) FROM payments
UNION ALL
SELECT 'platform_earnings', COUNT(*) FROM platform_earnings;
