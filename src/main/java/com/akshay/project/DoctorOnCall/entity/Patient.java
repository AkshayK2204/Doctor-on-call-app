package com.akshay.project.DoctorOnCall.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "patients")
public class Patient extends User {

//    @Id
//    private String patId;


    //private BLOOD_TYPE bloodType;
//    private String emergencyContact;
//    private Double height;
//    private Double weight;

//    public Patient() {
//        this.setPatId(getFormattedPatientId());
//        this.setRole(ROLE.ROLE_PATIENT);
//    }

    public String getFormattedPatientId() {
        return "P" + getId();
    }
}
