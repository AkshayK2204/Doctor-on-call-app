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
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@AllArgsConstructor
public class AppointmentService {

    @Autowired
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AvailabilityRepository availabilityRepository;

    public Appointment getAppointmentById(Long appId) {
        return appointmentRepository.findById(appId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid appointment ID: " + appId));
    }


    @Transactional
    public Long bookAppointment(AppReqDTO appReqDTO, Doctor doctor, Patient patient) {
        Appointment appointment = getAppointment(appReqDTO, doctor, patient);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        if (savedAppointment.getAppId() != null) {
            return savedAppointment.getAppId();
        } else {
            throw new IllegalStateException("Appointment ID generation failed...");
        }
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

        String accessKey = String.format("%04d", new Random().nextInt(10000));
        appointment.setAccessKey(accessKey);
        return appointment;
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

    public List<Appointment> findBookedAppointmentByDoctor(Doctor doctor) {
        return appointmentRepository.findByDoctor(doctor)
                .stream()
                .filter(appointment ->
                        appointment.getStatus() == APP_STATUS.SCHEDULED)
                .toList();
    }

    public int countAllAppointment(List<Appointment> doctorAppointments){
        return doctorAppointments.size();
    }

    public int countCompletedAppointment(List<Appointment> doctorAppointments){
        return (int) doctorAppointments.stream().filter(appointment -> appointment.getStatus()==APP_STATUS.COMPLETED).count();
    }

    public int countPendingAppointment(List<Appointment> doctorAppointments) {
        return countAllAppointment(doctorAppointments) - countCompletedAppointment(doctorAppointments);
    }




    /**
     * Updates an existing appointment by id.
     * If the appointment is already cancelled or completed, the update is not allowed and null is returned.
     * Otherwise, the availability of the doctor is updated and the appointment is updated.
     *
     * @param appId the id of the appointment to update
     * @param appUpdateDTO the DTO containing the updated information for the appointment
     * @return the updated appointment if successful, null otherwise
     */
    @Transactional
    public Appointment updateAppointment(Long appId,AppReqDTO appUpdateDTO) {
        Appointment appointment = appointmentRepository.findByAppId(appId);

        if(appointment.getStatus()==APP_STATUS.CANCELLED || appointment.getStatus()==APP_STATUS.COMPLETED ){
            return null;
        }
        availabilityRepository.findByDoctorAndDate(appointment.getDoctor(), appointment.getDate()).ifPresent(availability ->{
            List<LocalTime> openTimes = availability.getOpenTimes();
            openTimes.add(appointment.getStartTime());
            openTimes.remove(appUpdateDTO.getStartTime());
            availability.setOpenTimes(openTimes);
            availabilityRepository.save(availability);
        });

        appointment.setName(appUpdateDTO.getName());
        appointment.setAge(appUpdateDTO.getAge());
        appointment.setPhoneNumber(appUpdateDTO.getPhoneNumber());
        appointment.setAddress(appUpdateDTO.getAddress());
        appointment.setBloodType(appUpdateDTO.getBloodType());
        appointment.setStartTime(appUpdateDTO.getStartTime());
        appointment.setEndTime(appUpdateDTO.getStartTime().plusMinutes(30));
        appointment.setDate(appUpdateDTO.getDate());
        appointment.setStatus(APP_STATUS.SCHEDULED);
        appointmentRepository.save(appointment);

        return appointmentRepository.findByAppId(appId);

    }


    /**
     * Cancels an existing appointment by id.
     * If the appointment is already cancelled or completed, the cancellation is not allowed and false is returned.
     * Otherwise, the appointment is updated to cancelled status.
     *
     * @param appId the id of the appointment to cancel
     * @return true if the cancellation is successful, false otherwise
     */
    public boolean cancelAppointment(Long appId) {
        Appointment appointment= appointmentRepository.findByAppId(appId);

        if(appointment.getStatus()==APP_STATUS.CANCELLED || appointment.getStatus()==APP_STATUS.COMPLETED ){
            return false;
        }
        appointment.setStatus(APP_STATUS.CANCELLED);
        appointmentRepository.save(appointment);
        return true;
    }





    /**
     * Creates and returns an Availability object with open time slots for a given doctor on a specified date.
     * If availability already exists for the given doctor and date, it deletes the existing record before creating a new one.
     *
     * @param openTimesDTO the DTO containing the date and time range for open appointments
     * @param doctor the doctor for whom the availability is being set
     * @return the newly created Availability object
     */
    @Transactional
    public Availability getOpenAppointments(OpenTimesDTO openTimesDTO, Doctor doctor) {
        LocalDate date = openTimesDTO.getDate();
        LocalTime start = openTimesDTO.getStartTime();
        LocalTime end = openTimesDTO.getEndTime();

        List<LocalTime> openTimesList = new ArrayList<>();
        while (!start.isAfter(end.minusMinutes(30))) {
            openTimesList.add(start);
            start = start.plusMinutes(30);
        }

        if (availabilityRepository.existsByDateAndDoctor(date, doctor)) {
            availabilityRepository.deleteByDateAndDoctor(date, doctor);
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

    public Appointment findAppById(Long appId) {
        return appointmentRepository.findByAppId(appId);
    }
}
