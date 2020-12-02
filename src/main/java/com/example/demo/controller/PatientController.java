package com.example.demo.controller;

import com.example.demo.domain.Patient;
import com.example.demo.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/patient")
public class PatientController {

    private static final Logger log = LoggerFactory.getLogger(PatientController.class);

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    private ResponseEntity<String> printValidError(Errors errors) {
        FieldError field = errors.getFieldErrors().get(0);
        return new ResponseEntity<>(field.getField() + ": " +
                field.getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Patient>> findAll() {
        log.info("GET request for a list of patients");
        List<Patient> patients = patientService.findAll();
        return new ResponseEntity<>(patients, patients.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> find(@PathVariable Long id) {
        log.info("GET request for a patient with id " + id);
        Patient patient = patientService.find(id);
        if (patient == null) {
            log.info("Patient with id " + id + " not found");
        }
        return new ResponseEntity<>(patient, patient == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addPatient(@RequestBody @Valid Patient patient, Errors errors) {
        log.info("POST request for creation " + patient);
        if (errors.hasErrors()) {
            log.info("Patient not valid");
            return printValidError(errors);
        }
        Long patient_id = patientService.savePatient(patient);
        log.info("Patient created with id " + patient_id);
        return ResponseEntity.ok("Patient created with id " + patient_id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable Long id) {
        log.info("DELETE request for a patient with id " + id);
        int countDeleted = patientService.delete(id);
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
        Long patient_id = patientService.put(id, patient);
        if (patient_id == -1) {
            log.info("Patient with id " + id + " not found");
            return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
        }
        log.info("Patient updated with id " + patient_id);
        return new ResponseEntity<>("Patient updated with id " + patient_id, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> addSymptom(@PathVariable Long id, @RequestBody Map<String, Object> patientInfo) {

        log.info("PATCH request for change with " + patientInfo);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        for (Map.Entry<String, Object> entry : patientInfo.entrySet()) {
            Set<ConstraintViolation<Patient>> constraintViolations =
                    validator.validateValue(Patient.class, entry.getKey(), entry.getValue());
            if (constraintViolations.iterator().hasNext()) {
                log.info("Patient not valid");
                return new ResponseEntity<>(entry.getKey() + ": " +
                        constraintViolations.iterator().next().getMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        if (!patientService.patch(id, patientInfo)) {
            log.info("Patient with id " + id + " not found");
            return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
        }
        
        log.info("Successfully changed");
        return new ResponseEntity<>("Successfully changed", HttpStatus.OK);
    }
}
