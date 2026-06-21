package com.example.vue2holidaybackend.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class HolidayConfig {
    private Long id;
    private Integer year;
    private String configName;
    private Integer weekendDays = 0;
    private Integer publicHolidays = 0;
    private Integer totalDays = 0;
    private String status; // active / archived

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private List<HolidayDetail> holidays;
}