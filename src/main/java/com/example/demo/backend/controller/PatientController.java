package com.example.demo.backend.controller;

import com.example.demo.backend.domain.Patient;
import com.example.demo.backend.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/backend/patient")
public class PatientController {

    private static final Logger log = LoggerFactory.getLogger(PatientController.class);

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private final PatientRepository patientRepository;

    @Autowired
    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    protected boolean notValidField(String fieldName, Object fieldValue, BindingResult errors) {
        Iterator<ConstraintViolation<Patient>> cvIter;
        if (fieldValue != null && (cvIter = validator.validateValue(Patient.class, fieldName, fieldValue).iterator()).hasNext()) {
            errors.rejectValue(fieldName, "invalid field", cvIter.next().getMessage());
            return true;
        }
        return false;
    }

    public BindingResult validatePatient(Patient patient) {
        BindingResult errors = new BeanPropertyBindingResult(patient, "Patient");

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

    protected Object compareValue(Object oldVal, Object newVal) {
        if (newVal != null && !newVal.equals(oldVal)) {
            return newVal;
        }
        return oldVal;
    }

    private void mergePatients(Patient oldPatient, Patient newPatient) {
        oldPatient.setName((String) compareValue(oldPatient.getName(), newPatient.getName()));
        oldPatient.setSurname((String) compareValue(oldPatient.getSurname(), newPatient.getSurname()));
        oldPatient.setMiddleName((String) compareValue(oldPatient.getMiddleName(), newPatient.getMiddleName()));
        oldPatient.setIsHavingTripAbroad(
                (String) compareValue(oldPatient.getIsHavingTripAbroad(), newPatient.getIsHavingTripAbroad()));
        oldPatient.setContactWithPatients(
                (String) compareValue(oldPatient.getContactWithPatients(), newPatient.getContactWithPatients()));
        oldPatient.setSymptoms((List) compareValue(oldPatient.getSymptoms(), newPatient.getSymptoms()));
        oldPatient.setMedics((List) compareValue(oldPatient.getMedics(), newPatient.getMedics()));
    }


    @GetMapping("/all")
    public ResponseEntity<List<Patient>> findAll() {
        log.info("GET request for a list of patients");
        List<Patient> patients = patientRepository.findAll();
        return new ResponseEntity<>(patients, patients.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }


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


    @PostMapping
    public ResponseEntity<String> add(@RequestBody @Valid Patient patient, BindingResult bindingResult) {
        log.info("POST request for creation " + patient);
        if (bindingResult.hasErrors()) {
            FieldError err = bindingResult.getFieldError();
            String message = err.getField() + ": " + err.getDefaultMessage();
            log.info("Patient not valid (" + message + ")");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        Long patient_id = patientRepository.save(patient).getId();
        log.info("Patient created with id " + patient_id);
        return ResponseEntity.ok("Patient created with id " + patient_id);
    }


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


    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody @Valid Patient patient, BindingResult bindingResult) {
        log.info("PUT request for update " + patient);
        if (bindingResult.hasErrors()) {
            FieldError err = bindingResult.getFieldError();
            String message = err.getField() + ": " + err.getDefaultMessage();
            log.info("Patient not valid (" + message + ")");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
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


    @PatchMapping("/{id}")
    public ResponseEntity<String> edit(@PathVariable Long id, @RequestBody Patient patient) {
        log.info("PATCH request for update " + patient);

        BindingResult bindingResult = validatePatient(patient);
        if (bindingResult.hasErrors()) {
            FieldError err = bindingResult.getFieldError();
            String message = err.getField() + ": " + err.getDefaultMessage();
            log.info("Patient not valid (" + message + ")");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
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
