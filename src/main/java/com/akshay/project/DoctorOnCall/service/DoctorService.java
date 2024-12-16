package com.akshay.project.DoctorOnCall.service;

import com.akshay.project.DoctorOnCall.dtos.DoctorDTO;
import com.akshay.project.DoctorOnCall.entity.Appointment;
import com.akshay.project.DoctorOnCall.entity.Availability;
import com.akshay.project.DoctorOnCall.entity.Doctor;
import com.akshay.project.DoctorOnCall.enums.APP_STATUS;
import com.akshay.project.DoctorOnCall.repository.AppointmentRepository;
import com.akshay.project.DoctorOnCall.repository.AvailabilityRepository;
import com.akshay.project.DoctorOnCall.repository.DoctorRepository;
import com.akshay.project.DoctorOnCall.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DoctorService extends UserService {

    private final AvailabilityRepository availabilityRepository;
    private final AppointmentRepository appointmentRepository;

    public DoctorService(BCryptPasswordEncoder bCryptPasswordEncoder,
                         DoctorRepository doctorRepository,
                         PatientRepository patientRepository,
                         AppointmentRepository appointmentRepository,
                         AvailabilityRepository availabilityRepository

    ){
        super(bCryptPasswordEncoder,doctorRepository,patientRepository);
        this.appointmentRepository = appointmentRepository;
        this.availabilityRepository = availabilityRepository;
    }

    @Override
    public Optional<Doctor> findByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }

    @Transactional
    public void registerDoctor(Doctor doctor) {
        if (findByEmail(doctor.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already registered...try Logging in");
        }
        doctor.setPassword(encodePassword(doctor, doctor.getPassword()));
        doctorRepository.save(doctor);
    }


    public List<DoctorDTO> getAllDoctors(){
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream()
                .map(doctor -> this.convertToDTO(doctor))
                .collect((Collectors.toList()));
    }

    public DoctorDTO convertToDTO(Doctor doctor) {
        DoctorDTO dto = new DoctorDTO();
        dto.setDoc_id(doctor.getId());
        dto.setDoc_name(doctor.getName());
        dto.setDoc_specialization(doctor.getSpecialization());
        return dto;
    }

    public Doctor findById(Long doctorId) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(doctorId);
        return doctorOptional.orElse(null);
    }




    /**
     * Updates the doctor's availability list based on the given appointment status.
     * If the appointment status is CANCELLED, adds the appointment's start time to the doctor's availability.
     * If the appointment status is not CANCELLED, removes the appointment's start time from the doctor's availability.
     * @param appointment the appointment to update availability for
     */
    @Transactional
    public void updateAvailability(Appointment appointment) {
        System.out.println("Updating availability...");
        Doctor doctor = appointment.getDoctor();
        List<Availability> availabilityList= doctor.getAvailabilityList();
        if (availabilityList == null) {
            throw new IllegalStateException("Doctor's availability list is null");
        }
        availabilityList.stream()
            .filter(availability ->
                    availability.getDate().equals(appointment.getDate()))
            .findFirst()
            .ifPresent(availability -> {
                List<LocalTime> openTimes = availability.getOpenTimes();
                if(appointment.getStatus().equals(APP_STATUS.CANCELLED)){
                    openTimes.add(appointment.getStartTime());
                }
                else {
                    if (!openTimes.remove(appointment.getStartTime())) {
                        throw new IllegalArgumentException("Time slot not available for removal");
                    }
                }
                availability.setOpenTimes(openTimes);
            });
        doctor.setAvailabilityList(availabilityList);
        doctorRepository.save(doctor);
    }
}

