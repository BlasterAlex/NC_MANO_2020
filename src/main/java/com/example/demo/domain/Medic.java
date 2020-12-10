package com.example.demo.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Medic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medic_id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 16)
    private String surname;

    @NotNull
    @Size(min = 2, max = 16)
    private String name;

    @Size(min = 2, max = 16)
    private String middleName;

    @ManyToMany(mappedBy = "medics")
    private final Set<Patient> patients = new HashSet<>();

    @NotNull
    private String position;

    @NotNull
    private Integer category;

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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Set<Patient> getPatients() {
        return patients;
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
        patient.getMedics().add(this);
    }

    public void removePatient(Patient patient) {
        patients.remove(patient);
        patient.getMedics().remove(this);
    }
}
