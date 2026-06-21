package com.example.vue2holidaybackend.service.impl;

import com.example.vue2holidaybackend.entity.CarUsage;
import com.example.vue2holidaybackend.entity.HolidayConfig;
import com.example.vue2holidaybackend.mapper.CarUsageMapper;
import com.example.vue2holidaybackend.mapper.HolidayConfigMapper;
import com.example.vue2holidaybackend.service.CarUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CarUsageServiceImpl implements CarUsageService {

    @Autowired
    private CarUsageMapper carUsageMapper;

    @Autowired
    private HolidayConfigMapper holidayConfigMapper;

    @Override
    public List<CarUsage> findAll() {
        return carUsageMapper.findAll();
    }

    @Override
    public CarUsage findById(Long id) {
        return carUsageMapper.findById(id);
    }

    @Transactional
    @Override
    public Long create(CarUsage carUsage) {
        if (carUsage == null) {
            throw new IllegalArgumentException("用車記錄不能為空");
        }

        carUsage.setIsHoliday(isHoliday(carUsage.getUsageDate()));

        carUsageMapper.insert(carUsage);   // 先執行 insert

        return carUsage.getId();           // ← 改成這樣，返回自動生成的 ID
    }

    @Transactional
    @Override
    public boolean update(CarUsage carUsage) {
        carUsage.setIsHoliday(isHoliday(carUsage.getUsageDate()));
        int rows = carUsageMapper.update(carUsage);
        return rows > 0;
    }

    @Transactional
    @Override
    public boolean delete(Long id) {
        int rows = carUsageMapper.deleteById(id);
        return rows > 0;
    }

    // 判斷是否為節假日
    private boolean isHoliday(LocalDate date) {
        if (date == null) return false;

        // 1. 檢查是否為週末
        int dayOfWeek = date.getDayOfWeek().getValue(); // 1=星期一 ... 6=星期六 7=星期日
        if (dayOfWeek == 6 || dayOfWeek == 7) {
            return true;
        }

        // 2. 檢查是否為公眾假期
        HolidayConfig config = holidayConfigMapper.findByYear(date.getYear());
        if (config != null && config.getHolidays() != null) {
            for (var h : config.getHolidays()) {
                if (h.getStartDate() != null && h.getEndDate() != null) {
                    if (!date.isBefore(h.getStartDate()) && !date.isAfter(h.getEndDate())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}