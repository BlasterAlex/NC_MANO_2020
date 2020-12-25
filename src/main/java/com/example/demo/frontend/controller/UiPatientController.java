package com.example.demo.frontend.controller;

import com.example.demo.frontend.domain.UiPatient;
import com.example.demo.frontend.service.UiPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/ui/patient")
public class UiPatientController {

    @Autowired
    UiPatientService uiPatientService;

    @GetMapping("/all")
    public ResponseEntity<List<UiPatient>> findAll() {
        List<UiPatient> uiPatients = uiPatientService.getAllUiPatient();
        Collections.sort(uiPatients);
        return new ResponseEntity<>(uiPatients, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UiPatient> find(@PathVariable Long id) {
        UiPatient uiPatient = uiPatientService.getUiPatient(id);
        return new ResponseEntity<>(uiPatient, uiPatient == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> add(@RequestBody UiPatient uiPatient) {
        return uiPatientService.addUiPatient(uiPatient);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> delete(@PathVariable Long id) {
        if (uiPatientService.deletePatient(id)) {
            return ResponseEntity.ok("Patient removed successfully");
        }
        return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody UiPatient uiPatient) {
        return uiPatientService.updatePatient(id, uiPatient);
    }

    @PatchMapping(value = "/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> edit(@PathVariable Long id, @RequestBody UiPatient uiPatient) {
        return uiPatientService.editPatient(id, uiPatient);
    }
}
