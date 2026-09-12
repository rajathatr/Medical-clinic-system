package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.util.Validator;

public abstract class Person extends MedicalEntity {
    private String name;
    private int age;
    private String email;
    private String username;
    private String password;

    protected Person(int id, String name, int age, String username, String email, String password) {
        super(id);
        this.name = Validator.requireNonBlank(name, "Name");
        this.age = Validator.requireValidAge(age);
        this.username = Validator.requireNonBlank(username, "Username");
        this.email = Validator.requireValidEmail(email);
        this.password = Validator.requireNonBlank(password, "Password");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Validator.requireNonBlank(name, "Name");
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = Validator.requireValidAge(age);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = Validator.requireValidEmail(email);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = Validator.requireNonBlank(username, "Username");
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = Validator.requireNonBlank(password, "Password");
    }
}
