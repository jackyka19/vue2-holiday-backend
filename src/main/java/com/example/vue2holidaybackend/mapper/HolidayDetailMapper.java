package com.example.vue2holidaybackend.mapper;

import com.example.vue2holidaybackend.entity.HolidayDetail;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface HolidayDetailMapper {
    int batchInsert(List<HolidayDetail> list);
    List<HolidayDetail> findByConfigId(Long configId);
    int deleteByConfigId(Long configId);
    int insert(HolidayDetail detail);
    int update(HolidayDetail detail);
    int deleteById(Long id);
}
