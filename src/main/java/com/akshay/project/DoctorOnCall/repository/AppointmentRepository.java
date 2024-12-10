package com.akshay.project.DoctorOnCall.repository;

import com.akshay.project.DoctorOnCall.entity.Appointment;
import com.akshay.project.DoctorOnCall.entity.Doctor;
import com.akshay.project.DoctorOnCall.entity.Patient;
import com.akshay.project.DoctorOnCall.enums.APP_STATUS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    long countByStatus(APP_STATUS status);

    Appointment findByAppId(Long appId);

    List<Appointment> findByDoctor(Doctor doctor);

    Optional<Appointment> findByAppIdAndStatus(Long appId, APP_STATUS status);

    List<Appointment> findByPatient(Patient patient);

    List<Appointment> findByDoctorAndDate(Doctor doctor, LocalDate date);

}

