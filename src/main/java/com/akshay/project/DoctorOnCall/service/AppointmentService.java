package com.akshay.project.DoctorOnCall.service;

import com.akshay.project.DoctorOnCall.dtos.AppReqDTO;
import com.akshay.project.DoctorOnCall.dtos.OpenTimesDTO;
import com.akshay.project.DoctorOnCall.entity.Appointment;
import com.akshay.project.DoctorOnCall.entity.Availability;
import com.akshay.project.DoctorOnCall.entity.Doctor;
import com.akshay.project.DoctorOnCall.entity.Patient;
import com.akshay.project.DoctorOnCall.enums.APP_STATUS;
import com.akshay.project.DoctorOnCall.repository.AppointmentRepository;
import com.akshay.project.DoctorOnCall.repository.AvailabilityRepository;
import com.akshay.project.DoctorOnCall.repository.DoctorRepository;
import com.akshay.project.DoctorOnCall.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class AppointmentService {

    @Autowired
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AvailabilityRepository availabilityRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              DoctorRepository doctorRepository,
                              PatientRepository patientRepository, AvailabilityRepository availabilityRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.availabilityRepository = availabilityRepository;
    }

    public Appointment getAppointmentById(Long appId) {
        return appointmentRepository.findById(appId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid appointment ID: " + appId));
    }


    @Transactional
    public Long bookAppointment(AppReqDTO appReqDTO, Doctor doctor, Patient patient) {
        System.out.println("Entering bookAppointment service method");
        Appointment appointment = getAppointment(appReqDTO, doctor, patient);
        System.out.println("Appointment details before saving: " + appointment.toString());
        Appointment savedAppointment = appointmentRepository.save(appointment);

        if (savedAppointment.getAppId() != null) {
            System.out.println("Appointment successfully saved with ID: " + savedAppointment.getAppId());
        } else {
            throw new IllegalStateException("Appointment ID generation failed.");
        }
        return savedAppointment.getAppId();
    }

    private static Appointment getAppointment(AppReqDTO appReqDTO, Doctor doctor, Patient patient) {
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setName(appReqDTO.getName());
        appointment.setAge(appReqDTO.getAge());
        appointment.setPhoneNumber(appReqDTO.getPhoneNumber());
        appointment.setAddress(appReqDTO.getAddress());
        appointment.setBloodType(appReqDTO.getBloodType());
        appointment.setStartTime(appReqDTO.getStartTime());
        appointment.setEndTime(appReqDTO.getStartTime().plusMinutes(30));
        appointment.setDate(appReqDTO.getDate());
        appointment.setStatus(APP_STATUS.SCHEDULED);
        return appointment;
    }


    public List<Appointment> findAllAppointments() {
        return appointmentRepository.findAll();
    }

    public boolean saveData(Appointment appointment) {
        appointmentRepository.save(appointment);
        return true;
    }

    public List<Appointment> findAppByPatient(Long patient_id){
        Optional<Patient> optPatient = patientRepository.findById(patient_id);
        if(optPatient.isPresent()){
            return appointmentRepository.findByPatient(optPatient.get());
        }
        else{
            throw new IllegalStateException("No appointment exists...");
        }
    }

    public List<Appointment> findAppByDoctor(Long doctorId) {
        Optional<Doctor> doctor = doctorRepository.findById(doctorId);
        if(doctor.isPresent()){
            return appointmentRepository.findByDoctor(doctor.get());
        }
        else{
            throw new IllegalStateException("No appointment exists...");
        }
    }

//    public Map<LocalDate,List<LocalTime>> findOpenAppointmentMapByDoctor(Doctor doctor) {
//        return doctor.getAvailabilityMap();
//    }

    public List<Appointment> findBookedAppointmentByDoctor(Doctor doctor) {
        return appointmentRepository.findByDoctor(doctor)
                .stream()
                .filter(appointment ->
                        appointment.getStatus() == APP_STATUS.SCHEDULED)
                .toList();
    }

    public int countAllAppointment(List<Appointment> doctorAppointments){
        return doctorAppointments.stream().filter(appointment -> appointment.getStatus()!=APP_STATUS.OPEN).toList().size();
    }

    public int countCompletedAppointment(List<Appointment> doctorAppointments){
        return (int) doctorAppointments.stream().filter(appointment -> appointment.getStatus()==APP_STATUS.COMPLETED).count();
    }

    public int countPendingAppointment(List<Appointment> doctorAppointments) {
        return countAllAppointment(doctorAppointments) - countCompletedAppointment(doctorAppointments);
    }

    @Transactional
    public Appointment updateAppointment(Long appId,AppReqDTO appUpdateDTO) {
        Appointment appointment = appointmentRepository.findByAppId(appId);
        appointment.setName(appUpdateDTO.getName());
        appointment.setAge(appUpdateDTO.getAge());
        appointment.setPhoneNumber(appUpdateDTO.getPhoneNumber());
        appointment.setAddress(appUpdateDTO.getAddress());
        appointment.setBloodType(appUpdateDTO.getBloodType());
        appointment.setStartTime(appUpdateDTO.getStartTime());
        appointment.setEndTime(appUpdateDTO.getEndTime());
        appointment.setDate(appUpdateDTO.getDate());
        appointment.setStatus(APP_STATUS.SCHEDULED);
        appointmentRepository.save(appointment);
        return appointmentRepository.findByAppId(appId);

    }


    public boolean cancelAppointment(Long appId) {
        Appointment appointment= appointmentRepository.findByAppId(appId);

        if(appointment.getStatus()==APP_STATUS.CANCELLED || appointment.getStatus()==APP_STATUS.COMPLETED ){
            return false;
        }
        appointment.setStatus(APP_STATUS.CANCELLED);
        appointmentRepository.save(appointment);
        return true;
    }

    @Transactional
    public Availability getOpenAppointments(OpenTimesDTO openTimesDTO, Doctor doctor) {

//        HashMap<LocalDate, List<LocalTime>> availabilityMap = Optional.ofNullable(doctor.getAvailabilityMap()).orElse(new HashMap<>());

//        List<Appointment> openAppointments = new ArrayList<>();
        LocalDate date = openTimesDTO.getDate();
        LocalTime start = openTimesDTO.getStartTime();
        LocalTime end = openTimesDTO.getEndTime();


//        while (!start.isAfter(end.minusMinutes(30))) {
//            Appointment appointment = new Appointment();
//            appointment.setDoctor(doctor);
//            appointment.setStartTime(start);
//            appointment.setEndTime(start.plusMinutes(30));
//            appointment.setDate(day);
//            appointment.setStatus(APP_STATUS.OPEN);
//            openAppointments.add(appointment);
//            start = start.plusMinutes(30);
//        }
        List<LocalTime> openTimesList = new ArrayList<>();
        while (!start.isAfter(end.minusMinutes(30))) {
            openTimesList.add(start);
            start = start.plusMinutes(30);
        }


        Availability availability = new Availability();
        if (!openTimesList.isEmpty()) {
            availability.setDoctor(doctor);
            availability.setDate(date);
            availability.setOpenTimes(openTimesList);
            availabilityRepository.save(availability);
        }
        return availability;
    }

    public void saveAll(List<Appointment> openAppointmentList) {
        appointmentRepository.saveAll(openAppointmentList);
    }


}
