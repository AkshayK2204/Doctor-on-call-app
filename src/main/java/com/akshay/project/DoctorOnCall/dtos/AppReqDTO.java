package com.akshay.project.DoctorOnCall.dtos;

import com.akshay.project.DoctorOnCall.enums.BLOOD_TYPE;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppReqDTO {


    private String name;
    private long age;
    private String phoneNumber;
    private String address;

    private BLOOD_TYPE bloodType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate date;

    public AppReqDTO(String name, long age, String phoneNumber, String address, BLOOD_TYPE bloodType, LocalTime startTime, LocalDate date) {
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.bloodType = bloodType;
        this.startTime = startTime;
        this.endTime=startTime.plusMinutes(30);
        this.date = date;
    }
}
