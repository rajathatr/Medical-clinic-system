package com.airtribe.meditrack;

import com.airtribe.meditrack.constants.BillingType;
import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.constants.Specialization;
import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.BillSummary;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.service.AnalyticsService;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.BillingService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.util.DateUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Scanner;

/** Menu-driven console entry point for MediTrack. */
public final class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final DoctorService doctorService = new DoctorService();
    private final PatientService patientService = new PatientService();
    private final AppointmentService appointmentService = new AppointmentService(doctorService, patientService);
    private final BillingService billingService = new BillingService(appointmentService, doctorService);
    private final AnalyticsService analyticsService = new AnalyticsService(doctorService, appointmentService);

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        System.out.println("Welcome to " + Constants.APPLICATION_NAME + ".");
        boolean running = true;
        while (running) {
            printMainMenu();
            try {
                switch (readInt("Choose an option: ")) {
                    case 1 -> doctorMenu();
                    case 2 -> patientMenu();
                    case 3 -> appointmentMenu();
                    case 4 -> billingMenu();
                    case 5 -> analyticsMenu();
                    case 0 -> running = false;
                    default -> System.out.println("Please choose an option from the menu.");
                }
            } catch (IllegalArgumentException exception) {
                System.out.println("Error: " + exception.getMessage());
                if (!scanner.hasNextLine()) {
                    running = false;
                }
            }
        }
        System.out.println("Thank you for using " + Constants.APPLICATION_NAME + ".");
    }

    private void doctorMenu() {
        System.out.println("\n--- Doctor Management ---");
        System.out.println("1. Add doctor\n2. View all doctors\n3. Search doctors by name");
        System.out.println("4. Filter doctors by specialization\n5. Update doctor fee\n6. Delete doctor\n0. Back");
        switch (readInt("Choose an option: ")) {
            case 1 -> addDoctor();
            case 2 -> doctorService.getAllDoctors().forEach(System.out::println);
            case 3 -> doctorService.searchDoctorsByName(readRequired("Name to search: ")).forEach(System.out::println);
            case 4 -> doctorService.findDoctorsBySpecialization(readSpecialization()).forEach(System.out::println);
            case 5 -> updateDoctorFee();
            case 6 -> deleteDoctor();
            case 0 -> { }
            default -> System.out.println("Please choose an option from the menu.");
        }
    }

    private void addDoctor() {
        Doctor doctor = doctorService.createDoctor(readRequired("Name: "), readInt("Age: "),
                readRequired("Username: "), readRequired("Email: "), readRequired("Password: "),
                readSpecialization(), readAmount("Consultation fee: "));
        System.out.println("Doctor added with id: " + doctor.getId());
    }

    private void updateDoctorFee() {
        Doctor doctor = doctorService.getDoctorById(readInt("Doctor id: "));
        doctor.setConsultationFee(readAmount("New consultation fee: "));
        doctorService.updateDoctor(doctor);
        System.out.println("Doctor fee updated.");
    }

    private void deleteDoctor() {
        System.out.println(doctorService.deleteDoctor(readInt("Doctor id: ")) ? "Doctor deleted." : "No doctor found for that id.");
    }

    private void patientMenu() {
        System.out.println("\n--- Patient Management ---");
        System.out.println("1. Add patient\n2. View all patients\n3. Search patient by id");
        System.out.println("4. Search patients by name\n5. Search patients by age");
        System.out.println("6. Update current illness\n7. Delete patient\n0. Back");
        switch (readInt("Choose an option: ")) {
            case 1 -> addPatient();
            case 2 -> patientService.getAllPatients().forEach(System.out::println);
            case 3 -> patientService.searchPatient(Integer.valueOf(readInt("Patient id: ")))
                    .ifPresentOrElse(System.out::println, () -> System.out.println("Patient not found."));
            case 4 -> patientService.searchPatient(readRequired("Name to search: ")).forEach(System.out::println);
            case 5 -> patientService.searchPatient(readInt("Age: ")).forEach(System.out::println);
            case 6 -> updatePatientIllness();
            case 7 -> deletePatient();
            case 0 -> { }
            default -> System.out.println("Please choose an option from the menu.");
        }
    }

    private void addPatient() {
        Patient patient = patientService.createPatient(readRequired("Name: "), readInt("Age: "),
                readRequired("Username: "), readRequired("Email: "), readRequired("Password: "),
                readRequired("Current illness: "));
        System.out.println("Patient added with id: " + patient.getId());
    }

    private void updatePatientIllness() {
        Patient patient = patientService.getPatientById(readInt("Patient id: "));
        patient.setCurrentIllness(readRequired("New current illness: "));
        patientService.updatePatient(patient);
        System.out.println("Patient illness updated.");
    }

    private void deletePatient() {
        System.out.println(patientService.deletePatient(readInt("Patient id: ")) ? "Patient deleted." : "No patient found for that id.");
    }

    private void appointmentMenu() {
        System.out.println("\n--- Appointment Management ---");
        System.out.println("1. Create appointment\n2. View all appointments\n3. View appointment by id");
        System.out.println("4. View appointments for doctor\n5. View appointments for patient");
        System.out.println("6. Confirm appointment\n7. Cancel appointment\n0. Back");
        switch (readInt("Choose an option: ")) {
            case 1 -> createAppointment();
            case 2 -> appointmentService.getAllAppointments().forEach(System.out::println);
            case 3 -> System.out.println(appointmentService.getAppointmentById(readInt("Appointment id: ")));
            case 4 -> appointmentService.getAppointmentsForDoctor(readInt("Doctor id: ")).forEach(System.out::println);
            case 5 -> appointmentService.getAppointmentsForPatient(readInt("Patient id: ")).forEach(System.out::println);
            case 6 -> confirmAppointment();
            case 7 -> cancelAppointment();
            case 0 -> { }
            default -> System.out.println("Please choose an option from the menu.");
        }
    }

    private void createAppointment() {
        int doctorId = readInt("Doctor id: ");
        int patientId = readInt("Patient id: ");
        LocalDate date = DateUtil.parseDate(readRequired("Appointment date (yyyy-MM-dd): "));
        LocalTime startTime = DateUtil.parseTime(readRequired("Start time (HH:mm): "));
        LocalTime endTime = DateUtil.parseTime(readRequired("End time (HH:mm): "));
        Appointment appointment = appointmentService.createAppointment(doctorId, patientId, date, startTime, endTime);
        System.out.println("Appointment created with id: " + appointment.getId() + " (PENDING).");
    }

    private void confirmAppointment() {
        appointmentService.markAppointmentConfirmed(readInt("Appointment id: "));
        System.out.println("Appointment confirmed.");
    }

    private void cancelAppointment() {
        appointmentService.cancelAppointment(readInt("Appointment id: "));
        System.out.println("Appointment cancelled.");
    }

    private void billingMenu() {
        System.out.println("\n--- Billing ---\n1. Generate bill\n2. View all bills\n0. Back");
        switch (readInt("Choose an option: ")) {
            case 1 -> printBill(billingService.generateBill(readInt("Appointment id: "), readBillingType()));
            case 2 -> billingService.getAllBills().stream().map(Bill::generateBill).forEach(this::printBill);
            case 0 -> { }
            default -> System.out.println("Please choose an option from the menu.");
        }
    }

    private void analyticsMenu() {
        System.out.println("\n--- Analytics ---");
        System.out.println("1. Filter doctors by specialization\n2. Average consultation fee");
        System.out.println("3. Appointment count per doctor\n4. Upcoming confirmed appointments\n5. Most-booked doctor\n0. Back");
        switch (readInt("Choose an option: ")) {
            case 1 -> analyticsService.filterDoctorsBySpecialization(readSpecialization()).forEach(System.out::println);
            case 2 -> analyticsService.calculateAverageConsultationFee().ifPresentOrElse(
                    amount -> System.out.println("Average fee: " + amount), () -> System.out.println("No doctors registered."));
            case 3 -> printAppointmentCounts();
            case 4 -> analyticsService.getUpcomingConfirmedAppointments(
                    DateUtil.parseDate(readRequired("From date (yyyy-MM-dd): "))).forEach(System.out::println);
            case 5 -> analyticsService.findMostBookedDoctor().ifPresentOrElse(
                    System.out::println, () -> System.out.println("No appointments recorded."));
            case 0 -> { }
            default -> System.out.println("Please choose an option from the menu.");
        }
    }

    private void printAppointmentCounts() {
        Map<Doctor, Long> counts = analyticsService.countAppointmentsPerDoctor();
        if (counts.isEmpty()) {
            System.out.println("No doctors registered.");
            return;
        }
        counts.forEach((doctor, count) -> System.out.println(doctor.getDisplayName() + ": " + count));
    }

    private void printBill(BillSummary summary) {
        System.out.println("\nBill " + summary.getBillId() + " (" + summary.getBillingType() + ")");
        System.out.println("Consultation fee: " + summary.getConsultationFee());
        System.out.println("Discount: " + summary.getDiscountAmount());
        System.out.println("Tax: " + summary.getTaxAmount());
        System.out.println("Total: " + summary.getTotalAmount());
    }

    private void printMainMenu() {
        System.out.println("\n=== " + Constants.APPLICATION_NAME + " ===");
        System.out.println("1. Doctors\n2. Patients\n3. Appointments\n4. Billing\n5. Analytics\n0. Exit");
    }

    private int readInt(String prompt) {
        try {
            return Integer.parseInt(readRequired(prompt));
        } catch (NumberFormatException exception) {
            throw new InvalidDataException("Please enter a whole number.", exception);
        }
    }

    private BigDecimal readAmount(String prompt) {
        try {
            return new BigDecimal(readRequired(prompt));
        } catch (NumberFormatException exception) {
            throw new InvalidDataException("Please enter a valid amount.", exception);
        }
    }

    private String readRequired(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            throw new InvalidDataException("Input ended unexpectedly.");
        }
        String value = scanner.nextLine().trim();
        if (value.isEmpty()) {
            throw new InvalidDataException("A value is required.");
        }
        return value;
    }

    private Specialization readSpecialization() {
        System.out.println("Specializations:");
        Specialization[] values = Specialization.values();
        for (int index = 0; index < values.length; index++) {
            System.out.println((index + 1) + ". " + values[index]);
        }
        int choice = readInt("Specialization number: ");
        if (choice < 1 || choice > values.length) {
            throw new InvalidDataException("Invalid specialization number.");
        }
        return values[choice - 1];
    }

    private BillingType readBillingType() {
        System.out.println("Billing types:");
        BillingType[] values = BillingType.values();
        for (int index = 0; index < values.length; index++) {
            System.out.println((index + 1) + ". " + values[index]);
        }
        int choice = readInt("Billing type number: ");
        if (choice < 1 || choice > values.length) {
            throw new InvalidDataException("Invalid billing type number.");
        }
        return values[choice - 1];
    }
}
