package com.akshay.project.DoctorOnCall.configuration;

import com.akshay.project.DoctorOnCall.controller.AuthSuccessHandler;
import com.akshay.project.DoctorOnCall.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthSuccessHandler authSuccessHandler;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;



    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("AuthSuccessHandler injected: " + (authSuccessHandler != null));
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/customLogin", "/register","/index","/dashboard","/process-login").permitAll()
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/doctor/**").hasRole("DOCTOR")
//                        .requestMatchers("/patient/**").hasRole("PATIENT")
                        .anyRequest()
                        .permitAll()
//                        .authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/customLogin")
                        .loginProcessingUrl("/process-login")
                        .successHandler(authSuccessHandler)
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/customLogin?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/customLogin?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()

                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .maximumSessions(1)
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }


//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(customUserDetailsService);
//        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
//        return authProvider;
//    }
//
//    @Autowired
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authenticationProvider());
//    }



//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests(authorizeRequests ->
//                        authorizeRequests
////                                .requestMatchers("/register", "/customLogin", "/index")
////                                .permitAll()
////                                .requestMatchers("/doctor/**").hasRole("DOCTOR")
////                                .requestMatchers("/patient/**").hasRole("PATIENT")
//                                .anyRequest().permitAll()
//                                //.authenticated()
//                )
//                .formLogin(formLogin ->
//                        formLogin
//                                .loginPage("/customLogin") // Custom login page
//                                //.defaultSuccessUrl("/dashboard", true)
//                                .permitAll()
//                )
////                .logout(logout ->
////                        logout
////                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
////                                .logoutSuccessUrl("/login?logout")
////                                .permitAll()
////                )
//                .csrf(csrf -> csrf.disable());
//
//        return http.build();
//    }




}
