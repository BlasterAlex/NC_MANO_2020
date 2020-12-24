package com.example.demo.backend.domain;

import com.example.demo.backend.domain.constraints.BooleanConstraint;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    @EqualsAndHashCode.Exclude
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
    private List<Symptom> symptoms = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "patient_medic",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "medic_id")
    )
    @JsonIgnoreProperties("patients")
    private List<Medic> medics = new ArrayList<>();

    @BooleanConstraint
    private String isHavingTripAbroad;

    @BooleanConstraint
    private String contactWithPatients;

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
