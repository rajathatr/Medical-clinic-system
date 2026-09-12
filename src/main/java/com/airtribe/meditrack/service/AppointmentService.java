package com.airtribe.meditrack.service;

import com.airtribe.meditrack.constants.AppointmentStatus;
import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.Validator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

/** Creates, views, and cancels clinic appointments. */
public class AppointmentService {
    private final DataStore<Appointment> appointmentStore = new DataStore<>();
    private final DoctorService doctorService;
    private final PatientService patientService;

    public AppointmentService(DoctorService doctorService, PatientService patientService) {
        if (doctorService == null || patientService == null) {
            throw new IllegalArgumentException("Doctor and patient services must not be null.");
        }
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    public Appointment createAppointment(int doctorId, int patientId, LocalDate appointmentDate,
                                         LocalTime startTime, LocalTime endTime) {
        Validator.requireValidAppointmentPeriod(appointmentDate, startTime, endTime);
        doctorService.getDoctorById(doctorId);
        patientService.getPatientById(patientId);

        if (hasScheduleConflict(doctorId, appointmentDate, startTime, endTime, true)) {
            throw new InvalidDataException("Doctor already has an appointment in this time slot.");
        }
        if (hasScheduleConflict(patientId, appointmentDate, startTime, endTime, false)) {
            throw new InvalidDataException("Patient already has an appointment in this time slot.");
        }

        Appointment appointment = new Appointment(doctorId, patientId, appointmentDate, startTime, endTime);
        appointmentStore.add(appointment);
        patientService.getPatientById(patientId).addAppointmentToHistory(appointment.getId());
        return appointment;
    }

    public Appointment getAppointmentById(int appointmentId) {
        return appointmentStore.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));
    }

    public List<Appointment> getAllAppointments() {
        return sortAppointments(appointmentStore.findAll());
    }

    public List<Appointment> getAppointmentsForDoctor(int doctorId) {
        return sortAppointments(appointmentStore.findAll().stream()
                .filter(appointment -> appointment.getDoctorId() == doctorId)
                .toList());
    }

    public List<Appointment> getAppointmentsForPatient(int patientId) {
        return sortAppointments(appointmentStore.findAll().stream()
                .filter(appointment -> appointment.getPatientId() == patientId)
                .toList());
    }

    public void cancelAppointment(int appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new InvalidDataException("A completed appointment cannot be cancelled.");
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);
    }

    public void markAppointmentConfirmed(int appointmentId) {
        getAppointmentById(appointmentId).setStatus(AppointmentStatus.CONFIRMED);
    }

    private boolean hasScheduleConflict(int participantId, LocalDate appointmentDate,
                                        LocalTime startTime, LocalTime endTime, boolean isDoctor) {
        return appointmentStore.findAll().stream()
                .filter(appointment -> appointment.getStatus() != AppointmentStatus.CANCELLED)
                .filter(appointment -> appointment.getAppointmentDate().equals(appointmentDate))
                .filter(appointment -> isDoctor
                        ? appointment.getDoctorId() == participantId
                        : appointment.getPatientId() == participantId)
                .anyMatch(appointment -> appointment.getStartTime().isBefore(endTime)
                        && startTime.isBefore(appointment.getEndTime()));
    }

    private List<Appointment> sortAppointments(List<Appointment> appointments) {
        return appointments.stream()
                .sorted(Comparator.comparing(Appointment::getAppointmentDate)
                        .thenComparing(Appointment::getStartTime))
                .toList();
    }
}
