package com.example.demo.frontend.builder;

import com.example.demo.backend.domain.Patient;
import com.example.demo.frontend.domain.UiPatient;
import org.springframework.stereotype.Service;

@Service
public class PatientBuilder implements Builder<Patient, UiPatient> {

    @Override
    public Patient encode(UiPatient uiPatient) {
        Patient p = new Patient();
        String[] nameSplit = uiPatient.getSurnameWithInitials().split("\\.");
        p.setSurname(nameSplit[0].trim());
        p.setName(nameSplit[1]);
        if (nameSplit.length > 2)
            p.setMiddleName(nameSplit[2]);
        return p;
    }

    @Override
    public UiPatient decode(Patient patient) {
        UiPatient uiPatient = new UiPatient();
        StringBuilder sb = new StringBuilder(patient.getSurname());
        sb.append(" ").append(patient.getName().charAt(0)).append(".");
        if (patient.getMiddleName() != null) {
            sb.append(patient.getMiddleName().charAt(0)).append(".");
        }
        uiPatient.setSurnameWithInitials(sb.toString());
        uiPatient.setSymptomsQty(patient.getSymptoms().size());
        return uiPatient;
    }
}
