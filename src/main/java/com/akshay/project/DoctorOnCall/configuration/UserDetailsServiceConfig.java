package com.akshay.project.DoctorOnCall.configuration;

import com.akshay.project.DoctorOnCall.entity.Appointment;
import com.akshay.project.DoctorOnCall.entity.Doctor;
import com.akshay.project.DoctorOnCall.entity.Patient;
import com.akshay.project.DoctorOnCall.enums.*;
import com.akshay.project.DoctorOnCall.repository.AppointmentRepository;
import com.akshay.project.DoctorOnCall.repository.DoctorRepository;
import com.akshay.project.DoctorOnCall.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

@Configuration
public class UserDetailsServiceConfig {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDetailsServiceConfig(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    CommandLineRunner commandLineRunner(PatientRepository patientRepository,
                                        DoctorRepository doctorRepository,
                                        AppointmentRepository appointmentRepository) {
        return args -> {
            Doctor[] doctors = {
                    new Doctor("Alice Smith", "alice.smith@gmail.com", bCryptPasswordEncoder.encode("password123"), ROLE.ROLE_DOCTOR, GENDER.FEMALE, SPECIALIZATION.CARDIOLOGY),
                    new Doctor("Bob Jones", "bob.jones@gmail.com", bCryptPasswordEncoder.encode("password123"), ROLE.ROLE_DOCTOR, GENDER.MALE, SPECIALIZATION.NEUROLOGY),
                    new Doctor("Clara White", "clara.white@gmail.com", bCryptPasswordEncoder.encode("password123"), ROLE.ROLE_DOCTOR, GENDER.FEMALE, SPECIALIZATION.DERMATOLOGY),
                    new Doctor("David Green", "david.green@gmail.com", bCryptPasswordEncoder.encode("password123"), ROLE.ROLE_DOCTOR, GENDER.MALE, SPECIALIZATION.ORTHOPEDICS),
                    new Doctor("Emily Brown", "emily.brown@gmail.com", bCryptPasswordEncoder.encode("password123"), ROLE.ROLE_DOCTOR, GENDER.FEMALE, SPECIALIZATION.PEDIATRICS),
                    new Doctor("Doctor","doctor@gmail.com", bCryptPasswordEncoder.encode("doctor"),ROLE.ROLE_DOCTOR,GENDER.MALE,SPECIALIZATION.NEUROLOGY)
            };
            doctorRepository.saveAll(Arrays.asList(doctors));

            Patient[] patients = {
                    new Patient("John Doe", "john.doe@gmail.com", bCryptPasswordEncoder.encode("password123"), ROLE.ROLE_PATIENT, GENDER.MALE),
                    new Patient("Jane Smith", "jane.smith@gmail.com", bCryptPasswordEncoder.encode("password123"), ROLE.ROLE_PATIENT, GENDER.FEMALE),
                    new Patient("Michael Johnson", "michael.johnson@gmail.com", bCryptPasswordEncoder.encode("password123"), ROLE.ROLE_PATIENT, GENDER.MALE),
                    new Patient("Sarah Williams", "sarah.williams@gmail.com", bCryptPasswordEncoder.encode("password123"), ROLE.ROLE_PATIENT, GENDER.FEMALE),
                    new Patient("James Brown", "james.brown@gmail.com", bCryptPasswordEncoder.encode("password123"), ROLE.ROLE_PATIENT, GENDER.MALE),
                    new Patient("Patient","patient@gmail.com",bCryptPasswordEncoder.encode("patient"),ROLE.ROLE_PATIENT,GENDER.MALE)
            };
            patientRepository.saveAll(Arrays.asList(patients));

            Appointment[] appointments = {
                    new Appointment(doctors[0], patients[0], "John Doe", 30, "1234567890", "123 Main St", BLOOD_TYPE.A_POSITIVE, LocalDate.of(2024, 11, 15), LocalTime.of(9, 0), LocalTime.of(9, 15), APP_STATUS.SCHEDULED),
                    new Appointment(doctors[1], patients[1], "Jane Smith", 28, "1234567891", "456 Maple Ave", BLOOD_TYPE.B_POSITIVE, LocalDate.of(2024, 11, 15), LocalTime.of(10, 0), LocalTime.of(10, 15), APP_STATUS.SCHEDULED),
                    new Appointment(doctors[2], patients[2], "Michael Johnson", 35, "1234567892", "789 Oak Dr", BLOOD_TYPE.O_NEGATIVE, LocalDate.of(2024, 11, 16), LocalTime.of(11, 0), LocalTime.of(11, 15), APP_STATUS.CANCELLED),
                    new Appointment(doctors[3], patients[3], "Sarah Williams", 40, "1234567893", "101 Pine Ln", BLOOD_TYPE.AB_POSITIVE, LocalDate.of(2024, 11, 16), LocalTime.of(12, 0), LocalTime.of(12, 15), APP_STATUS.SCHEDULED),
                    new Appointment(doctors[4], patients[4], "James Brown", 25, "1234567894", "202 Birch St", BLOOD_TYPE.B_NEGATIVE, LocalDate.of(2024, 11, 17), LocalTime.of(13, 0), LocalTime.of(13, 15), APP_STATUS.SCHEDULED),
                    new Appointment(doctors[0], patients[1], "Jane Smith", 28, "1234567895", "456 Maple Ave", BLOOD_TYPE.B_POSITIVE, LocalDate.of(2024, 11, 17), LocalTime.of(14, 0), LocalTime.of(14, 15), APP_STATUS.SCHEDULED),
                    new Appointment(doctors[1], patients[2], "Michael Johnson", 35, "1234567896", "789 Oak Dr", BLOOD_TYPE.O_NEGATIVE, LocalDate.of(2024, 11, 18), LocalTime.of(15, 0), LocalTime.of(15, 15), APP_STATUS.SCHEDULED),
                    new Appointment(doctors[2], patients[3], "Sarah Williams", 40, "1234567897", "101 Pine Ln", BLOOD_TYPE.AB_POSITIVE, LocalDate.of(2024, 11, 18), LocalTime.of(16, 0), LocalTime.of(16, 15), APP_STATUS.SCHEDULED),
                    new Appointment(doctors[3], patients[4], "James Brown", 25, "1234567898", "202 Birch St", BLOOD_TYPE.B_NEGATIVE, LocalDate.of(2024, 11, 19),LocalTime.of(17, 0), LocalTime.of(17, 15), APP_STATUS.SCHEDULED),
                    new Appointment(doctors[4], patients[0], "John Doe", 30, "1234567899", "123 Main St", BLOOD_TYPE.A_POSITIVE, LocalDate.of(2024, 11, 19), LocalTime.of(18, 0), LocalTime.of(18, 15), APP_STATUS.SCHEDULED)
            };
            appointmentRepository.saveAll(Arrays.asList(appointments));
        };
    }
    //    @Bean
//    CommandLineRunner commandLineRunner(PatientRepository patientRepository,
//                                        DoctorRepository doctorRepository,
//                                        AppointmentRepository appointmentRepository
//                                        ) {
//        return args -> {
//
//            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//
//
//            Patient patient = new Patient();
//            //patient.setId(0L);
//            patient.setEmail("patient@gmail.com");
//            patient.setName("patient");
//            patient.setPassword(bCryptPasswordEncoder.encode("patient"));
//            patient.setRole(ROLE.ROLE_PATIENT);
//            patientRepository.save(patient);
//
//
//
//            Doctor doctor = new Doctor();
//            //doctor.setId(0L);
//            doctor.setName("doctor");
//            doctor.setEmail("doctor@gmail.com");
//            doctor.setPassword(bCryptPasswordEncoder.encode("doctor"));
//            doctor.setRole(ROLE.ROLE_DOCTOR);
//            doctorRepository.save(doctor);
//
//
//
//            Appointment appointment = new Appointment();
//            appointment.setDoctor(doctor);
//            appointment.setPatient(patient);
//            appointment.setName("John Doe");
//            appointment.setAge(22);
//            appointment.setPhoneNumber("1234567890");
//            appointment.setAddress("123 Main St, Cityville, ST 12345");
//            appointment.setBloodType(BLOOD_TYPE.B_POSITIVE);
//            appointment.setStartTime(LocalTime.parse("09:00"));
//            appointment.setEndTime(LocalTime.parse("10:00"));
//            appointment.setDate(LocalDate.parse("2024-12-24"));
//            appointment.setStatus(APP_STATUS.SCHEDULED);
//            appointmentRepository.save(appointment);
//        };
//    }





}
