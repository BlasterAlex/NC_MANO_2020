package com.example.demo.backend.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@Data
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
    private final List<Patient> patients = new ArrayList<>();

    @NotNull
    private String position;

    @NotNull
    private Integer category;

}
