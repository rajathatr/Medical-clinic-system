package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.util.IdGenerator;
import com.airtribe.meditrack.util.Validator;
import java.util.ArrayList;
import java.util.List;

public class Patient extends Person implements Cloneable {
    private List<Integer> appointmentHistory;
    private String currentIllness;

    public Patient(String name, int age, String username, String email, String password, String currentIllness) {
        super(IdGenerator.getInstance().nextPatientId(), name, age, username, email, password);
        this.appointmentHistory = new ArrayList<>();
        this.currentIllness = Validator.requireNonBlank(currentIllness, "Current illness");
    }

    public List<Integer> getAppointmentHistory() {
        return List.copyOf(appointmentHistory);
    }

    public void addAppointmentToHistory(int appointmentId) {
        if (appointmentId <= 0) {
            throw new IllegalArgumentException("Appointment id must be positive.");
        }
        appointmentHistory.add(appointmentId);
    }

    public String getCurrentIllness() {
        return currentIllness;
    }

    public void setCurrentIllness(String currentIllness) {
        this.currentIllness = Validator.requireNonBlank(currentIllness, "Current illness");
    }

    @Override
    public String getDisplayName() {
        return getName();
    }

    @Override
    public Patient clone() {
        try {
            Patient copy = (Patient) super.clone();
            copy.appointmentHistory = new ArrayList<>(appointmentHistory);
            return copy;
        } catch (CloneNotSupportedException exception) {
            throw new AssertionError("Patient cloning should be supported.", exception);
        }
    }

    @Override
    public String toString() {
        return "Patient " + getName() + " - Current illness: " + currentIllness;
    }
}
