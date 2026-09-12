package com.airtribe.meditrack.exception;

public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException(int appointmentId) {
        super("Appointment not found for id: " + appointmentId);
    }
}
