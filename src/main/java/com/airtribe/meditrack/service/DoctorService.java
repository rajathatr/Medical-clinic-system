package com.airtribe.meditrack.service;

import com.airtribe.meditrack.constants.Specialization;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.util.DataStore;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/** Handles doctor CRUD operations and doctor searches. */
public class DoctorService {
    private final DataStore<Doctor> doctorStore = new DataStore<>();

    public Doctor createDoctor(String name, int age, String username, String email, String password,
                               Specialization specialization, BigDecimal consultationFee) {
        Doctor doctor = new Doctor(name, age, username, email, password, specialization, consultationFee);
        doctorStore.add(doctor);
        return doctor;
    }

    public void addDoctor(Doctor doctor) {
        doctorStore.add(doctor);
    }

    public Doctor getDoctorById(int doctorId) {
        return doctorStore.get(doctorId, "Doctor");
    }

    public Optional<Doctor> findDoctorById(int doctorId) {
        return doctorStore.findById(doctorId);
    }

    public List<Doctor> getAllDoctors() {
        return doctorStore.findAll();
    }

    public List<Doctor> searchDoctorsByName(String name) {
        String query = normalizeQuery(name);
        return doctorStore.findAll().stream()
                .filter(doctor -> doctor.getName().toLowerCase(Locale.ROOT).contains(query))
                .toList();
    }

    public List<Doctor> findDoctorsBySpecialization(Specialization specialization) {
        return doctorStore.findAll().stream()
                .filter(doctor -> doctor.getSpecialization() == specialization)
                .toList();
    }

    public void updateDoctor(Doctor doctor) {
        doctorStore.update(doctor);
    }

    public boolean deleteDoctor(int doctorId) {
        return doctorStore.delete(doctorId);
    }

    private String normalizeQuery(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Search name must not be blank.");
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
