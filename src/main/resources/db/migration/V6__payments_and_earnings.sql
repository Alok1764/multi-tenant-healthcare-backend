-- V6: Payments and Platform Earnings
-- Description: Payment transactions and platform commission tracking

-- Payments table (multi-tenant)
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    hospital_id BIGINT NOT NULL,
    appointment_id BIGINT,
    patient_id BIGINT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL CHECK (amount >= 0),
    payment_type VARCHAR(50) NOT NULL CHECK (payment_type IN ('CONSULTATION', 'SUBSCRIPTION', 'OTHER')),
    payment_method VARCHAR(50) CHECK (payment_method IN ('CASH', 'CARD', 'UPI', 'NET_BANKING', 'WALLET')),
    payment_status VARCHAR(50) NOT NULL CHECK (payment_status IN ('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED')),
    transaction_id VARCHAR(255) UNIQUE,
    payment_gateway VARCHAR(100),
    payment_gateway_response JSONB, -- Store gateway response as JSON
    paid_at TIMESTAMP,
    refunded_at TIMESTAMP,
    refund_reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (hospital_id) REFERENCES hospitals(id) ON DELETE CASCADE,
    FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE SET NULL,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE RESTRICT
);

-- Create indexes
CREATE INDEX idx_payments_hospital_id ON payments(hospital_id);
CREATE INDEX idx_payments_appointment_id ON payments(appointment_id);
CREATE INDEX idx_payments_patient_id ON payments(patient_id);
CREATE INDEX idx_payments_status ON payments(payment_status);
CREATE INDEX idx_payments_type ON payments(payment_type);
CREATE INDEX idx_payments_transaction_id ON payments(transaction_id);
CREATE INDEX idx_payments_created_at ON payments(created_at);

-- Trigger for payments table
CREATE TRIGGER update_payments_updated_at
    BEFORE UPDATE ON payments
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Platform earnings table (multi-tenant)
CREATE TABLE platform_earnings (
    id BIGSERIAL PRIMARY KEY,
    hospital_id BIGINT NOT NULL,
    payment_id BIGINT NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL CHECK (total_amount >= 0),
    commission_percentage DECIMAL(5, 2) NOT NULL CHECK (commission_percentage >= 0 AND commission_percentage <= 100),
    commission_amount DECIMAL(10, 2) NOT NULL CHECK (commission_amount >= 0),
    hospital_amount DECIMAL(10, 2) NOT NULL CHECK (hospital_amount >= 0),
    settlement_status VARCHAR(50) NOT NULL CHECK (settlement_status IN ('PENDING', 'SETTLED', 'ON_HOLD')),
    settled_at TIMESTAMP,
    settlement_reference VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (hospital_id) REFERENCES hospitals(id) ON DELETE CASCADE,
    FOREIGN KEY (payment_id) REFERENCES payments(id) ON DELETE RESTRICT,
    CONSTRAINT check_amounts_sum CHECK (commission_amount + hospital_amount = total_amount)
);

-- Create indexes
CREATE INDEX idx_platform_earnings_hospital_id ON platform_earnings(hospital_id);
CREATE INDEX idx_platform_earnings_payment_id ON platform_earnings(payment_id);
CREATE INDEX idx_platform_earnings_status ON platform_earnings(settlement_status);
CREATE INDEX idx_platform_earnings_created_at ON platform_earnings(created_at);

-- Trigger for platform_earnings table
CREATE TRIGGER update_platform_earnings_updated_at
    BEFORE UPDATE ON platform_earnings
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();
