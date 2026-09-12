package com.airtribe.meditrack.exception;

public class AppointmentNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AppointmentNotFoundException(int appointmentId) {
        super("Appointment not found for id: " + appointmentId);
    }
}
