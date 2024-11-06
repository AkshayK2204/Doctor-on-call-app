package com.akshay.project.DoctorOnCall.service;

import com.akshay.project.DoctorOnCall.dtos.AppReqDTO;
import com.akshay.project.DoctorOnCall.entity.Appointment;
import com.akshay.project.DoctorOnCall.entity.Doctor;
import com.akshay.project.DoctorOnCall.entity.Patient;
import com.akshay.project.DoctorOnCall.enums.APP_STATUS;
import com.akshay.project.DoctorOnCall.repository.AppointmentRepository;
import com.akshay.project.DoctorOnCall.repository.DoctorRepository;
import com.akshay.project.DoctorOnCall.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              DoctorRepository doctorRepository,
                              PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public Appointment getAppointmentById(Long appId) {
        return appointmentRepository.findById(appId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid appointment ID: " + appId));
    }



    public Long bookAppointment(AppReqDTO appReqDTO, Doctor doctor, Patient patient) {
        System.out.println("Entering bookAppointment service method");
        //System.out.println("Received AppReqDTO: " + appReqDTO.toString());
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setName(appReqDTO.getName());
        appointment.setAge(appReqDTO.getAge());
        appointment.setPhoneNumber(appReqDTO.getPhoneNumber());
        appointment.setAddress(appReqDTO.getAddress());
        appointment.setBloodType(appReqDTO.getBloodType());
        appointment.setStartTime(appReqDTO.getStartTime());
        appointment.setEndTime(appReqDTO.getEndTime());
        appointment.setDate(appReqDTO.getDate());
        appointment.setStatus(APP_STATUS.SCHEDULED);
        System.out.println("Appointment details before saving: " + appointment.toString());
        Appointment savedAppointment = appointmentRepository.save(appointment);

        if (savedAppointment.getAppId() != null) {
            System.out.println("Appointment successfully saved with ID: " + savedAppointment.getAppId());
        } else {
            throw new IllegalStateException("Appointment ID generation failed.");
        }
        return savedAppointment.getAppId();
    }


    public List<Appointment> findAllAppointments() {
        return appointmentRepository.findAll();
    }

    public boolean saveData(Appointment appointment) {
        appointmentRepository.save(appointment);
        return true;
    }

    public List<Appointment> findByPatient(Long patient_id){
        Optional<Patient> optPatient = patientRepository.findById(patient_id);
        if(optPatient.isPresent()){
            return appointmentRepository.findByPatient(optPatient.get());
        }
        else{
            throw new IllegalStateException("No appointment exists...");
        }


    }
}
