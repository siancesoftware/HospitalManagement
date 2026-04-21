package com.siance.hm.kafka.event;

public final class KafkaTopics {
    private KafkaTopics() {}

    // Patient events
    public static final String PATIENT_REGISTERED = "hm.patient.registered";
    public static final String PATIENT_UPDATED = "hm.patient.updated";
    public static final String PATIENT_VISIT_CREATED = "hm.patient.visit-created";

    // Appointment events
    public static final String APPOINTMENT_BOOKED = "hm.appointment.booked";
    public static final String APPOINTMENT_CANCELLED = "hm.appointment.cancelled";
    public static final String APPOINTMENT_REMINDER = "hm.appointment.reminder";

    // OPD events
    public static final String OPD_CONSULTATION_COMPLETED = "hm.opd.consultation-completed";
    public static final String OPD_PRESCRIPTION_CREATED = "hm.opd.prescription-created";
    public static final String OPD_INVESTIGATION_ORDERED = "hm.opd.investigation-ordered";

    // IPD events
    public static final String IPD_PATIENT_ADMITTED = "hm.ipd.patient-admitted";
    public static final String IPD_PATIENT_DISCHARGED = "hm.ipd.patient-discharged";
    public static final String IPD_BED_TRANSFERRED = "hm.ipd.bed-transferred";

    // Lab events
    public static final String LAB_ORDER_RECEIVED = "hm.lab.order-received";
    public static final String LAB_SAMPLE_COLLECTED = "hm.lab.sample-collected";
    public static final String LAB_RESULT_VALIDATED = "hm.lab.result-validated";
    public static final String LAB_CRITICAL_ALERT = "hm.lab.critical-alert";

    // Pharmacy events
    public static final String PHARMACY_DISPENSED = "hm.pharmacy.dispensed";

    // Billing events
    public static final String BILLING_INVOICE_CREATED = "hm.billing.invoice-created";
    public static final String BILLING_PAYMENT_RECEIVED = "hm.billing.payment-received";

    // Notification events
    public static final String NOTIFICATION_SEND = "hm.notification.send";
}
