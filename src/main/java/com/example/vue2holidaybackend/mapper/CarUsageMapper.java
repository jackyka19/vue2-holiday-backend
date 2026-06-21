package com.example.vue2holidaybackend.mapper;

import com.example.vue2holidaybackend.entity.CarUsage;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CarUsageMapper {
    List<CarUsage> findAll();
    CarUsage findById(Long id);
    int insert(CarUsage carUsage);
    int update(CarUsage carUsage);
    int deleteById(Long id);
}