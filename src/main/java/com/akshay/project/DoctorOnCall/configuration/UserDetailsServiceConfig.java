package com.akshay.project.DoctorOnCall.configuration;

import com.akshay.project.DoctorOnCall.entity.Appointment;
import com.akshay.project.DoctorOnCall.entity.Doctor;
import com.akshay.project.DoctorOnCall.entity.Patient;
import com.akshay.project.DoctorOnCall.enums.APP_STATUS;
import com.akshay.project.DoctorOnCall.enums.BLOOD_TYPE;
import com.akshay.project.DoctorOnCall.enums.ROLE;
import com.akshay.project.DoctorOnCall.repository.AppointmentRepository;
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

import java.time.LocalDate;
import java.time.LocalTime;

@Configuration
public class UserDetailsServiceConfig {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDetailsServiceConfig(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Bean
    CommandLineRunner commandLineRunner(PatientRepository patientRepository,
                                        DoctorRepository doctorRepository,
                                        AppointmentRepository appointmentRepository
                                        ) {
        return args -> {

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


            Patient patient = new Patient();
            //patient.setId(0L);
            patient.setEmail("patient@gmail.com");
            patient.setName("patient");
            patient.setPassword(bCryptPasswordEncoder.encode("patient"));
            patient.setRole(ROLE.ROLE_PATIENT);
            patientRepository.save(patient);



            Doctor doctor = new Doctor();
            //doctor.setId(0L);
            doctor.setName("doctor");
            doctor.setEmail("doctor@gmail.com");
            doctor.setPassword(bCryptPasswordEncoder.encode("doctor"));
            doctor.setRole(ROLE.ROLE_DOCTOR);
            doctorRepository.save(doctor);



            Appointment appointment = new Appointment();
            appointment.setDoctor(doctor);
            appointment.setPatient(patient);
            appointment.setName("John Doe");
            appointment.setAge(22);
            appointment.setPhoneNumber("1234567890");
            appointment.setAddress("123 Main St, Cityville, ST 12345");
            appointment.setBloodType(BLOOD_TYPE.B_POSITIVE);
            appointment.setStartTime(LocalTime.parse("09:00"));
            appointment.setEndTime(LocalTime.parse("10:00"));
            appointment.setDate(LocalDate.parse("2024-12-24"));
            appointment.setStatus(APP_STATUS.SCHEDULED);
            appointmentRepository.save(appointment);
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
