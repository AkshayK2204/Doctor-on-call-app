package com.akshay.project.DoctorOnCall.repository;

import com.akshay.project.DoctorOnCall.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    Optional<Availability> findByDate(LocalDate date);
}
