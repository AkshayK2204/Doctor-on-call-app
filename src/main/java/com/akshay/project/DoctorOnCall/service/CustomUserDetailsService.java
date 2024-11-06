package com.akshay.project.DoctorOnCall.service;

import com.akshay.project.DoctorOnCall.entity.Doctor;
import com.akshay.project.DoctorOnCall.entity.Patient;
import com.akshay.project.DoctorOnCall.repository.DoctorRepository;
import com.akshay.project.DoctorOnCall.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Patient> patient = patientRepository.findByEmail(username);
        if (patient.isPresent()) {
            return new User(
                    patient.get().getEmail(),
                    patient.get().getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority(patient.get().getRole().name()))
            );
        }

        Optional<Doctor> doctor = doctorRepository.findByEmail(username);
        if (doctor.isPresent()) {
            return new User(
                    doctor.get().getEmail(),
                    doctor.get().getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority(doctor.get().getRole().name()))
            );
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
