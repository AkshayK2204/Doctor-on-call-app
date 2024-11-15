package com.akshay.project.DoctorOnCall.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        System.out.println("Login successful for user: " + authentication.getName());

        // Role-based redirection
        boolean isDoctor = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_DOCTOR"));
        boolean isPatient = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_PATIENT"));
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (isDoctor) {
            setDefaultTargetUrl("/doctor/6/dashboard");
        } else if (isPatient) {
            setDefaultTargetUrl("/user/2/doctors");
        } else {
            setDefaultTargetUrl("/index"); // Default redirect if role not matched
        }

        super.setDefaultTargetUrl(null); // Remove saved URL behavior if always redirecting by role
        super.onAuthenticationSuccess(request, response, authentication);

        // Session debugging
        HttpSession session = request.getSession();
        System.out.println("Session ID: " + session.getId());
        System.out.println("Session Timeout: " + session.getMaxInactiveInterval());
    }
}






























//package com.akshay.project.DoctorOnCall.controller;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request,
//                                        HttpServletResponse response,
//                                        Authentication authentication) throws IOException, ServletException {
//
//
//        System.out.println("Login successfullllllll for user: " + authentication.getName());
//
////        String redirectUrl = "/index";
//
//        System.out.println("AuthSuccessHandler is working............");
//
//        boolean isDoctor = authentication.getAuthorities().stream()
//                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_DOCTOR"));
//
//        if (isDoctor) {
//            setDefaultTargetUrl("/doctor/6/dashboard");
//        } else {
//            setDefaultTargetUrl("/user/6/doctors");
//        }
//        super.onAuthenticationSuccess(request, response, authentication);
//
//        HttpSession session = request.getSession();
//        System.out.println("Session ID: " + session.getId());
//        System.out.println("Session Timeout: " + session.getMaxInactiveInterval());
//    }
//}
//
//
//
//
//
//
//
