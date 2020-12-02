package com.example.demo.service;

import com.example.demo.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

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

    public Patient find(Long id) {
        try {
            return namedParameterJdbcTemplate.queryForObject("select * from patient where patient_id = (:id)",
                    Collections.singletonMap("id", id), this::mapRowToPatient);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
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
            return -1L;
        return savePatient(patient);
    }

    public boolean patch(Long id, Map<String, Object> patientInfo) {
        Patient patientBase = find(id);
        if (patientBase == null) {
            return false;
        }

        StringBuilder sqlBuilder = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Object> entry : patientInfo.entrySet()) {
            if (!first) {
                sqlBuilder.append(", ");
            }
            first = false;
            sqlBuilder.append(entry.getKey())
                    .append(" = :")
                    .append(entry.getKey());
        }

        String sql = "update patient set " + sqlBuilder.toString() + " where patient_id = :id";
        patientInfo.put("id", id);

        try {
            namedParameterJdbcTemplate.update(sql, patientInfo);
            return true;
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
