package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.constants.AppointmentStatus;
import com.airtribe.meditrack.util.IdGenerator;
import com.airtribe.meditrack.util.Validator;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment extends MedicalEntity implements Cloneable {
    private final int doctorId;
    private final int patientId;
    private AppointmentStatus status;
    private final LocalDate appointmentDate;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public Appointment(int doctorId, int patientId, LocalDate appointmentDate,
                       LocalTime startTime, LocalTime endTime) {
        this(IdGenerator.getInstance().nextAppointmentId(), doctorId, patientId, appointmentDate,
                startTime, endTime, AppointmentStatus.PENDING);
    }

    private Appointment(int id, int doctorId, int patientId, LocalDate appointmentDate,
                        LocalTime startTime, LocalTime endTime, AppointmentStatus status) {
        super(id);
        if (doctorId <= 0 || patientId <= 0) {
            throw new IllegalArgumentException("Doctor and patient ids must be positive.");
        }
        Validator.requireValidAppointmentPeriod(appointmentDate, startTime, endTime);
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.appointmentDate = appointmentDate;
        this.startTime = startTime;
        this.endTime = endTime;
        if (status == null) {
            throw new IllegalArgumentException("Appointment status must not be null.");
        }
        this.status = status;
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
        if (status == null) {
            throw new IllegalArgumentException("Appointment status must not be null.");
        }
        this.status = status;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    @Override
    public String getDisplayName() {
        return "Appointment " + getId() + " on " + appointmentDate + " at " + startTime;
    }

    @Override
    public Appointment clone() {
        return new Appointment(getId(), doctorId, patientId, appointmentDate, startTime, endTime, status);
    }

    @Override
    public String toString() {
        return getDisplayName() + " - " + status;
    }
}
