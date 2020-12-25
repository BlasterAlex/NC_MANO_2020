package com.example.demo.frontend.builder;

import com.example.demo.backend.domain.Patient;
import com.example.demo.frontend.domain.UiPatient;
import org.springframework.stereotype.Service;

@Service
public class PatientBuilder implements Builder<Patient, UiPatient> {

    @Override
    public Patient encode(UiPatient uiPatient) {
        Patient p = new Patient();
        p.setId(uiPatient.getId());
        p.setName(uiPatient.getName());
        p.setSurname(uiPatient.getSurname());
        p.setMiddleName(uiPatient.getMiddleName());
        p.setContactWithPatients(uiPatient.getContactWithPatients());
        p.setIsHavingTripAbroad(uiPatient.getIsHavingTripAbroad());
        p.setMedics(null);
        p.setSymptoms(null);
        return p;
    }

    @Override
    public UiPatient decode(Patient patient) {
        UiPatient uiPatient = new UiPatient();
        uiPatient.setId(patient.getId());
        uiPatient.setName(patient.getName());
        uiPatient.setSurname(patient.getSurname());
        uiPatient.setMiddleName(patient.getMiddleName());
        uiPatient.setContactWithPatients(patient.getContactWithPatients());
        uiPatient.setIsHavingTripAbroad(patient.getIsHavingTripAbroad());
        return uiPatient;
    }
}
