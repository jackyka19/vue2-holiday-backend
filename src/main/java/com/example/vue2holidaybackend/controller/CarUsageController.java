package com.example.vue2holidaybackend.controller;

import com.example.vue2holidaybackend.dto.CarUsageExportDTO;
import com.example.vue2holidaybackend.entity.CarUsage;
import com.example.vue2holidaybackend.service.CarUsageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import com.alibaba.excel.EasyExcel;

@RestController
@RequestMapping("/api/car")
@CrossOrigin(origins = "*")
public class CarUsageController {

    @Autowired
    private CarUsageService carUsageService;

    @GetMapping("/list")
    public List<CarUsage> list() {
        return carUsageService.findAll();
    }

    @PostMapping
    public Long create(@RequestBody CarUsage carUsage) {
        return carUsageService.create(carUsage);
    }

    @PutMapping("/{id}")
    public boolean update(@PathVariable Long id, @RequestBody CarUsage carUsage) {
        carUsage.setId(id);
        return carUsageService.update(carUsage);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return carUsageService.delete(id);
    }

    @PostMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        List<CarUsage> list = carUsageService.findAll();

        List<CarUsageExportDTO> exportList = list.stream().map(car -> {
            CarUsageExportDTO dto = new CarUsageExportDTO();
            dto.setUsageDate(car.getUsageDate());
            dto.setStartTime(car.getStartTime() != null ? car.getStartTime().toString() : "");
            dto.setEndTime(car.getEndTime() != null ? car.getEndTime().toString() : "");
            dto.setStartLocation(car.getStartLocation());
            dto.setEndLocation(car.getEndLocation());
            dto.setIsHolidayStr(car.getIsHoliday() ? "是" : "否");
            return dto;
        }).collect(Collectors.toList());

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = "用車記錄.xlsx";
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" +
                java.net.URLEncoder.encode(fileName, "UTF-8"));

        EasyExcel.write(response.getOutputStream(), CarUsageExportDTO.class)
                .sheet("用車記錄")
                .doWrite(exportList);
    }

}