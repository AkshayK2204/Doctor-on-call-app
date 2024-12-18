package com.akshay.project.DoctorOnCall.controller;


import com.akshay.project.DoctorOnCall.dtos.AppReqDTO;
import com.akshay.project.DoctorOnCall.dtos.DoctorDTO;
import com.akshay.project.DoctorOnCall.entity.Appointment;
import com.akshay.project.DoctorOnCall.entity.Availability;
import com.akshay.project.DoctorOnCall.entity.Doctor;
import com.akshay.project.DoctorOnCall.entity.Patient;
import com.akshay.project.DoctorOnCall.service.AppointmentService;
import com.akshay.project.DoctorOnCall.service.AvailabilityService;
import com.akshay.project.DoctorOnCall.service.DoctorService;
import com.akshay.project.DoctorOnCall.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Controller
@AllArgsConstructor
public class PatientController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final AvailabilityService availabilityService;


    @Operation(summary = "Displays patient's dashboard")
    @GetMapping("/user/{patient_id}/dashboard")
    public String getPatientHome(@PathVariable Long patient_id, HttpSession session, Model model) {
        String patientName = patientService.findById(patient_id).getName();
        model.addAttribute("patientName",patientName);
        return "patientHome";
    }

    @Operation(summary = "Displays all available doctors for a patient")
    @GetMapping("/user/{patient_id}/doctors")
    public String showAllDoctors(@PathVariable Long patient_id, Model model) {
        List<DoctorDTO> doctors = doctorService.getAllDoctors();
        model.addAttribute("patient_id",patient_id);
        model.addAttribute("doctorsList", doctors);
        return "doctorList";
    }


    @Operation(summary = "Shows form to book an appointment with a doctor")
    @GetMapping("/user/{patient_id}/doctors/{doctor_id}/appointment")
    public String showAppointmentForm(@PathVariable Long patient_id,@PathVariable Long doctor_id, Model model) {
        System.out.println("Appointment form loaded...");
        model.addAttribute("appReqDTO",new AppReqDTO());
        Doctor doctor = doctorService.findById(doctor_id);
        List<Availability> availabilityList = doctor.getAvailabilityList();
        model.addAttribute("availabilityList",availabilityList != null ? availabilityList : Collections.emptyList());
        assert availabilityList != null;
        model.addAttribute("doctor_id", doctor_id);
        return "appointmentForm";
    }

    @Operation(summary = "Books an appointment with a doctor")
    @PostMapping("/user/{patient_id}/doctors/{doctor_id}/appointment")
    public String bookAppointment(@PathVariable Long patient_id,@PathVariable Long doctor_id, @Valid @ModelAttribute("appReqDTO") AppReqDTO appReqDTO, Model model) {
        System.out.println("Entering bookAppointment controller method...");
        Doctor doctor = doctorService.findById(doctor_id);
        Patient patient = patientService.findById(patient_id);
        long app_id = appointmentService.bookAppointment(appReqDTO,doctor,patient);
        model.addAttribute("app_id",app_id);
        doctorService.updateAvailability(appointmentService.getAppointmentById(app_id));
        System.out.println("Appointment created  with app Id : " + app_id);
        return String.format("redirect:/user/%d/doctors/%d/appointment/book/%d", patient_id,doctor_id, app_id);
    }

    @Operation(summary = "Confirms the booking of an appointment")
    @GetMapping("/user/{patient_id}/doctors/{doctor_id}/appointment/book/{app_id}")
    public String appointmentConfirmation(@PathVariable Long patient_id,@PathVariable Long doctor_id, @PathVariable Long app_id, Model model) {
        Appointment appointment = appointmentService.getAppointmentById(app_id);
        System.out.println(appointment.toString());
        model.addAttribute("appointment", appointment);
        model.addAttribute("doctor_id", doctor_id);
        return "appointmentConfirmation";
    }

    @Operation(summary = "Displays all appointments of a patient")
    @GetMapping("/user/{patient_id}/appointments/view")
    public String viewAppointments(@PathVariable Long patient_id, Model model){
        List<Appointment> appList = appointmentService.findAppByPatient(patient_id);
        model.addAttribute("appList",appList);
        return "appointmentList";
    }


    @Operation(summary = "Shows form to edit an appointment")
    @GetMapping("/user/{patient_id}/appointments/{app_id}/edit")
    public String appUpdateForm(@PathVariable Long patient_id,@PathVariable Long app_id, Model model){
        Appointment appointment = appointmentService.getAppointmentById(app_id);
        model.addAttribute("patient_id",patient_id);
        model.addAttribute("doctor_id",appointment.getDoctor().getId());
        List<Availability> availabilityList = appointmentService.getAppointmentById(app_id).getDoctor().getAvailabilityList();
        model.addAttribute("availabilityList",availabilityList);
        AppReqDTO appUpdateDTO = new AppReqDTO();
        appUpdateDTO.setName(appointment.getName());
        appUpdateDTO.setAge(appointment.getAge());
        appUpdateDTO.setPhoneNumber(appointment.getPhoneNumber());
        appUpdateDTO.setAddress(appointment.getAddress());
        appUpdateDTO.setBloodType(appointment.getBloodType());
        model.addAttribute("appUpdateDTO", appUpdateDTO);
        return "appUpdateForm";
    }


    @Operation(summary = "Updates an existing appointment")
    @PostMapping("/user/{patient_id}/appointments/{app_id}/edit")
    public String updateAppointment(@PathVariable Long app_id,@PathVariable Long patient_id,@Valid @ModelAttribute("appUpdateDTO") AppReqDTO appUpdateDTO , Model model){
        Doctor doctor = appointmentService.findAppById(app_id).getDoctor();
        boolean isTimeSlotAvailable = availabilityService.validateOpenTime(doctor,appUpdateDTO.getDate(),appUpdateDTO.getStartTime());
        if(!isTimeSlotAvailable){
            model.addAttribute("message","The selected time slot is not available. Please choose another time slot.");
            return "appUpdateForm";
        }
        Appointment updatedAppointment =appointmentService.updateAppointment(app_id,appUpdateDTO);
        model.addAttribute("updatedAppointment", updatedAppointment);
        model.addAttribute("patient_id",patient_id);
        return "appUpdateConfirmation";
    }

    @Operation(summary = "Cancels an appointment")
    @GetMapping("/user/{patient_id}/appointments/{app_id}/cancel")
    public String cancelAppointment(@PathVariable Long patient_id,@PathVariable Long app_id, Model model){
        boolean isCancelled = appointmentService.cancelAppointment(app_id);
        doctorService.updateAvailability(appointmentService.getAppointmentById(app_id));
        if(isCancelled){
            model.addAttribute("cancellationMessage", "The appointment has been cancelled...");
        }
        else{
            model.addAttribute("cancellationMessage", "Cannot cancel appointment because it is already completed or cancelled ...");
        }
        return String.format("redirect:/user/%d/appointments/view",patient_id);
    }


    @Operation(summary = "Displays available time slots for a specific doctor")
    @GetMapping("/user/{patient_id}/doctor/{doctor_id}/open-times")
    public String getAvailableSlot(@PathVariable Long doctor_id, Model model){
        Doctor doctor = doctorService.findById(doctor_id);
        List<Availability> availabilityList = doctor.getAvailabilityList()
                .stream()
                .filter(availability ->
                        availability.getDate().isAfter(LocalDate.now())).toList();
        model.addAttribute("availabilityList",availabilityList);
        return "openTimesList";
    }


    @GetMapping("user/{patient_id}/appointments/{app_id}/video-call")
    public String showPatientVideoCall(
            @RequestParam Long appId,
            @RequestParam String accessKey,
            @RequestParam Long userId,
            @PathVariable Long app_id,
            @PathVariable Long patient_id,
            Model model) {


        model.addAttribute("appId", appId);
        model.addAttribute("accessKey", accessKey);
        model.addAttribute("userId", userId);

        return "videoCall";
    }
}

