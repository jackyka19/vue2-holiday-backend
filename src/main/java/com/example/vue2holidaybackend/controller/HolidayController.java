package com.example.vue2holidaybackend.controller;

import com.example.vue2holidaybackend.entity.HolidayConfig;
import com.example.vue2holidaybackend.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/holiday")
@CrossOrigin(origins = "*")
public class HolidayController {

    @Autowired
    private HolidayService holidayService;

    @GetMapping("/list")
    public List<HolidayConfig> list() {
        return holidayService.findAll();
    }

    @PostMapping
    public Long create(@RequestBody HolidayConfig config) {
        return holidayService.create(config);
    }

    @PutMapping("/{id}")
    public boolean update(@PathVariable Long id, @RequestBody HolidayConfig config) {
        config.setId(id);
        return holidayService.update(config);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return holidayService.delete(id);
    }
}