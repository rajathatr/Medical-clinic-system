package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.strategy.StandardBillingStrategy;

import java.math.BigDecimal;

/** Full-fee bill implementation. */
public class StandardBill extends Bill {
    public StandardBill(int id, int appointmentId, int doctorId, int patientId, BigDecimal consultationFee) {
        super(id, appointmentId, doctorId, patientId, consultationFee, new StandardBillingStrategy());
    }

    @Override
    public BillSummary generateBill() {
        return createSummary();
    }
}
