package com.akshay.project.DoctorOnCall.entity;

import com.akshay.project.DoctorOnCall.enums.GENDER;
import com.akshay.project.DoctorOnCall.enums.ROLE;
import com.akshay.project.DoctorOnCall.enums.SPECIALIZATION;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "doctors")
public class Doctor extends User {

     SPECIALIZATION specialization;

     @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
     @JsonManagedReference
     private List<Availability> availabilityList;

    public Doctor(String name, String email, String password, ROLE role, GENDER gender, SPECIALIZATION specialization) {
        this.setName(name);
        this.setEmail(email);
        this.setPassword(password);
        this.setRole(role);
        this.setGender(gender);
        this.specialization = specialization;
    }

}
