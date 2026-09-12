package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.constants.AppointmentStatus;

import java.time.LocalTime;

public class Appointment {
    private final int id;
    private final int doctorId;
    private final int patientId;
    private AppointmentStatus status;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public Appointment(int doctorId, int patientId, LocalTime startTime, LocalTime endTime) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = AppointmentStatus.PENDING;
        id = 0;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public int getPatientId() {
        return patientId;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getId() {
        return id;
    }
}
