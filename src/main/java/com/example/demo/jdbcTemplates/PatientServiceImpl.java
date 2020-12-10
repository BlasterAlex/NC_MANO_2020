package com.example.demo.jdbcTemplates;

import com.example.demo.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
@Profile("!test")
public class PatientServiceImpl implements PatientService {

    final private JdbcTemplate jdbcTemplate;
    final private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    PatientServiceImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
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
        params.put("isHavingTripAbroad", patient.getIsHavingTripAbroad());
        params.put("contactWithPatients", patient.getContactWithPatients());
        return params;
    }

    @Override
    public List<Patient> findAll() {
        return new ArrayList<>(jdbcTemplate.query("select * from patient", this::mapRowToPatient));
    }

    @Override
    public Patient find(Long id) throws EmptyResultDataAccessException {
        return namedParameterJdbcTemplate.queryForObject("select * from patient where patient_id = (:id)",
                Collections.singletonMap("id", id), this::mapRowToPatient);

    }

    @Override
    public Long savePatient(Patient patient) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("patient")
                .usingGeneratedKeyColumns("patient_id");
        return simpleJdbcInsert.executeAndReturnKey(mapPatientToParams(patient)).longValue();
    }

    @Override
    public int delete(Long id) {
        return namedParameterJdbcTemplate.update("delete from patient where patient_id = (:id)",
                Collections.singletonMap("id", id));
    }

    @Override
    public Long put(Long id, Patient patient) {
        if (delete(id) == 0)
            throw new RuntimeException("Patient with id " + id + " not found");
        return savePatient(patient);
    }

    @Override
    public void patch(Long id, Patient patient) {
        Patient patientBase = find(id);
        if (patientBase == null) {
            throw new RuntimeException("Patient with id " + id + " not found");
        }

        // Формирование sql запроса
        StringBuilder sqlBuilder = new StringBuilder();
        boolean first = true;
        boolean changed = false;

        // Валидация введенных данных
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Patient>> constraintViolations;

        // Перебор полей
        Map<String, Object> patientInfo = mapPatientToParams(patient);
        patientInfo.values().removeIf(Objects::isNull);
        for (Map.Entry<String, Object> entry : patientInfo.entrySet()) {
            constraintViolations = validator.validateValue(Patient.class, entry.getKey(), entry.getValue());
            if (constraintViolations.iterator().hasNext()) {
                throw new RuntimeException("Patient has no valid " + entry.getKey());
            }
            try {
                Field f = patientBase.getClass().getDeclaredField(entry.getKey());
                f.setAccessible(true);
                if (!f.get(patientBase).equals(entry.getValue())) {
                    if (!first) {
                        sqlBuilder.append(", ");
                    }
                    first = false;
                    changed = true;
                    sqlBuilder.append(entry.getKey())
                            .append(" = :")
                            .append(entry.getKey());
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                System.err.println(e.getMessage());
            }
        }

        if (changed) {
            patientInfo.put("id", id);
            String sql = "update patient set " + sqlBuilder.toString() + " where patient_id = :id";
            namedParameterJdbcTemplate.update(sql, patientInfo);
        }
    }
}
