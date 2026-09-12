package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.interfaces.Payable;
import com.airtribe.meditrack.strategy.BillingStrategy;
import com.airtribe.meditrack.util.Validator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/** Base class for generated appointment bills. */
public abstract class Bill extends MedicalEntity implements Payable {
    private final int appointmentId;
    private final int doctorId;
    private final int patientId;
    private final BigDecimal consultationFee;
    private final BillingStrategy billingStrategy;

    protected Bill(int id, int appointmentId, int doctorId, int patientId,
                   BigDecimal consultationFee, BillingStrategy billingStrategy) {
        super(id);
        if (appointmentId <= 0 || doctorId <= 0 || patientId <= 0) {
            throw new IllegalArgumentException("Appointment, doctor, and patient ids must be positive.");
        }
        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.consultationFee = money(Validator.requirePositiveAmount(consultationFee, "Consultation fee"));
        this.billingStrategy = Objects.requireNonNull(billingStrategy, "Billing strategy must not be null.");
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public int getPatientId() {
        return patientId;
    }

    public BigDecimal getConsultationFee() {
        return consultationFee;
    }

    protected BillSummary createSummary() {
        BigDecimal discount = money(billingStrategy.calculateDiscount(consultationFee));
        if (discount.signum() < 0 || discount.compareTo(consultationFee) > 0) {
            throw new IllegalStateException("Billing strategy returned an invalid discount.");
        }
        BigDecimal taxableAmount = money(consultationFee.subtract(discount));
        BigDecimal taxAmount = money(taxableAmount.multiply(Constants.TAX_RATE));
        BigDecimal totalAmount = money(taxableAmount.add(taxAmount));
        return new BillSummary(getId(), appointmentId, billingStrategy.getBillingType(), consultationFee,
                discount, taxableAmount, taxAmount, totalAmount, getCreatedAt());
    }

    @Override
    public String getDisplayName() {
        return "Bill " + getId() + " for appointment " + appointmentId;
    }

    private BigDecimal money(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
