package com.example.demo.backend.repository;

import com.example.demo.backend.domain.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Long> {

  List<Patient> findAll();

  Optional<Patient> findPatientById(Long id);

}
