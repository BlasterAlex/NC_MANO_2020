package com.example.demo.domain;

import com.example.demo.domain.constraints.BooleanConstraint;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Symptom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "symptom_id")
    private Long id;

    @NotNull
    @Size(min = 2)
    private String name;

    @BooleanConstraint
    private String isCovidSymptom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsCovidSymptom() {
        return isCovidSymptom;
    }

    public void setIsCovidSymptom(String isCovidSymptom) {
        this.isCovidSymptom = isCovidSymptom;
    }
}
