package com.akshay.project.DoctorOnCall.dtos;

import com.akshay.project.DoctorOnCall.enums.SPECIALIZATION;

public class DoctorDTO {

    private Long doc_id;
    private String doc_name;
    private SPECIALIZATION doc_specialization;

    public Long getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(Long doc_id) {
        this.doc_id = doc_id;
    }

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    public SPECIALIZATION getDoc_specialization() {
        return doc_specialization;
    }

    public void setDoc_specialization(SPECIALIZATION doc_specialization) {
        this.doc_specialization = doc_specialization;
    }
}
