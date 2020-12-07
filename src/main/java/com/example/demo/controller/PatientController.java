package com.example.demo.controller;

import com.example.demo.domain.Patient;
import com.example.demo.service.PatientServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/patient")
public class PatientController {

    private static final Logger log = LoggerFactory.getLogger(PatientController.class);

    private final PatientServiceImpl patientServiceImpl;

    @Autowired
    public PatientController(PatientServiceImpl patientService) {
        this.patientServiceImpl = patientService;
    }

    private ResponseEntity<String> printValidError(Errors errors) {
        FieldError field = errors.getFieldErrors().get(0);
        return new ResponseEntity<>(field.getField() + ": " +
                field.getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Patient>> findAll() {
        log.info("GET request for a list of patients");
        List<Patient> patients = patientServiceImpl.findAll();
        return new ResponseEntity<>(patients, patients.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> find(@PathVariable Long id) {
        log.info("GET request for a patient with id " + id);
        try {
            return new ResponseEntity<>(patientServiceImpl.find(id), HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            log.info("Patient with id " + id + " not found");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<String> addPatient(@RequestBody @Valid Patient patient, Errors errors) {
        log.info("POST request for creation " + patient);
        if (errors.hasErrors()) {
            log.info("Patient not valid");
            return printValidError(errors);
        }
        Long patient_id = patientServiceImpl.savePatient(patient);
        log.info("Patient created with id " + patient_id);
        return ResponseEntity.ok("Patient created with id " + patient_id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable Long id) {
        log.info("DELETE request for a patient with id " + id);
        int countDeleted = patientServiceImpl.delete(id);
        if (countDeleted == 0) {
            log.info("Patient with id " + id + " not found");
            return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
        }
        log.info("Patient removed successfully");
        return new ResponseEntity<>("Patient removed successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePatient(@PathVariable Long id, @RequestBody @Valid Patient patient, Errors errors) {
        log.info("PUT request for update " + patient);
        if (errors.hasErrors()) {
            log.info("Patient not valid");
            return printValidError(errors);
        }
        try {
            Long patient_id = patientServiceImpl.put(id, patient);
            log.info("Patient updated with id " + patient_id);
            return new ResponseEntity<>("Patient updated with id " + patient_id, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> editPatient(@PathVariable Long id, @RequestBody Patient patient) {
        log.info("PATCH request for change with " + patient);
        try {
            patientServiceImpl.patch(id, patient);
            log.info("Successfully changed");
            return new ResponseEntity<>("Successfully changed", HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
