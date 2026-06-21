package com.example.vue2holidaybackend.mapper;

import com.example.vue2holidaybackend.entity.HolidayConfig;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface HolidayConfigMapper {
    List<HolidayConfig> findAll();
    HolidayConfig findById(Long id);
    HolidayConfig findByYear(Integer year);
    int insert(HolidayConfig config);
    int update(HolidayConfig config);
    int deleteById(Long id);
}