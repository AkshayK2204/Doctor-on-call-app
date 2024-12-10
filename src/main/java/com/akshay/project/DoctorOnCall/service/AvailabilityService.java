package com.akshay.project.DoctorOnCall.service;

import com.akshay.project.DoctorOnCall.entity.Availability;
import com.akshay.project.DoctorOnCall.entity.Doctor;
import com.akshay.project.DoctorOnCall.repository.AvailabilityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class AvailabilityService {

    protected final AvailabilityRepository availabilityRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

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
}
