-- V2: Hospitals and Subscription Management
-- Description: Hospital master data and subscription plan management

-- Subscription plans table (shared - platform-level)
CREATE TABLE subscription_plans (
    id BIGSERIAL PRIMARY KEY,
    plan_name VARCHAR(50) NOT NULL UNIQUE CHECK (plan_name IN ('BASIC', 'PREMIUM', 'ENTERPRISE')),
    description TEXT,
    monthly_price DECIMAL(10, 2) NOT NULL CHECK (monthly_price >= 0),
    max_doctors INTEGER NOT NULL CHECK (max_doctors > 0),
    max_patients INTEGER CHECK (max_patients > 0),
    features JSONB, -- Store plan features as JSON
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Trigger for subscription_plans table
CREATE TRIGGER update_subscription_plans_updated_at
    BEFORE UPDATE ON subscription_plans
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Hospitals table
CREATE TABLE hospitals (
    id BIGSERIAL PRIMARY KEY,
    hospital_name VARCHAR(255) NOT NULL,
    registration_number VARCHAR(100) UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL,
    address TEXT NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL DEFAULT 'India',
    postal_code VARCHAR(20),
    website VARCHAR(255),
    admin_user_id BIGINT UNIQUE, -- Link to hospital admin user
    is_active BOOLEAN NOT NULL DEFAULT true,
    is_verified BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (admin_user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Create indexes
CREATE INDEX idx_hospitals_email ON hospitals(email);
CREATE INDEX idx_hospitals_active ON hospitals(is_active);
CREATE INDEX idx_hospitals_city ON hospitals(city);

-- Trigger for hospitals table
CREATE TRIGGER update_hospitals_updated_at
    BEFORE UPDATE ON hospitals
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Hospital subscriptions table (links hospitals to their active subscription)
CREATE TABLE hospital_subscriptions (
    id BIGSERIAL PRIMARY KEY,
    hospital_id BIGINT NOT NULL,
    subscription_plan_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL CHECK (status IN ('ACTIVE', 'EXPIRED', 'CANCELLED', 'SUSPENDED')),
    auto_renew BOOLEAN NOT NULL DEFAULT true,
    payment_status VARCHAR(50) CHECK (payment_status IN ('PAID', 'PENDING', 'FAILED')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (hospital_id) REFERENCES hospitals(id) ON DELETE CASCADE,
    FOREIGN KEY (subscription_plan_id) REFERENCES subscription_plans(id) ON DELETE RESTRICT,
    CONSTRAINT check_end_date_after_start CHECK (end_date > start_date)
);

-- Create indexes
CREATE INDEX idx_hospital_subscriptions_hospital_id ON hospital_subscriptions(hospital_id);
CREATE INDEX idx_hospital_subscriptions_status ON hospital_subscriptions(status);
CREATE INDEX idx_hospital_subscriptions_end_date ON hospital_subscriptions(end_date);

-- Trigger for hospital_subscriptions table
CREATE TRIGGER update_hospital_subscriptions_updated_at
    BEFORE UPDATE ON hospital_subscriptions
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();
