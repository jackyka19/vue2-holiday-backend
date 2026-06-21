package com.example.vue2holidaybackend.dto;

import lombok.Data;
import java.util.List;

@Data
public class HolidayConfigDTO {
    private Long id;
    private Integer year;
    private String configName;
    private Integer weekendDays;
    private Integer publicHolidays;
    private Integer totalDays;
    private String status;
    private List<HolidayDetailDTO> holidays;
}

@Data
class HolidayDetailDTO {
    private String name;
    private String startDate;   // "2026/02/10"
    private String endDate;
}
