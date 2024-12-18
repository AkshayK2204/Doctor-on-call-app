package com.akshay.project.DoctorOnCall.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@Entity
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    @JsonBackReference
    private Doctor doctor;

    private LocalDate date;

    @ElementCollection
    @CollectionTable(name = "availability_times", joinColumns = @JoinColumn(name = "availability_id"))
    @Column(name = "time")
    private List<LocalTime> openTimes;

    public List<LocalTime> getOpenTimes() {
        if (openTimes != null) {
            openTimes.sort(Comparator.naturalOrder());
        }
        return openTimes;
    }

}