package com.akshay.project.DoctorOnCall.controller;


import com.akshay.project.DoctorOnCall.dtos.AppReqDTO;
import com.akshay.project.DoctorOnCall.dtos.DoctorDTO;
import com.akshay.project.DoctorOnCall.entity.Appointment;
import com.akshay.project.DoctorOnCall.entity.Doctor;
import com.akshay.project.DoctorOnCall.entity.Patient;
import com.akshay.project.DoctorOnCall.service.AppointmentService;
import com.akshay.project.DoctorOnCall.service.DoctorService;
import com.akshay.project.DoctorOnCall.service.PatientService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class PatientController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    public PatientController(AppointmentService appointmentService, DoctorService doctorService, PatientService patientService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @GetMapping("/user/{patient_id}/dashboard")
    public String getPatientHome(@PathVariable Long patient_id, HttpSession session, Model model) {
        System.out.println("Session ID: " + session.getId());
        System.out.println("Session Timeout: " + session.getMaxInactiveInterval());
        return "patientHome";
    }

    @GetMapping("/user/{patient_id}/doctors")
    public String showAllDoctors(@PathVariable Long patient_id, Model model) {
        List<DoctorDTO> doctors = doctorService.getAllDoctors();
        model.addAttribute("patient_id",patient_id);
        model.addAttribute("doctorsList", doctors);
        return "doctorList";
    }

    @GetMapping("/user/{patient_id}/doctors/{doctor_id}/appointment")
    public String showAppointmentForm(@PathVariable Long patient_id,@PathVariable Long doctor_id, Model model) {
        System.out.println("Appointment form loaded...");
        model.addAttribute("appReqDTO",new AppReqDTO());
//        model.addAttribute("appointment", new AppReqDTO());
        model.addAttribute("doctor_id", doctor_id);
        return "appointmentForm";
    }

    @PostMapping("/user/{patient_id}/doctors/{doctor_id}/appointment")
    public String bookAppointment(@PathVariable Long patient_id,@PathVariable Long doctor_id, @Valid @ModelAttribute("appReqDTO") AppReqDTO appReqDTO, Model model,BindingResult result) {
        System.out.println("Entering bookAppointment controller method");
        //appReqDTO.setEndTime(appReqDTO.getStartTime().plusMinutes(15));
        //System.out.println("End time set to: " + appReqDTO.getEndTime());
//        appReqDTO.setDoctorId(doctor_id);
//        appReqDTO.setPatientId(patient_id);

        Doctor doctor = doctorService.findById(doctor_id);
        Patient patient = patientService.findById(patient_id);
        long app_id = appointmentService.bookAppointment(appReqDTO,doctor,patient);
        model.addAttribute("app_id",app_id);
        System.out.println("Appointment created  with app Id : " + app_id);
        return String.format("redirect:/user/%d/doctors/%d/appointment/book/%d", patient_id,doctor_id, app_id);
    }

    @GetMapping("/user/{patient_id}/doctors/{doctor_id}/appointment/book/{app_id}")
    public String appointmentConfirmation(@PathVariable Long patient_id,@PathVariable Long doctor_id, @PathVariable Long app_id, Model model) {
        Appointment appointment = appointmentService.getAppointmentById(app_id);
        System.out.println(appointment.toString());
        model.addAttribute("appointment", appointment);
        model.addAttribute("doctor_id", doctor_id);
        //model.addAttribute("app_id", app_id);
        return "appointmentConfirmation";
    }

    @GetMapping("/user/{patient_id}/appointments/view")
    public String viewAppointments(@PathVariable Long patient_id, Model model){
        List<Appointment> appList = appointmentService.findByPatient(patient_id);
        model.addAttribute("appList",appList);
        return "appointmentList";
    }


}



