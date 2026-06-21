package com.example.vue2holidaybackend.service;

import com.example.vue2holidaybackend.entity.HolidayConfig;
import java.util.List;

public interface HolidayService {
    List<HolidayConfig> findAll();
    HolidayConfig findById(Long id);
    Long create(HolidayConfig config);
    boolean update(HolidayConfig config);
    boolean delete(Long id);
}