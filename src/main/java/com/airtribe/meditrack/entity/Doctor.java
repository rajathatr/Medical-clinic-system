package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.constants.Specialization;
import com.airtribe.meditrack.util.IdGenerator;
import com.airtribe.meditrack.util.Validator;
import java.math.BigDecimal;

public class Doctor extends Person {
    private Specialization specialization;
    private BigDecimal consultationFee;

    public Doctor(String name, int age, String username, String email, String password,
                  Specialization specialization, BigDecimal consultationFee) {
        super(IdGenerator.getInstance().nextDoctorId(), name, age, username, email, password);
        if (specialization == null) {
            throw new IllegalArgumentException("Specialization must not be null.");
        }
        this.specialization = specialization;
        this.consultationFee = Validator.requirePositiveAmount(consultationFee, "Consultation fee");
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        if (specialization == null) {
            throw new IllegalArgumentException("Specialization must not be null.");
        }
        this.specialization = specialization;
    }

    public BigDecimal getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(BigDecimal consultationFee) {
        this.consultationFee = Validator.requirePositiveAmount(consultationFee, "Consultation fee");
    }

    @Override
    public String getDisplayName() {
        return "Dr. " + getName() + " (" + specialization + ")";
    }

    @Override
    public String toString() {
        return getDisplayName() + " - Fee: " + consultationFee;
    }
}
