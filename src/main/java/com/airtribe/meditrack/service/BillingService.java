package com.airtribe.meditrack.service;

import com.airtribe.meditrack.constants.AppointmentStatus;
import com.airtribe.meditrack.constants.BillingType;
import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.BillSummary;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.factory.BillFactory;
import com.airtribe.meditrack.util.DataStore;

import java.util.List;

/** Generates and retrieves one bill for each confirmed or completed appointment. */
public class BillingService {
    private final DataStore<Bill> billStore = new DataStore<>();
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    public BillingService(AppointmentService appointmentService, DoctorService doctorService) {
        if (appointmentService == null || doctorService == null) {
            throw new IllegalArgumentException("Appointment and doctor services must not be null.");
        }
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
    }

    public BillSummary generateBill(int appointmentId, BillingType billingType) {
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        if (appointment.getStatus() != AppointmentStatus.CONFIRMED
                && appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new InvalidDataException("Only confirmed or completed appointments can be billed.");
        }
        if (findBillByAppointmentId(appointmentId) != null) {
            throw new InvalidDataException("A bill already exists for appointment id: " + appointmentId);
        }

        Doctor doctor = doctorService.getDoctorById(appointment.getDoctorId());
        Bill bill = BillFactory.createBill(billingType, appointment, doctor);
        billStore.add(bill);
        return bill.generateBill();
    }

    public Bill getBillById(int billId) {
        return billStore.get(billId, "Bill");
    }

    public List<Bill> getAllBills() {
        return billStore.findAll();
    }

    private Bill findBillByAppointmentId(int appointmentId) {
        return billStore.findAll().stream()
                .filter(bill -> bill.getAppointmentId() == appointmentId)
                .findFirst()
                .orElse(null);
    }
}
