package com.example.demo.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Patient {

    private Long id;

    @NotNull
    @Size(min = 2, max = 16)
    private String surname;

    @NotNull
    @Size(min = 2, max = 16)
    private String name;

    @Size(min = 2, max = 16)
    private String middleName;

    private String symptoms;

    @PatientBooleanConstraint
    private String isHavingTipAbroad;

    @PatientBooleanConstraint
    private String contactWithPatients;

    public Patient() {
    }

    public Patient(Long id, String surname, String name,
                   String middleName, String symptoms,
                   String isHavingTipAbroad, String contactWithPatients) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.middleName = middleName;
        this.symptoms = symptoms;
        this.isHavingTipAbroad = isHavingTipAbroad;
        this.contactWithPatients = contactWithPatients;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getIsHavingTipAbroad() {
        return isHavingTipAbroad;
    }

    public void setHavingTipAbroad(String havingTipAbroad) {
        isHavingTipAbroad = havingTipAbroad;
    }

    public String getContactWithPatients() {
        return contactWithPatients;
    }

    public void setContactWithPatients(String contactWithPatients) {
        this.contactWithPatients = contactWithPatients;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", middleName='" + middleName + '\'' +
                ", symptoms='" + symptoms + '\'' +
                ", isHavingTipAbroad=" + isHavingTipAbroad +
                ", contactWithPatients=" + contactWithPatients +
                '}';
    }
}
