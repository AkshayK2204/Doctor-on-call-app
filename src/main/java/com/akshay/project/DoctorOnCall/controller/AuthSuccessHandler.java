package com.akshay.project.DoctorOnCall.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {


        System.out.println("Login successfullllllll for user: " + authentication.getName());





//        String redirectUrl = "/index";

        System.out.println("AuthSuccessHandler is working............");


        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DOCTOR"))) {
            response.sendRedirect("/doctor/home");
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PATIENT"))) {
            response.sendRedirect("/user/doctors");
//        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
//            response.sendRedirect("/admin/dashboard");
        } else {
            response.sendRedirect("/login?error");
        }







        HttpSession session = request.getSession();
        System.out.println("Session ID: " + session.getId());
        System.out.println("Session Timeout: " + session.getMaxInactiveInterval());

        // Get user's role
//        User user = (User) authentication.getPrincipal();
//        ROLE role = user.getRole();
//        System.out.println("Inside auth handler..");
//        // Redirect based on the role
//        if (role == ROLE.ROLE_DOCTOR) {
//            redirectUrl = "/doctor/home";
//        } else if (role == ROLE.ROLE_PATIENT) {
//            System.out.println("Role is patient...");
//            redirectUrl = "/patient/home";
//        } else if (role == ROLE.ROLE_ADMIN) {
//            redirectUrl = "/admin/home";
//        }
//        System.out.println("Redirecting to: " + redirectUrl);
//        response.sendRedirect(redirectUrl);
    }
}
