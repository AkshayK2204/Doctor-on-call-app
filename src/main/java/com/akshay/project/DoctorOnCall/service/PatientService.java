package com.akshay.project.DoctorOnCall.service;

import com.akshay.project.DoctorOnCall.entity.Patient;
import com.akshay.project.DoctorOnCall.repository.DoctorRepository;
import com.akshay.project.DoctorOnCall.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

    @Service
    @Primary
    public class PatientService extends UserService {

        public PatientService(BCryptPasswordEncoder passwordEncoder, DoctorRepository doctorRepository, PatientRepository patientRepository) {
            super(passwordEncoder,doctorRepository,patientRepository);
        }

        @Override
        public Optional<Patient> findByEmail(String email) {
            return patientRepository.findByEmail(email);
        }


        public Patient findById(Long patientId) {
            Optional<Patient> patientOptional = patientRepository.findById(patientId);
            return patientOptional.orElse(null);
        }

        @Transactional
        public void registerPatient(Patient patient) {
            if (findByEmail(patient.getEmail()).isPresent()) {
                throw new IllegalStateException("Email already registered...");
            }
            patient.setPassword(encodePassword(patient, patient.getPassword()));
            patientRepository.save(patient);
        }

    }


