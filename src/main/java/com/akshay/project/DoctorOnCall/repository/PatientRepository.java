package com.akshay.project.DoctorOnCall.repository;

import com.akshay.project.DoctorOnCall.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {

    //@Query("SELECT u FROM Patient p WHERE p.email = ?1")
    Optional<Patient> findByEmail(String email);
}
