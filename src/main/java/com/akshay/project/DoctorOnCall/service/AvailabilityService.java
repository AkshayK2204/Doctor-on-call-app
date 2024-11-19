package com.akshay.project.DoctorOnCall.service;

import com.akshay.project.DoctorOnCall.repository.AvailabilityRepository;
import org.springframework.stereotype.Service;

@Service
public class AvailabilityService {

    protected final AvailabilityRepository availabilityRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

//    public void validateOpenTime(LocalDate date, LocalTime startTime) {
//        Optional<Availability> optionalAvailability = availabilityRepository.findByDate(date);
//        if(optionalAvailability.isEmpty()){
//            throw new IllegalArgumentException("No availability found for the given date.");
//        }
//        else{
//            Availability availability = optionalAvailability.get();
//            if(!availability.getOpenTimes().contains(startTime)){
//                throw new IllegalArgumentException("Time slot not available for booking.");
//            }
//            else{
//                availability.getOpenTimes().remove(startTime);
//                availabilityRepository.save(availability);
//            }
//        }
//    }


}
