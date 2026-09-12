package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.strategy.DiscountedBillingStrategy;

import java.math.BigDecimal;

/** Discounted bill implementation. */
public class DiscountedBill extends Bill {
    public DiscountedBill(int id, int appointmentId, int doctorId, int patientId, BigDecimal consultationFee) {
        super(id, appointmentId, doctorId, patientId, consultationFee, new DiscountedBillingStrategy());
    }

    @Override
    public BillSummary generateBill() {
        return createSummary();
    }
}
