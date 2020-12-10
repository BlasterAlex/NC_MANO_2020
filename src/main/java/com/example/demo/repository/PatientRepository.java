package com.example.demo.repository;

import com.example.demo.domain.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Long> {

    List<Patient> findAll();

    Optional<Patient> findPatientById(Long id);

    @Override
    Patient save(Patient patient);

    @Override
    void deleteById(Long id);
}
