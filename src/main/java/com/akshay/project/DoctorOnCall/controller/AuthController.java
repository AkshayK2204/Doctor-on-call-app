package com.akshay.project.DoctorOnCall.controller;

import com.akshay.project.DoctorOnCall.dtos.LoginDTO;
import com.akshay.project.DoctorOnCall.dtos.RegisterDTO;
import com.akshay.project.DoctorOnCall.entity.Doctor;
import com.akshay.project.DoctorOnCall.entity.Patient;
import com.akshay.project.DoctorOnCall.entity.User;
import com.akshay.project.DoctorOnCall.enums.ROLE;
import com.akshay.project.DoctorOnCall.service.CustomUserDetailsService;
import com.akshay.project.DoctorOnCall.service.DoctorService;
import com.akshay.project.DoctorOnCall.service.PatientService;
import com.akshay.project.DoctorOnCall.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    private final DoctorService doctorService;
    private final PatientService patientService;
    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public AuthController(DoctorService doctorService, PatientService patientService, UserService userService, CustomUserDetailsService customUserDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.userService = userService;
        this.customUserDetailsService = customUserDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Operation(summary = "Show registration form")
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        System.out.println("Registration Form loaded...");
        model.addAttribute("user", new RegisterDTO());
        return "register";
    }

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegisterDTO registerDTO, Model model) {
        model.addAttribute("user", registerDTO);
        System.out.println("Register method triggered");
        try {
            System.out.println("Registering user " + registerDTO.getReg_email());
            if (registerDTO.getReg_role() == ROLE.ROLE_DOCTOR) {
                Doctor doctor = new Doctor();
                System.out.println("Doctor registering...");
                doctor.setName(registerDTO.getReg_name());
                doctor.setEmail(registerDTO.getReg_email());
                doctor.setPassword(registerDTO.getReg_password());
                doctor.setRole(ROLE.ROLE_DOCTOR);
                doctorService.registerDoctor(doctor);
                return "redirect:/customLogin?success";
            } else if (registerDTO.getReg_role() == ROLE.ROLE_PATIENT) {
                Patient patient = new Patient();
                System.out.println("Patient registering...");
                patient.setName(registerDTO.getReg_name());
                patient.setEmail(registerDTO.getReg_email());
                patient.setPassword(registerDTO.getReg_password());
                patient.setRole(ROLE.ROLE_PATIENT);
                patientService.registerPatient(patient);
                return "redirect:/customLogin?success";
            }
            return "redirect:/customLogin?success";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/register?error";
        }
    }

    @Operation(summary = "Show login form with success or error messages")
    @GetMapping("/customLogin")
    public String showLoginForm(@RequestParam(value = "success", required = false) String success,
                                @RequestParam(value = "error", required = false) String error,
                                Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        System.out.println("Login page loaded");
        if (success != null) {
            model.addAttribute("success", "Registration successful! Please log in.");
        }
        if (error != null) {
            model.addAttribute("error", "Login failed! Please try again.");
        }
        return "login";
    }

    @Operation(summary = "Process user login")
    @PostMapping(value = "/customLogin")
    public String loginUser(@Valid @ModelAttribute LoginDTO loginDTO, Model model, HttpSession session) {
        System.out.println("Login processing..");
        Optional<? extends User> user1 = userService.authenticateUser(loginDTO.getLog_email(), loginDTO.getLog_password());

        if (user1.isPresent()) {
            User user = user1.get();
            System.out.println("Login successful for user: " + user.getEmail());
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, loginDTO.getLog_password(), userDetails.getAuthorities());
            if (bCryptPasswordEncoder.matches(loginDTO.getLog_password(), userDetails.getPassword())) {
                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authToken);
                session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
            }
            else {
                throw new BadCredentialsException("Invalid password");
            }
            switch (user.getRole()) {
                case ROLE_DOCTOR:
                    System.out.println("111111111");
                    Doctor doctor = (Doctor) user;
                    Long doctorId = doctor.getId();
                    return String.format("redirect:/doctor/%d/dashboard", doctorId);
                case ROLE_PATIENT:
                    System.out.println("222222222");
                    Patient patient = (Patient) user;
                    Long patientId = patient.getId();
                    return String.format("redirect:/user/%d/dashboard", patientId);
                case ROLE_ADMIN:
                    System.out.println("3333333333");
                    return "redirect:/admin/home";
                default:
                    System.out.println("44444444444");
                    return "redirect:/index";
            }
        } else {
            System.out.println("Login failed for email: " + loginDTO.getLog_email());
            model.addAttribute("error", "Invalid email or password");
            return "redirect:/customLogin?error";
        }
    }

        @Operation(summary = "Show the home page")
        @GetMapping("/index")
        public String getHomePage () {
            return "index";
        }

        @GetMapping("/403")
        public String get403Error(){
            return "403";
        }
    }
