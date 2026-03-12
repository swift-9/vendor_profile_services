-- Flyway V1 schema for public-vendor-profile

CREATE DATABASE IF NOT EXISTS public_vendor_profile;
USE public_vendor_profile;

-- auth table
CREATE TABLE IF NOT EXISTS auth (
  auth_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS password_reset_tokens (
  token_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  auth_id BIGINT NOT NULL,
  token VARCHAR(255) NOT NULL,
  expiry_time DATETIME,
  CONSTRAINT fk_prt_auth FOREIGN KEY (auth_id) REFERENCES auth (auth_id)
);

CREATE TABLE IF NOT EXISTS user_profile (
  user_profile_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  auth_id BIGINT NOT NULL,
  full_name VARCHAR(255),
  email VARCHAR(255),
  phone_number VARCHAR(50),
  age INT,
  gender VARCHAR(20),
  CONSTRAINT fk_user_auth FOREIGN KEY (auth_id) REFERENCES auth (auth_id)
);

CREATE TABLE IF NOT EXISTS vendor_profile (
  vendor_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  auth_id BIGINT NOT NULL,
  full_name VARCHAR(255),
  phone_number VARCHAR(50),
  age INT,
  blood_group VARCHAR(20),
  gender VARCHAR(20),
  address_line VARCHAR(500),
  city VARCHAR(100),
  state VARCHAR(100),
  country VARCHAR(100),
  pincode VARCHAR(20),
  photo_path VARCHAR(500),
  isVerified VARCHAR(50),
  CONSTRAINT fk_vendor_auth FOREIGN KEY (auth_id) REFERENCES auth (auth_id)
);

CREATE TABLE IF NOT EXISTS vendor_work (
  vendor_work_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  vendor_id BIGINT NOT NULL,
  work_type VARCHAR(50),
  agency_name VARCHAR(255),
  experience_years INT,
  service_category_id BIGINT,
  CONSTRAINT fk_vendor_work_vendor FOREIGN KEY (vendor_id) REFERENCES vendor_profile (vendor_id)
);

CREATE TABLE IF NOT EXISTS vendor_work_sub_service_map (
  vendor_work_id BIGINT NOT NULL,
  sub_service_id BIGINT NOT NULL,
  PRIMARY KEY (vendor_work_id, sub_service_id),
  CONSTRAINT fk_vwsm_vw FOREIGN KEY (vendor_work_id) REFERENCES vendor_work (vendor_work_id)
);

CREATE TABLE IF NOT EXISTS vendor_documents (
  vendor_doc_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  vendor_id BIGINT NOT NULL,
  id_proof_type VARCHAR(100),
  id_file_path VARCHAR(500),
  CONSTRAINT fk_vdoc_vendor FOREIGN KEY (vendor_id) REFERENCES vendor_profile (vendor_id)
);

CREATE TABLE IF NOT EXISTS vendor_bank_details (
  vendor_bank_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  vendor_id BIGINT NOT NULL,
  account_holder_name VARCHAR(255),
  account_number VARCHAR(100),
  ifsc_code VARCHAR(50),
  bank_name VARCHAR(255),
  CONSTRAINT fk_vbank_vendor FOREIGN KEY (vendor_id) REFERENCES vendor_profile (vendor_id)
);

CREATE TABLE IF NOT EXISTS services (
  service_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  service_name VARCHAR(255),
  icon_path VARCHAR(500),
  description TEXT,
  is_active BOOLEAN DEFAULT TRUE,
  created_at DATETIME,
  updated_at DATETIME
);

CREATE TABLE IF NOT EXISTS sub_services (
  sub_service_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  service_id BIGINT,
  sub_service_name VARCHAR(255),
  description TEXT,
  image_path VARCHAR(500),
  duration INT,
  is_active BOOLEAN DEFAULT TRUE,
  price DECIMAL(10,2),
  CONSTRAINT fk_sub_service_service FOREIGN KEY (service_id) REFERENCES services (service_id)
);

CREATE TABLE IF NOT EXISTS reviews (
  review_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT,
  vendor_id BIGINT,
  booking_id BIGINT,
  rating INT,
  comments TEXT,
  review_date DATE,
  review_time TIME,
  status VARCHAR(50)
);
