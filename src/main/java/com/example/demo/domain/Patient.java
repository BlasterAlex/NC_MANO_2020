package com.example.demo.domain;

import com.example.demo.domain.constraints.BooleanConstraint;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 16)
    private String surname;

    @NotNull
    @Size(min = 2, max = 16)
    private String name;

    @Size(min = 2, max = 16)
    private String middleName;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "patient_symptom",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "symptom_id")
    )
    private Set<Symptom> symptoms = new HashSet<>();

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "patient_medic",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "medic_id")
    )
    @JsonIgnoreProperties("patients")
    private Set<Medic> medics = new HashSet<>();

    @BooleanConstraint
    private String isHavingTripAbroad;

    @BooleanConstraint
    private String contactWithPatients;

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

    public String getIsHavingTripAbroad() {
        return isHavingTripAbroad;
    }

    public void setIsHavingTripAbroad(String havingTripAbroad) {
        isHavingTripAbroad = havingTripAbroad;
    }

    public String getContactWithPatients() {
        return contactWithPatients;
    }

    public void setContactWithPatients(String contactWithPatients) {
        this.contactWithPatients = contactWithPatients;
    }

    public Set<Symptom> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(Set<Symptom> symptoms) {
        this.symptoms = symptoms;
    }

    public void addSymptom(Symptom symptom) {
        symptoms.add(symptom);
    }

    public void removeSymptom(Symptom symptom) {
        symptoms.remove(symptom);
    }

    public Set<Medic> getMedics() {
        return medics;
    }

    public void setMedics(Set<Medic> medics) {
        this.medics = medics;
    }

    public void addMedic(Medic medic) {
        medics.add(medic);
        medic.getPatients().add(this);
    }

    public void removeMedic(Medic medic) {
        medics.remove(medic);
        medic.getPatients().remove(this);
    }

    @Override
    public String toString() {
        final List<String> fields = new ArrayList<>();
        if (surname != null)
            fields.add("surname='" + surname + '\'');
        if (name != null)
            fields.add("name='" + name + '\'');
        if (middleName != null)
            fields.add("middleName='" + middleName + '\'');
        if (symptoms != null)
            fields.add("symptoms=[" +
                    symptoms.stream()
                            .map(entry -> String.valueOf(entry.getId()))
                            .sorted()
                            .collect(Collectors.joining(", ")) + ']');
        if (medics != null)
            fields.add("medics=[" +
                    medics.stream()
                            .map(entry -> String.valueOf(entry.getId()))
                            .sorted()
                            .collect(Collectors.joining(", ")) + ']');
        if (isHavingTripAbroad != null)
            fields.add("isHavingTripAbroad=" + isHavingTripAbroad);
        if (contactWithPatients != null)
            fields.add("contactWithPatients=" + contactWithPatients);
        return "Patient{" + String.join(", ", fields) + '}';
    }
}
