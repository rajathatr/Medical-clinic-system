package com.airtribe.meditrack.entity;

import java.util.List;

public class Patient extends Person{
    private final int id;
    private List<Integer> history;
    private String currentIllness;

    public Patient(String name, int age, String username, String email, String password, String currentIllness) {
        super(name, age, email, username, password);
        this.currentIllness = currentIllness;
        this.id = 0;
    }

    public List<Integer> getHistory() {
        return history;
    }

    public void addToHistory(Integer history) {
        this.history.add(history);
    }

    public String getCurrentIllness() {
        return currentIllness;
    }

    public void setCurrentIllness(String currentIllness) {
        this.currentIllness = currentIllness;
    }

    public int getId() {
        return id;
    }
}
