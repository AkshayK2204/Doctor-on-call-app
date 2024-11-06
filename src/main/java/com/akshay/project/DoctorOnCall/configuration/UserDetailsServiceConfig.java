package com.akshay.project.DoctorOnCall.configuration;

import com.akshay.project.DoctorOnCall.entity.Doctor;
import com.akshay.project.DoctorOnCall.entity.Patient;
import com.akshay.project.DoctorOnCall.enums.ROLE;
import com.akshay.project.DoctorOnCall.repository.DoctorRepository;
import com.akshay.project.DoctorOnCall.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserDetailsServiceConfig {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDetailsServiceConfig(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Bean
    CommandLineRunner commandLineRunner(PatientRepository patientRepository,
                                        DoctorRepository doctorRepository
                                        //UserRepository userRepository,
                                        //BCryptPasswordEncoder bCryptPasswordEncoder
                                        ) {
        return args -> {

            Patient patient = new Patient();
            //patient.setId(0L);
            patient.setEmail("patient@gmail.com");
            patient.setName("patient");
            patient.setPassword(bCryptPasswordEncoder.encode("patient"));
            patient.setRole(ROLE.ROLE_PATIENT);

            Doctor doctor = new Doctor();
            //doctor.setId(0L);
            doctor.setName("doctor");
            doctor.setEmail("doctor@gmail.com");
            doctor.setPassword(bCryptPasswordEncoder.encode("doctor"));
            doctor.setRole(ROLE.ROLE_DOCTOR);


            patientRepository.save(patient);
            doctorRepository.save(doctor);
        };
    }




    //@Bean
    public UserDetailsService userDetailsService() {
        System.out.println("User Details Service invoked...");
        UserDetails user = User.builder()
                .username("patient@gmail.com")
                .password(bCryptPasswordEncoder.encode("patient"))
                .roles("PATIENT")
                .build();

        UserDetails doctor = User.builder()
                .username("doctor@gmail.com")
                .password(bCryptPasswordEncoder.encode("doctor"))
                .roles("DOCTOR")
                .build();

        UserDetails admin = User.builder()
                .username("admin@gmail.com")
                .password(bCryptPasswordEncoder.encode("admin"))
                .roles("ADMIN","DOCTOR","PATIENT")
                .build();


        return new InMemoryUserDetailsManager(user, doctor, admin);
    }
}
