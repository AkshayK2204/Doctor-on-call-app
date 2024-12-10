package com.akshay.project.DoctorOnCall.service;

import com.akshay.project.DoctorOnCall.entity.Doctor;
import com.akshay.project.DoctorOnCall.entity.Patient;
import com.akshay.project.DoctorOnCall.entity.User;
import com.akshay.project.DoctorOnCall.repository.DoctorRepository;
import com.akshay.project.DoctorOnCall.repository.PatientRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public abstract class UserService {
    protected final BCryptPasswordEncoder bCryptPasswordEncoder;
    protected final DoctorRepository doctorRepository;
    protected final PatientRepository patientRepository;


    protected UserService(BCryptPasswordEncoder bCryptPasswordEncoder, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public String encodePassword(User user, String plainPassword) {
        return bCryptPasswordEncoder.encode(plainPassword);
    }


    public Optional<? extends User> authenticateUser(String email, String password) {
        System.out.println("Authenticating user with email " + email);

        Optional<Doctor> doc = doctorRepository.findByEmail(email);
        if (doc.isPresent()) {
            System.out.println("Doctor found with email: " + email);
            User user1 = doc.get();
            if (bCryptPasswordEncoder.matches(password, user1.getPassword())) {
                System.out.println("Password is matching....");
                return Optional.of(user1);
            }
        }

        Optional<Patient> pat = patientRepository.findByEmail(email);
        if (pat.isPresent()) {
            System.out.println("Patient found with email: " + email);
            User user2 = pat.get();
            if (bCryptPasswordEncoder.matches(password, user2.getPassword())) {
                return Optional.of(user2);
            }
        }

        System.out.println("No user found with email: " + email);
        return Optional.empty();
    }

    public abstract Optional<? extends User> findByEmail (String email);

}
