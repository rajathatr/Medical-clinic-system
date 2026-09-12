package com.airtribe.meditrack.util;

import java.util.concurrent.atomic.AtomicInteger;

public final class IdGenerator {
    private static final AtomicInteger DOCTOR_COUNTER = new AtomicInteger(0);
    private static final AtomicInteger PATIENT_COUNTER = new AtomicInteger(0);
    private static final AtomicInteger APPOINTMENT_COUNTER = new AtomicInteger(0);
    private static final AtomicInteger BILL_COUNTER = new AtomicInteger(0);
    private static final IdGenerator INSTANCE = new IdGenerator();

    private IdGenerator() {
    }

    public static IdGenerator getInstance() {
        return INSTANCE;
    }

    public int nextDoctorId() {
        return DOCTOR_COUNTER.incrementAndGet();
    }

    public int nextPatientId() {
        return PATIENT_COUNTER.incrementAndGet();
    }

    public int nextAppointmentId() {
        return APPOINTMENT_COUNTER.incrementAndGet();
    }

    public int nextBillId() {
        return BILL_COUNTER.incrementAndGet();
    }
}
