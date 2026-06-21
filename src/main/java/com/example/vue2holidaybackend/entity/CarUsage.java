package com.example.vue2holidaybackend.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class CarUsage {
    private Long id;
    private LocalDate usageDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String startLocation;
    private String endLocation;
    private Boolean isHoliday;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}