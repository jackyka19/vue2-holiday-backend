package com.example.vue2holidaybackend.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CarUsageExportDTO {

    @ExcelProperty("用車日期")
    private LocalDate usageDate;

    @ExcelProperty("開始時間")
    private String startTime;     // ← 改成 String

    @ExcelProperty("結束時間")
    private String endTime;       // ← 改成 String

    @ExcelProperty("起點")
    private String startLocation;

    @ExcelProperty("目的地")
    private String endLocation;

    @ExcelProperty("是否節假日")
    private String isHolidayStr;
}