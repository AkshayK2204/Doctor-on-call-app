package com.akshay.project.DoctorOnCall.dtos;

import com.akshay.project.DoctorOnCall.enums.SPECIALIZATION;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorDTO {

    private Long doc_id;
    private String doc_name;
    private SPECIALIZATION doc_specialization;
}
