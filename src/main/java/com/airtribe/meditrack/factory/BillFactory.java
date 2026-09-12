package com.airtribe.meditrack.factory;

import com.airtribe.meditrack.constants.BillingType;
import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.DiscountedBill;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.StandardBill;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.util.IdGenerator;

/** Factory that creates the correct concrete bill from a billing choice. */
public final class BillFactory {
    private BillFactory() {
    }

    public static Bill createBill(BillingType billingType, Appointment appointment, Doctor doctor) {
        if (billingType == null || appointment == null || doctor == null) {
            throw new InvalidDataException("Billing type, appointment, and doctor must not be null.");
        }
        if (appointment.getDoctorId() != doctor.getId()) {
            throw new InvalidDataException("Appointment doctor does not match the bill doctor.");
        }

        int billId = IdGenerator.getInstance().nextBillId();
        return switch (billingType) {
            case STANDARD -> new StandardBill(billId, appointment.getId(), doctor.getId(),
                    appointment.getPatientId(), doctor.getConsultationFee());
            case DISCOUNTED -> new DiscountedBill(billId, appointment.getId(), doctor.getId(),
                    appointment.getPatientId(), doctor.getConsultationFee());
        };
    }
}
