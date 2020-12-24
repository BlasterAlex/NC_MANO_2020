package com.example.demo.backend.domain;

import com.example.demo.backend.domain.constraints.BooleanConstraint;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
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
}
