package com.airtribe.meditrack.entity;

public class Doctor extends Person {
    private final int id;
    private String speciality;

    public Doctor(String name, int age, String username, String email, String password, String speciality) {
        super(name, age, email, username, password);
        this.speciality = speciality;
        this.id = 0;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public int getId() {
        return id;
    }
}
