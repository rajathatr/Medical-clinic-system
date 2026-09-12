package com.airtribe.meditrack;

import com.airtribe.meditrack.constants.Specialization;
import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/** Manual smoke tests for the service layer. Run with assertions enabled. */
public final class TestRunner {
    private TestRunner() {
    }

    public static void main(String[] args) {
        DoctorService doctorService = new DoctorService();
        PatientService patientService = new PatientService();
        AppointmentService appointmentService = new AppointmentService(doctorService, patientService);

        Doctor doctor = doctorService.createDoctor(
                "Asha Rao", 42, "asha", "asha@example.com", "password",
                Specialization.CARDIOLOGY, new BigDecimal("850.00"));
        Patient patient = patientService.createPatient(
                "Dev Kumar", 28, "dev", "dev@example.com", "password", "Fever");

        assert doctor.getId() == 1 : "The first doctor id should be 1.";
        assert patient.getId() == 1 : "The first patient id should be 1.";
        assert patientService.searchPatient("dev").size() == 1 : "Name search should find the patient.";
        assert patientService.searchPatient(28).size() == 1 : "Age search should find the patient.";
        assert patientService.searchPatient(Integer.valueOf(1)).isPresent()
                : "Id search should find the patient.";

        Appointment appointment = appointmentService.createAppointment(
                doctor.getId(), patient.getId(), LocalDate.of(2026, 9, 15),
                LocalTime.of(9, 0), LocalTime.of(9, 30));

        assert appointment.getId() == 1 : "The first appointment id should be 1.";
        assert patient.getAppointmentHistory().equals(java.util.List.of(appointment.getId()))
                : "Creating an appointment should update the patient history.";

        expectInvalidData(() -> appointmentService.createAppointment(
                doctor.getId(), patient.getId(), LocalDate.of(2026, 9, 15),
                LocalTime.of(9, 15), LocalTime.of(9, 45)));

        appointmentService.cancelAppointment(appointment.getId());
        Appointment rescheduledAppointment = appointmentService.createAppointment(
                doctor.getId(), patient.getId(), LocalDate.of(2026, 9, 15),
                LocalTime.of(9, 15), LocalTime.of(9, 45));
        assert rescheduledAppointment.getId() == 2
                : "Validation should reject a conflicting appointment before allocating its id.";

        System.out.println("All Step 3 service tests passed.");
    }

    private static void expectInvalidData(Runnable action) {
        try {
            action.run();
            throw new AssertionError("Expected InvalidDataException.");
        } catch (InvalidDataException expected) {
            // Expected result.
            System.out.println(expected.getMessage());
        }
    }
}
