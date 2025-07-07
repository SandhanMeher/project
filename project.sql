-- ===========================
-- DROP and CREATE DATABASE
-- ===========================
DROP DATABASE IF EXISTS healthsure;
CREATE DATABASE healthsure;
USE healthsure;

-- ===========================
-- 1. Providers
-- ===========================
CREATE TABLE Providers (
  provider_id VARCHAR(20) PRIMARY KEY,
  provider_name VARCHAR(100) NOT NULL,
  hospital_name VARCHAR(100) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  address VARCHAR(225) NOT NULL,
  city VARCHAR(225) NOT NULL,
  state VARCHAR(225) NOT NULL,
  zip_code VARCHAR(20) NOT NULL,
  status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===========================
-- 2. Doctors
-- ===========================
CREATE TABLE Doctors (
  doctor_id VARCHAR(20) PRIMARY KEY,
  provider_id VARCHAR(20),
  doctor_name VARCHAR(100) NOT NULL,
  qualification VARCHAR(255),
  specialization VARCHAR(100),
  license_no VARCHAR(50) UNIQUE NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  address VARCHAR(225) NOT NULL,
  gender VARCHAR(10),
  password VARCHAR(255) NOT NULL,
  login_status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
  doctor_status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'INACTIVE',
  FOREIGN KEY (provider_id) REFERENCES Providers(provider_id)
);

-- ===========================
-- 3. Doctor Availability
-- ===========================
CREATE TABLE Doctor_availability (
  availability_id VARCHAR(36) PRIMARY KEY,
  doctor_id VARCHAR(20) NOT NULL,
  available_date DATE NOT NULL,
  start_time TIME NOT NULL,
  end_time TIME NOT NULL,
  slot_type ENUM('STANDARD', 'ADHOC') DEFAULT 'STANDARD',
  max_capacity INT NOT NULL,
  patient_window INT GENERATED ALWAYS AS (
    TIMESTAMPDIFF(MINUTE, start_time, end_time) / max_capacity
  ) STORED,
  is_recurring BOOLEAN DEFAULT FALSE,
  notes VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id)
);

-- ===========================
-- 4. Recipient
-- ===========================
CREATE TABLE Recipient (
  h_id VARCHAR(20) PRIMARY KEY,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  mobile VARCHAR(10) UNIQUE NOT NULL,
  user_name VARCHAR(100) UNIQUE NOT NULL,
  gender ENUM('MALE', 'FEMALE') NOT NULL,
  dob DATE NOT NULL,
  address VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(150) UNIQUE NOT NULL,
  status ENUM('ACTIVE', 'INACTIVE', 'BLOCKED') DEFAULT 'ACTIVE',
  login_attempts INT DEFAULT 0,
  locked_until DATETIME DEFAULT NULL,
  last_login DATETIME DEFAULT NULL,
  password_updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ===========================
-- 5. Appointment
-- ===========================
CREATE TABLE Appointment (
  appointment_id VARCHAR(36) PRIMARY KEY,
  doctor_id VARCHAR(20) NOT NULL,
  h_id VARCHAR(20) NOT NULL,
  availability_id VARCHAR(36) NOT NULL,
  provider_id VARCHAR(20) NOT NULL,
  requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  booked_at TIMESTAMP NULL,
  cancelled_at TIMESTAMP NULL,
  completed_at TIMESTAMP NULL,
  status ENUM('PENDING', 'BOOKED', 'CANCELLED', 'COMPLETED') DEFAULT 'PENDING',
  slot_no INT NOT NULL,
  start TIMESTAMP,
  end TIMESTAMP,
  notes TEXT,
  FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id),
  FOREIGN KEY (h_id) REFERENCES Recipient(h_id),
  FOREIGN KEY (availability_id) REFERENCES Doctor_availability(availability_id),
  FOREIGN KEY (provider_id) REFERENCES Providers(provider_id)
);

-- ===========================
-- Sample Data
-- ===========================

-- Providers
INSERT INTO Providers (provider_id, provider_name, hospital_name, email, address, city, state, zip_code, status)
VALUES
('P1003','Dr. Alok Mehta','AIIMS','alok.m@aiims.edu','AIIMS Campus','Delhi','Delhi','110029','APPROVED'),
('P1005','Dr. Arjun Desai','Max Healthcare','arjun.d@max.com','Max Complex','Noida','Uttar Pradesh','201301','APPROVED');

-- Doctors
INSERT INTO Doctors (doctor_id, provider_id, doctor_name, qualification, specialization, license_no, email, address, gender, password, login_status, doctor_status)
VALUES
('D1003','P1003','Dr. Alok Nair','MBBS, DM','Neurology','LIC1003','alok.n@aiims.edu','South Campus, Delhi','MALE','pass123','APPROVED','ACTIVE'),
('D1005','P1005','Dr. Arjun Rao','MBBS, MS','Orthopedics','LIC1005','arjun.r@max.com','Sector 18, Noida','MALE','pass123','APPROVED','ACTIVE');

-- Recipients
INSERT INTO Recipient (h_id, first_name, last_name, mobile, user_name, gender, dob, address, password, email)
VALUES
('H1003','Robert','Brown','9876543212','robert.brown','MALE','1988-03-03','789 Pine St','pass123','robert.brown@example.com'),
('H1004','Emily','Davis','9876543213','emily.davis','FEMALE','1995-04-04','234 Oak St','pass123','emily.davis@example.com');

-- Doctor Availability
INSERT INTO Doctor_availability (availability_id, doctor_id, available_date, start_time, end_time, slot_type, max_capacity, is_recurring, notes)
VALUES
('A2001','D1003','2025-07-07','10:00:00','11:00:00','STANDARD',4,FALSE,'Morning Neurology Check'),
('A2002','D1005','2025-07-07','14:00:00','15:30:00','ADHOC',3,FALSE,'Afternoon Orthopedics Check');

-- Appointments for A2001
INSERT INTO Appointment (appointment_id, doctor_id, h_id, availability_id, provider_id, booked_at, status, slot_no, start, end, notes)
VALUES
('APPT101','D1003','H1003','A2001','P1003', NOW(), 'BOOKED', 1, '2025-07-07 10:00:00', '2025-07-07 10:15:00', 'Initial Consultation'),
('APPT102','D1003','H1004','A2001','P1003', NOW(), 'BOOKED', 2, '2025-07-07 10:15:00', '2025-07-07 10:30:00', 'Follow-up Visit'),
('APPT103','D1003','H1003','A2001','P1003', NOW(), 'PENDING', 3, '2025-07-07 10:30:00', '2025-07-07 10:45:00', 'MRI Discussion'),
('APPT104','D1003','H1004','A2001','P1003', NOW(), 'BOOKED', 4, '2025-07-07 10:45:00', '2025-07-07 11:00:00', 'Test Result Review');

-- Appointments for A2002
INSERT INTO Appointment (appointment_id, doctor_id, h_id, availability_id, provider_id, booked_at, status, slot_no, start, end, notes)
VALUES
('APPT201','D1005','H1004','A2002','P1005', NOW(), 'BOOKED', 1, '2025-07-07 14:00:00', '2025-07-07 14:30:00', 'Knee Checkup'),
('APPT202','D1005','H1003','A2002','P1005', NOW(), 'PENDING', 2, '2025-07-07 14:30:00', '2025-07-07 15:00:00', 'Shoulder Pain'),
('APPT203','D1005','H1004','A2002','P1005', NOW(), 'BOOKED', 3, '2025-07-07 15:00:00', '2025-07-07 15:30:00', 'Fracture Follow-up');

-- More Availabilities for D1003
INSERT INTO Doctor_availability (
    availability_id, doctor_id, available_date, start_time, end_time, 
    slot_type, max_capacity, is_recurring, notes
) VALUES
-- July 8: Morning & Afternoon
('A2010','D1003','2025-07-08','09:00:00','10:00:00','STANDARD',4,FALSE,'Morning Neuro Check'),
('A2011','D1003','2025-07-08','15:00:00','16:00:00','ADHOC',3,FALSE,'Evening Review'),

-- July 9: One session
('A2012','D1003','2025-07-09','10:30:00','11:30:00','STANDARD',4,FALSE,'MRI Consult'),

-- July 10: Morning & Afternoon
('A2013','D1003','2025-07-10','08:00:00','09:00:00','STANDARD',4,FALSE,'Early Neuro Slot'),
('A2014','D1003','2025-07-10','13:00:00','14:00:00','ADHOC',3,FALSE,'Follow-up Discussion'),

-- July 11: One session
('A2015','D1003','2025-07-11','11:00:00','12:00:00','STANDARD',4,FALSE,'Post-Treatment Check'),

-- July 12: Already exists as A2009

-- July 13: Evening slot
('A2016','D1003','2025-07-13','17:00:00','18:00:00','ADHOC',2,FALSE,'Late Slot'),

-- July 14: Two sessions
('A2017','D1003','2025-07-14','09:00:00','10:00:00','STANDARD',4,FALSE,'Morning Appointments'),
('A2018','D1003','2025-07-14','16:00:00','17:00:00','STANDARD',3,FALSE,'Late Neuro Review');

