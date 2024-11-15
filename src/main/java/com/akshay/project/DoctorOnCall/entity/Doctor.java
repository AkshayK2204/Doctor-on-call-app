package com.akshay.project.DoctorOnCall.entity;

import com.akshay.project.DoctorOnCall.enums.GENDER;
import com.akshay.project.DoctorOnCall.enums.ROLE;
import com.akshay.project.DoctorOnCall.enums.SPECIALIZATION;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "doctors")
public class Doctor extends User {

     SPECIALIZATION specialization;

    public Doctor(String name, String email, String password, ROLE role, GENDER gender, SPECIALIZATION specialization) {
        this.setName(name);
        this.setEmail(email);
        this.setPassword(password);
        this.setRole(role);
        this.setGender(gender);
        this.specialization = specialization;
    }

    //    public Doctor() {
//        this.setDocId(getFormattedDoctorId());
//        this.setRole(ROLE.ROLE_DOCTOR);
//    }

    public String getFormattedDoctorId() {
        return "D" + getId();
    }
}
