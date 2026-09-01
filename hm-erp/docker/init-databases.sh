#!/bin/sh
set -e

echo "Creating multiple databases..."

DATABASES="patient_db appointment_db opd_db billing_db master_db notification_db keycloak_db"

for DB in $DATABASES; do
    echo "Creating database: $DB"
    if ! psql -U "$POSTGRES_USER" -tc "SELECT 1 FROM pg_database WHERE datname = '$DB'" | grep -q 1; then
        psql -U "$POSTGRES_USER" -c "CREATE DATABASE $DB;"
    fi
done

echo "All databases created!"
