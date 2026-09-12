package com.airtribe.meditrack.service;

import com.airtribe.meditrack.constants.AppointmentStatus;
import com.airtribe.meditrack.constants.Specialization;
import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Doctor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Stream-based reporting and analytics for the MediTrack console application. */
public class AnalyticsService {
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    public AnalyticsService(DoctorService doctorService, AppointmentService appointmentService) {
        if (doctorService == null || appointmentService == null) {
            throw new IllegalArgumentException("Doctor and appointment services must not be null.");
        }
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
    }

    /** Filters doctors by their enumerated specialization. */
    public List<Doctor> filterDoctorsBySpecialization(Specialization specialization) {
        return doctorService.getAllDoctors().stream()
                .filter(doctor -> doctor.getSpecialization() == specialization)
                .toList();
    }

    /** Returns the average consultation fee, rounded to two decimal places. */
    public Optional<BigDecimal> calculateAverageConsultationFee() {
        List<BigDecimal> fees = doctorService.getAllDoctors().stream()
                .map(Doctor::getConsultationFee)
                .toList();
        if (fees.isEmpty()) {
            return Optional.empty();
        }
        BigDecimal total = fees.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return Optional.of(total.divide(BigDecimal.valueOf(fees.size()), 2, RoundingMode.HALF_UP));
    }

    /** Counts all recorded appointments per registered doctor, including cancelled history. */
    public Map<Doctor, Long> countAppointmentsPerDoctor() {
        Map<Integer, Long> countsByDoctorId = appointmentService.getAllAppointments().stream()
                .collect(Collectors.groupingBy(Appointment::getDoctorId, Collectors.counting()));

        return doctorService.getAllDoctors().stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        doctor -> countsByDoctorId.getOrDefault(doctor.getId(), 0L),
                        (first, ignored) -> first,
                        LinkedHashMap::new));
    }

    /** Lists confirmed future appointments from the supplied date, earliest first. */
    public List<Appointment> getUpcomingConfirmedAppointments(LocalDate fromDate) {
        if (fromDate == null) {
            throw new IllegalArgumentException("Start date must not be null.");
        }
        return appointmentService.getAllAppointments().stream()
                .filter(appointment -> appointment.getStatus() == AppointmentStatus.CONFIRMED)
                .filter(appointment -> !appointment.getAppointmentDate().isBefore(fromDate))
                .sorted(Comparator.comparing(Appointment::getAppointmentDate)
                        .thenComparing(Appointment::getStartTime))
                .toList();
    }

    /** Finds the doctor with the largest recorded appointment count. */
    public Optional<Doctor> findMostBookedDoctor() {
        return countAppointmentsPerDoctor().entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }
}
