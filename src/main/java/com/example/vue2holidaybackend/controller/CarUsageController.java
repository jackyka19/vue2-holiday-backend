package com.example.vue2holidaybackend.controller;

import com.example.vue2holidaybackend.entity.CarUsage;
import com.example.vue2holidaybackend.service.CarUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
}