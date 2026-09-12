package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.constants.BillingType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/** Immutable calculated view of a bill, safe to share across callers. */
public final class BillSummary {
    private final int billId;
    private final int appointmentId;
    private final BillingType billingType;
    private final BigDecimal consultationFee;
    private final BigDecimal discountAmount;
    private final BigDecimal taxableAmount;
    private final BigDecimal taxAmount;
    private final BigDecimal totalAmount;
    private final LocalDateTime generatedAt;

    public BillSummary(int billId, int appointmentId, BillingType billingType, BigDecimal consultationFee,
                       BigDecimal discountAmount, BigDecimal taxableAmount, BigDecimal taxAmount,
                       BigDecimal totalAmount, LocalDateTime generatedAt) {
        this.billId = billId;
        this.appointmentId = appointmentId;
        this.billingType = Objects.requireNonNull(billingType, "Billing type must not be null.");
        this.consultationFee = Objects.requireNonNull(consultationFee, "Consultation fee must not be null.");
        this.discountAmount = Objects.requireNonNull(discountAmount, "Discount amount must not be null.");
        this.taxableAmount = Objects.requireNonNull(taxableAmount, "Taxable amount must not be null.");
        this.taxAmount = Objects.requireNonNull(taxAmount, "Tax amount must not be null.");
        this.totalAmount = Objects.requireNonNull(totalAmount, "Total amount must not be null.");
        this.generatedAt = Objects.requireNonNull(generatedAt, "Generated time must not be null.");
    }

    public int getBillId() {
        return billId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public BillingType getBillingType() {
        return billingType;
    }

    public BigDecimal getConsultationFee() {
        return consultationFee;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getTaxableAmount() {
        return taxableAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    @Override
    public String toString() {
        return "Bill " + billId + " [" + billingType + "] total: " + totalAmount;
    }
}
