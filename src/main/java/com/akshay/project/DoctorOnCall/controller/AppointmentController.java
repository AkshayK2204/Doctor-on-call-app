package com.akshay.project.DoctorOnCall.controller;

import com.akshay.project.DoctorOnCall.entity.Appointment;
import com.akshay.project.DoctorOnCall.enums.APP_STATUS;
import com.akshay.project.DoctorOnCall.repository.AppointmentRepository;
import com.akshay.project.DoctorOnCall.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        List<Appointment> appointments = appointmentService.findAllAppointments();
        model.addAttribute("appointments", appointments);
        model.addAttribute("totalAppointments", appointmentRepository.count());
        model.addAttribute("totalBooked", appointmentRepository.countByStatus(APP_STATUS.SCHEDULED));
        model.addAttribute("totalPending", appointmentRepository.countByStatus(APP_STATUS.COMPLETED));
        return "dashboard";
    }



}
