package com.example.demo.controller;

import com.example.demo.domain.Patient;
import com.example.demo.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequestMapping(value = "/patient")
public class PatientController extends AbstractController<Patient> {

    private static final Logger log = LoggerFactory.getLogger(PatientController.class);

    private final PatientRepository patientRepository;

    @Autowired
    public PatientController(PatientRepository patientRepository) {
        super(Patient.class);
        this.patientRepository = patientRepository;
    }

    @Override
    public Errors validate(Patient patient) {
        Errors errors = new BeanPropertyBindingResult(patient, "Patient");

        if (notValidField("name", patient.getName(), errors)) {
            return errors;
        }

        if (notValidField("surname", patient.getSurname(), errors)) {
            return errors;
        }

        if (notValidField("middleName", patient.getMiddleName(), errors)) {
            return errors;
        }

        if (notValidField("isHavingTripAbroad", patient.getIsHavingTripAbroad(), errors)) {
            return errors;
        }

        notValidField("contactWithPatients", patient.getContactWithPatients(), errors);

        return errors;
    }

    private void mergePatients(Patient oldPatient, Patient newPatient) {
        oldPatient.setName((String) compareValue(oldPatient.getName(), newPatient.getName()));
        oldPatient.setSurname((String) compareValue(oldPatient.getSurname(), newPatient.getSurname()));
        oldPatient.setMiddleName((String) compareValue(oldPatient.getMiddleName(), newPatient.getMiddleName()));
        oldPatient.setIsHavingTripAbroad(
                (String) compareValue(oldPatient.getIsHavingTripAbroad(), newPatient.getIsHavingTripAbroad()));
        oldPatient.setContactWithPatients(
                (String) compareValue(oldPatient.getContactWithPatients(), newPatient.getContactWithPatients()));
        oldPatient.setSymptoms((Set) compareValue(oldPatient.getSymptoms(), newPatient.getSymptoms()));
        oldPatient.setMedics((Set) compareValue(oldPatient.getMedics(), newPatient.getMedics()));
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<Patient>> findAll() {
        log.info("GET request for a list of patients");
        List<Patient> patients = patientRepository.findAll();
        return new ResponseEntity<>(patients, patients.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Patient> find(@PathVariable Long id) {
        log.info("GET request for a patient with id " + id);
        try {
            return new ResponseEntity<>(patientRepository.findPatientById(id).get(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            log.info("Patient with id " + id + " not found");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @PostMapping
    public ResponseEntity<String> add(@RequestBody @Valid Patient patient, Errors errors) {
        log.info("POST request for creation " + patient);
        if (errors.hasErrors()) {
            log.info("Patient not valid");
            return printValidError(errors);
        }
        Long patient_id = patientRepository.save(patient).getId();
        log.info("Patient created with id " + patient_id);
        return ResponseEntity.ok("Patient created with id " + patient_id);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        log.info("DELETE request for a patient with id " + id);
        try {
            patientRepository.deleteById(id);
            log.info("Patient removed successfully");
            return new ResponseEntity<>("Patient removed successfully", HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            log.info("Patient with id " + id + " not found");
            return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody @Valid Patient patient, Errors errors) {
        log.info("PUT request for update " + patient);
        if (errors.hasErrors()) {
            log.info("Patient not valid");
            return printValidError(errors);
        }
        try {
            patientRepository.deleteById(id);
            Long patient_id = patientRepository.save(patient).getId();
            log.info("Patient updated with id " + patient_id);
            return ResponseEntity.ok("Patient updated with id " + patient_id);
        } catch (EmptyResultDataAccessException e) {
            log.info("Patient with id " + id + " not found");
            return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<String> edit(@PathVariable Long id, @RequestBody Patient patient) {
        log.info("PATCH request for update " + patient);

        Errors errors = validate(patient);
        if (errors.hasErrors()) {
            return printValidError(errors);
        }

        Patient oldPatient;
        try {
            oldPatient = patientRepository.findPatientById(id).get();
        } catch (NoSuchElementException e) {
            log.info("Patient with id " + id + " not found");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        mergePatients(oldPatient, patient);
        patientRepository.save(oldPatient);
        log.info("Successfully changed");
        return new ResponseEntity<>("Successfully changed", HttpStatus.OK);
    }
}
