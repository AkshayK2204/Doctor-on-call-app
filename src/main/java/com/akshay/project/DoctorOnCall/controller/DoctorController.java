package com.akshay.project.DoctorOnCall.controller;

import com.akshay.project.DoctorOnCall.dtos.OpenTimesDTO;
import com.akshay.project.DoctorOnCall.entity.Appointment;
import com.akshay.project.DoctorOnCall.entity.Availability;
import com.akshay.project.DoctorOnCall.entity.Doctor;
import com.akshay.project.DoctorOnCall.service.AppointmentService;
import com.akshay.project.DoctorOnCall.service.DoctorService;
import com.akshay.project.DoctorOnCall.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class DoctorController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;



    @Autowired
    public DoctorController(AppointmentService appointmentService, DoctorService doctorService, PatientService patientService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @Operation(summary = "Displays the doctor's dashboard with appointment statistics and details")
    @GetMapping("doctor/{doctor_id}/dashboard")
    public String getDoctorHome(@PathVariable Long doctor_id, Model model) {
        System.out.println("Loading doctor homepage...");
        System.out.println(doctor_id);
        Doctor doctor = doctorService.findById(doctor_id);
        model.addAttribute("doctorName",doctor.getName());
        List<Appointment> doctorAppointments = appointmentService.findBookedAppointmentByDoctor(doctor);
        model.addAttribute("doctorAppList", doctorAppointments);
        int totalAppointments = appointmentService.countAllAppointment(doctorAppointments);
        model.addAttribute("totalAppointments",totalAppointments);
        int completedAppointments = appointmentService.countCompletedAppointment(doctorAppointments);
        model.addAttribute("totalCompleted",completedAppointments);
        int pendingAppointments = appointmentService.countPendingAppointment(doctorAppointments);
        model.addAttribute("totalPending",pendingAppointments);
        return "dashboard";
    }


    @GetMapping("doctor/{doctor_id}/appointments/{app_id}/video-call")
    public String showDoctorVideoCall(@PathVariable Long app_id, Model model) {
        return "videoCall";
    }


    @Operation(summary = "Displays available time slots for a specific doctor")
    @GetMapping("/doctor/{doctor_id}/open-times/view")
    public String getAvailableSlot(@PathVariable Long doctor_id, Model model){
        Doctor doctor = doctorService.findById(doctor_id);
        List<Availability> availabilityList = doctor.getAvailabilityList();
        if (availabilityList == null || availabilityList.isEmpty()) {
            System.out.println("No availability found for doctor with ID: " + doctor_id);
        }
        System.out.println(availabilityList);
        model.addAttribute("availabilityList",availabilityList);
        return "openTimesList";
    }


    @Operation(summary = "Displays form for doctor to set available open times")
    @GetMapping("/doctor/{doctor_id}/open-times")
    public String openTimesForm(@PathVariable Long doctor_id,Model model){
        model.addAttribute("openTimesDTO",new OpenTimesDTO());
        model.addAttribute("doctor_id",doctor_id);
        return "openTimesForm";
    }

    @Operation(summary = "Saves the doctor's open times and redirects to the dashboard")
    @PostMapping("/doctor/{doctor_id}/open-times")
    public String addOpenTimes(@PathVariable Long doctor_id, @ModelAttribute OpenTimesDTO openTimesDTO,Model model){
        Doctor doctor = doctorService.findById(doctor_id);
        model.addAttribute("doctorName",doctor.getName());

        Availability availability = appointmentService.getOpenAppointments(openTimesDTO,doctor);
        model.addAttribute("availability",availability);

        return String.format("redirect:/doctor/{doctor_id}/open-times/view",doctor_id);
    }

}
