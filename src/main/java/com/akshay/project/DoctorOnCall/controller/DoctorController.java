package com.akshay.project.DoctorOnCall.controller;

import com.akshay.project.DoctorOnCall.entity.Appointment;
import com.akshay.project.DoctorOnCall.entity.Doctor;
import com.akshay.project.DoctorOnCall.service.AppointmentService;
import com.akshay.project.DoctorOnCall.service.DoctorService;
import com.akshay.project.DoctorOnCall.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class DoctorController {

    private AppointmentService appointmentService;
    private DoctorService doctorService;
    private PatientService patientService;

    @Autowired
    public void PatientController(AppointmentService appointmentService, DoctorService doctorService, PatientService patientService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @GetMapping("doctor/{doctor_id}/dashboard")
    public String getDoctorHome(@PathVariable Long doctor_id, Model model) {
        System.out.println("Loading doctor homepage...");
        System.out.println(model.getAttribute("doctor_id"));
        Doctor doctor = doctorService.findById(doctor_id);
        model.addAttribute("doctorName",doctor.getName());
        List<Appointment> doctorAppointments = appointmentService.findAppointmentByDoctor(doctor);
        model.addAttribute("doctorAppList", doctorAppointments);
        int totalAppointments = appointmentService.countAllAppointment(doctorAppointments);
        model.addAttribute("totalAppointments",totalAppointments);
        int completedAppointments = appointmentService.countCompletedAppointment(doctorAppointments);
        model.addAttribute("totalCompleted",completedAppointments);
        int pendingAppointments = appointmentService.countPendingAppointment(doctorAppointments);
        model.addAttribute("totalPending",pendingAppointments);
        return "dashboard";
    }
}
