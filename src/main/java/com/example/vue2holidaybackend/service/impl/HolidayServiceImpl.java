package com.example.vue2holidaybackend.service.impl;

import com.example.vue2holidaybackend.entity.HolidayConfig;
import com.example.vue2holidaybackend.entity.HolidayDetail;
import com.example.vue2holidaybackend.mapper.HolidayConfigMapper;
import com.example.vue2holidaybackend.mapper.HolidayDetailMapper;
import com.example.vue2holidaybackend.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class HolidayServiceImpl implements HolidayService {

    @Autowired
    private HolidayConfigMapper configMapper;
    @Autowired
    private HolidayDetailMapper detailMapper;

    @Override
    public List<HolidayConfig> findAll() {
        return configMapper.findAll();
    }

    @Override
    public HolidayConfig findById(Long id) {
        return configMapper.findById(id);
    }

    @Transactional
    @Override
    public Long create(HolidayConfig config) {
        // 先檢查年份是否已存在
        HolidayConfig existing = configMapper.findByYear(config.getYear());
        if (existing != null) {
            throw new IllegalArgumentException("該年份的節假日配置已存在！");
        }

        // 計算統計數據
        // 你可以在這裡呼叫之前 Vue 中的 calcHolidayStats 邏輯（轉成 Java）
        calculateAndSetStats(config);           // 計算統計數據
        config.setCreateTime(LocalDateTime.now());
        config.setUpdateTime(LocalDateTime.now());

        configMapper.insert(config);

        if (config.getHolidays() != null && !config.getHolidays().isEmpty()) {
            config.getHolidays().forEach(h -> h.setConfigId(config.getId()));
            detailMapper.batchInsert(config.getHolidays());
        }

        return config.getId();
    }

    /**
     * 修改年度配置
     */
    @Transactional
    @Override
    public boolean update(HolidayConfig config) {
        if (config.getId() == null) {
            throw new IllegalArgumentException("更新時必須提供 ID");
        }

        calculateAndSetStats(config);           // 重新計算統計數據
        config.setUpdateTime(LocalDateTime.now());

        // 更新主表
        int rows = configMapper.update(config);
        if (rows == 0) {
            return false;
        }

        // === 優化後的明細更新邏輯 ===
        Long configId = config.getId();
        List<HolidayDetail> newHolidays = config.getHolidays() != null ? config.getHolidays() : new ArrayList<>();

        // 1. 取得舊的明細
        List<HolidayDetail> oldHolidays = detailMapper.findByConfigId(configId);

        // 2. 找出要刪除的（舊的有，但新的沒有）
        for (HolidayDetail oldH : oldHolidays) {
            boolean stillExists = newHolidays.stream()
                    .anyMatch(newH -> newH.getId() != null && newH.getId().equals(oldH.getId()));
            if (!stillExists) {
                detailMapper.deleteById(oldH.getId());   // 需要在 HolidayDetailMapper 新增 deleteById 方法
            }
        }

        // 3. 更新或新增新的明細
        for (HolidayDetail newH : newHolidays) {
            newH.setConfigId(configId);
            if (newH.getId() != null) {
                // 有 id → 更新
                detailMapper.update(newH);
            } else {
                // 沒有 id → 新增
                detailMapper.insert(newH);
            }
        }

//        // 先刪除舊的假期明細
//        detailMapper.deleteByConfigId(config.getId());
//
//        // 插入新的假期明細
//        if (config.getHolidays() != null && !config.getHolidays().isEmpty()) {
//            config.getHolidays().forEach(h -> h.setConfigId(config.getId()));
//            detailMapper.batchInsert(config.getHolidays());
//        }

        return true;
    }

    /**
     * 刪除年度配置（含關聯明細）
     */
    @Transactional
    @Override
    public boolean delete(Long id) {
        // MySQL 有 ON DELETE CASCADE，所以刪主表即可
        int rows = configMapper.deleteById(id);
        return rows > 0;
    }

    /**
     * 核心計算邏輯（從 Vue 移植過來）
     */
    private void calculateAndSetStats(HolidayConfig config) {
        if (config.getYear() == null) {
            return;
        }

        StatsResult stats = calcHolidayStats(config.getYear(), config.getHolidays());

        config.setWeekendDays(stats.weekendDays);
        config.setPublicHolidays(stats.publicHolidays);
        config.setTotalDays(stats.totalDays);

        // 設定狀態
        int currentYear = LocalDate.now().getYear();
        config.setStatus(config.getYear() == currentYear ? "active" : "archived");
    }

    /**
     * 統計計算結果類
     */
    private static class StatsResult {
        int weekendDays;
        int publicHolidays;
        int totalDays;
    }

    /**
     * 計算假期統計（移植自 Vue）
     */
    private StatsResult calcHolidayStats(Integer year, List<HolidayDetail> holidays) {
        StatsResult result = new StatsResult();

        // 1. 收集所有公眾假期日期（去重）
        Set<String> holidayDateSet = new HashSet<>();
        if (holidays != null) {
            for (HolidayDetail h : holidays) {
                if (h.getStartDate() == null || h.getEndDate() == null) continue;

                LocalDate start = h.getStartDate();
                LocalDate end = h.getEndDate();

                for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
                    if (d.getYear() == year) {
                        holidayDateSet.add(d.toString());
                    }
                }
            }
        }

        // 2. 計算該年所有周末
        Set<String> weekendSet = new HashSet<>();
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        for (LocalDate d = startDate; !d.isAfter(endDate); d = d.plusDays(1)) {
            int dayOfWeek = d.getDayOfWeek().getValue(); // 1=星期一 ... 6=星期六 7=星期日
            if (dayOfWeek == 6 || dayOfWeek == 7) {     // 周六、周日
                weekendSet.add(d.toString());
            }
        }

        // 3. 合併去重（周末 ∪ 公眾假期）
        Set<String> allHolidaysSet = new HashSet<>(weekendSet);
        allHolidaysSet.addAll(holidayDateSet);

        result.weekendDays = weekendSet.size();
        result.publicHolidays = holidayDateSet.size();
        result.totalDays = allHolidaysSet.size();

        return result;
    }
}