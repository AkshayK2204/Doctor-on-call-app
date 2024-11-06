package com.akshay.project.DoctorOnCall.repository;

import com.akshay.project.DoctorOnCall.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Long> {
    //@Query("SELECT u FROM Doctor d WHERE d.email = ?1")
    Optional<Doctor> findByEmail(String email);

}
