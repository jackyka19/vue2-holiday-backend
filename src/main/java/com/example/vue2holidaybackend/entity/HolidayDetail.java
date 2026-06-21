package com.example.vue2holidaybackend.entity;

import lombok.Data;
import java.time.LocalDate;

@Data
public class HolidayDetail {
    private Long id;
    private Long configId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
}