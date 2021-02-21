package com.example.demo.frontend.domain;

import lombok.Data;

@Data
public class UiPatient implements Comparable<UiPatient> {

    private Long id;

    private String surname;

    private String name;

    private String middleName;

    private String isHavingTripAbroad;

    private String contactWithPatients;

    @Override
    public int compareTo(UiPatient u) {
        if (getId() == null || u.getId() == null) {
            return 0;
        }
        return getId().compareTo(u.getId());
    }

}
