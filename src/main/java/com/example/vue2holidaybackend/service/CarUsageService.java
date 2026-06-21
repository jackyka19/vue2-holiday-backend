package com.example.vue2holidaybackend.service;

import com.example.vue2holidaybackend.entity.CarUsage;
import java.util.List;

public interface CarUsageService {
    List<CarUsage> findAll();
    CarUsage findById(Long id);
    Long create(CarUsage carUsage);
    boolean update(CarUsage carUsage);
    boolean delete(Long id);
}