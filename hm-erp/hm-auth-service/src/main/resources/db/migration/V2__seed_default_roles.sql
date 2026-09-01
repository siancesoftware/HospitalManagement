-- Default role catalogue. The original project seeds roles via prisma/seed.ts
-- at deploy time rather than via SQL migration; this is the Flyway-native
-- equivalent so a fresh environment is usable immediately. If your
-- prisma/seed.ts defines additional/different role codes, add them here too.

INSERT INTO roles (id, code, name, description, is_system, created_at, updated_at)
VALUES (gen_random_uuid(), 'SUPER_ADMIN', 'Super Administrator', 'Full platform access across all hospitals.', TRUE, now(), now()),
       (gen_random_uuid(), 'USER', 'Platform User', 'Default role for a self-registered platform account.', TRUE, now(), now()),
       (gen_random_uuid(), 'HOSPITAL_ADMIN', 'Hospital Administrator', 'Owns and administers a single hospital tenant.', TRUE, now(), now()),
       (gen_random_uuid(), 'DOCTOR', 'Doctor', 'Consults, prescribes, and orders investigations.', TRUE, now(), now()),
       (gen_random_uuid(), 'NURSE', 'Nurse', 'Ward/IPD clinical staff.', TRUE, now(), now()),
       (gen_random_uuid(), 'RECEPTIONIST', 'Receptionist', 'Front-desk registration and scheduling.', TRUE, now(), now()),
       (gen_random_uuid(), 'PHARMACIST', 'Pharmacist', 'Dispenses medicines and manages pharmacy stock.', TRUE, now(), now()),
       (gen_random_uuid(), 'LAB_TECHNICIAN', 'Lab Technician', 'Collects samples and records lab results.', TRUE, now(), now()),
       (gen_random_uuid(), 'ACCOUNTANT', 'Accountant', 'Billing, payments, and financial reporting.', TRUE, now(), now()),
       (gen_random_uuid(), 'STORE_MANAGER', 'Store Manager', 'Inventory and procurement.', TRUE, now(), now())
ON CONFLICT (code) DO NOTHING;
