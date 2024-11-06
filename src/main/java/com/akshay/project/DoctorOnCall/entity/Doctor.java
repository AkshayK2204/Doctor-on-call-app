package com.akshay.project.DoctorOnCall.entity;

import com.akshay.project.DoctorOnCall.enums.SPECIALIZATION;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "doctors")
public class Doctor extends User {

     SPECIALIZATION specialization;


     //    public Doctor() {
//        this.setDocId(getFormattedDoctorId());
//        this.setRole(ROLE.ROLE_DOCTOR);
//    }

    public String getFormattedDoctorId() {
        return "D" + getId();
    }
}
