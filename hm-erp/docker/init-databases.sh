#!/bin/bash
set -e

POSTGRES="psql --username ${POSTGRES_USER}"
echo "Creating multiple databases..."

for DB in patient_db appointment_db opd_db billing_db master_db notification_db keycloak_db ipd_db lab_db radiology_db pharmacy_db nursing_db emergency_db ot_db inventory_db hr_db bloodbank_db dietary_db insurance_db quality_db analytics_db notification_db; do
    echo "  Creating database: $DB"
    $POSTGRES <<-EOSQL
        SELECT 'CREATE DATABASE $DB' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '$DB')\gexec
EOSQL
done

echo "All databases created!"
