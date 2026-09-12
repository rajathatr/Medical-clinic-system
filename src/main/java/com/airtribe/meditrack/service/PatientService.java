package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.util.DataStore;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/** Handles patient CRUD operations and overloaded patient searches. */
public class PatientService {
    private final DataStore<Patient> patientStore = new DataStore<>();

    public Patient createPatient(String name, int age, String username, String email, String password,
                                 String currentIllness) {
        Patient patient = new Patient(name, age, username, email, password, currentIllness);
        patientStore.add(patient);
        return patient;
    }

    public void addPatient(Patient patient) {
        patientStore.add(patient);
    }

    public Patient getPatientById(int patientId) {
        return patientStore.get(patientId, "Patient");
    }

    /** Searches by identifier; use an {@link Integer} to distinguish it from the age overload. */
    public Optional<Patient> searchPatient(Integer patientId) {
        if (patientId == null) {
            return Optional.empty();
        }
        return patientStore.findById(patientId);
    }

    /** Searches by a case-insensitive partial name match. */
    public List<Patient> searchPatient(String name) {
        String query = normalizeQuery(name);
        return patientStore.findAll().stream()
                .filter(patient -> patient.getName().toLowerCase(Locale.ROOT).contains(query))
                .toList();
    }

    /** Searches by an exact age. */
    public List<Patient> searchPatient(int age) {
        return patientStore.findAll().stream()
                .filter(patient -> patient.getAge() == age)
                .toList();
    }

    public List<Patient> getAllPatients() {
        return patientStore.findAll();
    }

    public void updatePatient(Patient patient) {
        patientStore.update(patient);
    }

    public boolean deletePatient(int patientId) {
        return patientStore.delete(patientId);
    }

    private String normalizeQuery(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Search name must not be blank.");
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
