package com.example.demo.service;

import com.example.demo.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class PatientService {

    final private JdbcTemplate jdbcTemplate;
    final private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    PatientService(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private Patient mapRowToPatient(ResultSet rs, int rowNum) throws SQLException {
        return new Patient(
                rs.getLong("patient_id"),
                rs.getString("surname"),
                rs.getString("name"),
                rs.getString("middleName"),
                rs.getString("symptoms"),
                rs.getString("isHavingTipAbroad"),
                rs.getString("contactWithPatients")
        );
    }

    private Map<String, Object> mapPatientToParams(Patient patient) {
        Map<String, Object> params = new HashMap<>();
        params.put("surname", patient.getSurname());
        params.put("name", patient.getName());
        params.put("middleName", patient.getMiddleName());
        params.put("symptoms", patient.getSymptoms());
        params.put("isHavingTipAbroad", patient.getIsHavingTipAbroad());
        params.put("contactWithPatients", patient.getContactWithPatients());
        return params;
    }

    public List<Patient> findAll() {
        return new ArrayList<>(jdbcTemplate.query("select * from patient", this::mapRowToPatient));
    }

    public Patient find(Long id) throws EmptyResultDataAccessException {
        return namedParameterJdbcTemplate.queryForObject("select * from patient where patient_id = (:id)",
                Collections.singletonMap("id", id), this::mapRowToPatient);

    }

    public Long savePatient(Patient patient) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("patient")
                .usingGeneratedKeyColumns("patient_id");
        return simpleJdbcInsert.executeAndReturnKey(mapPatientToParams(patient)).longValue();
    }

    public int delete(Long id) {
        return namedParameterJdbcTemplate.update("delete from patient where patient_id = (:id)",
                Collections.singletonMap("id", id));
    }

    public Long put(Long id, Patient patient) {
        if (delete(id) == 0)
            throw new RuntimeException("Patient with id " + id + " not found");
        return savePatient(patient);
    }

    public void patch(Long id, Patient patient) {
        Patient patientBase = find(id);
        if (patientBase == null) {
            throw new RuntimeException("Patient with id " + id + " not found");
        }

        // Формирование sql запроса
        StringBuilder sqlBuilder = new StringBuilder();
        Map<String, Object> values = new HashMap<>();
        boolean first = true;
        boolean changed = false;

        // Валидация введенных данных
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Patient>> constraintViolations;

        if (patient.getName() != null) {
            String name = patient.getName();
            constraintViolations = validator.validateValue(Patient.class, "name", name);
            if (constraintViolations.iterator().hasNext()) {
                throw new RuntimeException("Patient has no valid name");
            }
            if (!patientBase.getName().equals(name)) {
                if (!first) {
                    sqlBuilder.append(", ");
                }
                first = false;
                changed = true;
                values.put("name", name);
                sqlBuilder.append("name = :name");
            }
        }

        if (patient.getSurname() != null) {
            String surname = patient.getSurname();
            constraintViolations = validator.validateValue(Patient.class, "surname", surname);
            if (constraintViolations.iterator().hasNext()) {
                throw new RuntimeException("Patient has no valid surname");
            }
            if (!patientBase.getSurname().equals(surname)) {
                if (!first) {
                    sqlBuilder.append(", ");
                }
                first = false;
                changed = true;
                values.put("surname", surname);
                sqlBuilder.append("surname = :surname");
            }
        }

        if (patient.getMiddleName() != null) {
            String middleName = patient.getMiddleName();
            constraintViolations = validator.validateValue(Patient.class, "middleName", middleName);
            if (constraintViolations.iterator().hasNext()) {
                throw new RuntimeException("Patient has no valid middleName");
            }
            if (!patientBase.getMiddleName().equals(middleName)) {
                if (!first) {
                    sqlBuilder.append(", ");
                }
                first = false;
                changed = true;
                values.put("middleName", middleName);
                sqlBuilder.append("middleName = :middleName");
            }
        }

        if (patient.getSymptoms() != null) {
            String symptoms = patient.getSymptoms();
            constraintViolations = validator.validateValue(Patient.class, "symptoms", symptoms);
            if (constraintViolations.iterator().hasNext()) {
                throw new RuntimeException("Patient has no valid symptoms");
            }
            if (!patientBase.getSymptoms().equals(symptoms)) {
                if (!first) {
                    sqlBuilder.append(", ");
                }
                first = false;
                changed = true;
                values.put("symptoms", symptoms);
                sqlBuilder.append("symptoms = :symptoms");
            }
        }

        if (patient.getIsHavingTipAbroad() != null) {
            String isHavingTipAbroad = patient.getIsHavingTipAbroad();
            constraintViolations = validator.validateValue(Patient.class, "isHavingTipAbroad", isHavingTipAbroad);
            if (constraintViolations.iterator().hasNext()) {
                throw new RuntimeException("Patient has no valid isHavingTipAbroad");
            }
            if (!patientBase.getIsHavingTipAbroad().equals(isHavingTipAbroad)) {
                if (!first) {
                    sqlBuilder.append(", ");
                }
                first = false;
                changed = true;
                values.put("isHavingTipAbroad", isHavingTipAbroad);
                sqlBuilder.append("isHavingTipAbroad = :isHavingTipAbroad");
            }
        }

        if (patient.getContactWithPatients() != null) {
            String contactWithPatients = patient.getContactWithPatients();
            constraintViolations = validator.validateValue(Patient.class, "contactWithPatients", contactWithPatients);
            if (constraintViolations.iterator().hasNext()) {
                throw new RuntimeException("Patient has no valid contactWithPatients");
            }
            if (!patientBase.getContactWithPatients().equals(contactWithPatients)) {
                if (!first) {
                    sqlBuilder.append(", ");
                }
                first = false;
                changed = true;
                values.put("contactWithPatients", contactWithPatients);
                sqlBuilder.append("contactWithPatients = :contactWithPatients");
            }
        }

        if (changed) {
            values.put("id", id);
            String sql = "update patient set " + sqlBuilder.toString() + " where patient_id = :id";
            namedParameterJdbcTemplate.update(sql, values);
        }
    }
}
