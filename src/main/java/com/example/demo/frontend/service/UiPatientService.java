package com.example.demo.frontend.service;

import com.example.demo.backend.domain.Patient;
import com.example.demo.frontend.builder.PatientBuilder;
import com.example.demo.frontend.domain.UiPatient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@ConfigurationProperties(prefix = "backend")
public class UiPatientService {

    private final RestTemplate restTemplate;
    private final PatientBuilder patientBuilder;
    private String server;
    private String port;


    @Autowired
    public UiPatientService(PatientBuilder patientBuilder) {
        this.restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        this.patientBuilder = patientBuilder;
    }

    public List<UiPatient> getAllUiPatient() {
        Patient[] uiPatients = restTemplate
                .getForEntity(server + ":" + port + "/backend/patient/all", Patient[].class)
                .getBody();
        if (uiPatients == null)
            return new ArrayList<>();
        return Arrays.stream(uiPatients).map(patientBuilder::decode).collect(Collectors.toList());
    }

    public UiPatient getUiPatient(Long id) {
        try {
            return patientBuilder.decode(Objects.requireNonNull(restTemplate
                    .getForEntity(server + ":" + port + "/backend/patient/" + id, Patient.class)
                    .getBody()));
        } catch (HttpClientErrorException.NotFound error) {
            return null;
        }
    }

    public ResponseEntity<String> addUiPatient(UiPatient uiPatient) {
        try {
            return restTemplate.postForEntity(
                    server + ":" + port + "/backend/patient/",
                    patientBuilder.encode(uiPatient),
                    String.class
            );
        } catch (HttpClientErrorException.BadRequest e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public boolean deletePatient(Long id) {
        try {
            restTemplate.delete(server + ":" + port + "/backend/patient/" + id);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }

    public ResponseEntity<String> updatePatient(Long id, UiPatient uiPatient) {
        try {
            restTemplate.put(server + ":" + port + "/backend/patient/" + id, patientBuilder.encode(uiPatient), Patient.class);
            return ResponseEntity.ok("Patient updated");
        } catch (HttpClientErrorException.NotFound e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (HttpClientErrorException.BadRequest e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> editPatient(Long id, UiPatient uiPatient) {
        try {
            restTemplate.patchForObject(server + ":" + port + "/backend/patient/" + id, patientBuilder.encode(uiPatient), String.class);
            return ResponseEntity.ok("Patient successfully changed");
        } catch (HttpClientErrorException.NotFound e) {
            return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
        } catch (HttpClientErrorException.BadRequest e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
