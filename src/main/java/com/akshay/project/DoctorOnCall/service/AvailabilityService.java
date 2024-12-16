package com.akshay.project.DoctorOnCall.service;

import com.akshay.project.DoctorOnCall.entity.Appointment;
import com.akshay.project.DoctorOnCall.entity.Availability;
import com.akshay.project.DoctorOnCall.entity.Doctor;
import com.akshay.project.DoctorOnCall.enums.APP_STATUS;
import com.akshay.project.DoctorOnCall.repository.AppointmentRepository;
import com.akshay.project.DoctorOnCall.repository.AvailabilityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AvailabilityService {

    protected final AvailabilityRepository availabilityRepository;
    protected final AppointmentRepository appointmentRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository,AppointmentRepository appointmentRepository) {
        this.availabilityRepository = availabilityRepository;
        this.appointmentRepository = appointmentRepository;

    }

    /**
     * Validates if the given doctor has the given open time on the given date, and if so, removes the time slot from the doctor's availability.
     * @param doctor the doctor to check
     * @param date the date to check
     * @param startTime the start time of the open time slot to check
     * @return true if the doctor has the given open time on the given date and the time slot was successfully removed, false otherwise
     */
    public boolean validateOpenTime(Doctor doctor, LocalDate date, LocalTime startTime) {
        Optional<Availability> optionalAvailability = availabilityRepository.findByDoctorAndDate(doctor,date);
        if(optionalAvailability.isEmpty()){
            return false;
        }
        else{
            Availability availability = optionalAvailability.get();
            if(!availability.getOpenTimes().contains(startTime)){
                return false;
            }
            else{
                availability.getOpenTimes().remove(startTime);
                availabilityRepository.save(availability);
                return true;
            }
        }
    }


    /**
     * Validates if the given open time exists for the given availability id, and if so, removes the time slot from the doctor's availability.
     * @param availabilityId the id of the availability to check
     * @param time the open time to check
     * @throws IllegalArgumentException if the given open time does not exist for the given availability id
     */
    public void deleteOpenTime(Long availabilityId, LocalTime time) {
        Optional<Availability> optionalAvailability = availabilityRepository.findById(availabilityId);
        if (optionalAvailability.isPresent()) {
            Availability availability = optionalAvailability.get();
            List<LocalTime> openTimes = availability.getOpenTimes();
            if (!openTimes.remove(time)) {
                throw new IllegalArgumentException("Time slot not available for removal");
            }
            availability.setOpenTimes(openTimes);
            availabilityRepository.save(availability);
        }
    }


    /**
     * Deletes the availability for the given id and marks all appointments that were made during this time as cancelled.
     * @param availabilityId the id of the availability to delete
     */
    public void deleteAvailability(Long availabilityId) {

        availabilityRepository.findById(availabilityId).ifPresent(availability -> {
            List<Appointment> appointments = appointmentRepository.findByDoctorAndDate(availability.getDoctor(),availability.getDate());
            appointments.forEach(appointment -> {
                appointment.setStatus(APP_STATUS.CANCELLED);
                appointmentRepository.save(appointment);
            });
        });
        availabilityRepository.deleteById(availabilityId);
    }

}
