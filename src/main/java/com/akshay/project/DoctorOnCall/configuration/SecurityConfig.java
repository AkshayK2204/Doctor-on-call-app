package com.akshay.project.DoctorOnCall.configuration;

import com.akshay.project.DoctorOnCall.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(registry -> {
                    registry.requestMatchers("/customLogin", "/register", "/index", "/process-login").permitAll();
                    registry.requestMatchers("/doctor/**").hasRole("DOCTOR");
                    registry.requestMatchers("/user/**").hasRole("PATIENT");
                    registry.anyRequest().authenticated();
                })
                .formLogin(form -> form
                        .loginPage("/customLogin")
                        .loginProcessingUrl("/process-login")
                        .permitAll()
                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/customLogin?logout=true")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                        .permitAll())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .maximumSessions(1)
                        .expiredUrl("/customLogin"))
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                        httpSecurityExceptionHandlingConfigurer.accessDeniedPage("/403"))
                .build();
    }

    @Bean
    public SecurityContextPersistenceFilter securityContextPersistenceFilter() {
        SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
        return new SecurityContextPersistenceFilter(securityContextRepository);
    }


    @Bean
    public UserDetailsService userDetailsService(){
        return customUserDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    }
}



//    @Bean
//    public UserDetailsService userDetailsService() {
//        System.out.println("User Details Service invoked...");
//        UserDetails user = User.builder()
//                .username("patient1@gmail.com")
//                .password("$2a$12$4JqpuJ1A2a.lfMfhx77COeSt5r35zfk.JP.APkA/PfYD7MQRmL2cy")
//                .roles("PATIENT")
//                .build();
//
//        UserDetails doctor = User.builder()
//                .username("doctor@gmail.com")
//                .password("$2a$12$OWD13V3RYPo5Z9YW.HCqq.wXYoU2tleVgiyK6/h9pufth8xONNQXS")
//                .roles("DOCTOR")
//                .build();
//
//            UserDetails admin = User.builder()
//                    .username("admin@gmail.com")
//                    .password(bCryptPasswordEncoder.encode("admin"))
//                    .roles("ADMIN","DOCTOR","PATIENT")
//                    .build();
//
//
//        return new InMemoryUserDetailsManager(user, doctor);
//    }
