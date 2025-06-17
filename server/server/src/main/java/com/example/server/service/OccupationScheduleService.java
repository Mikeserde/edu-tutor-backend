package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.model.OccupationSchedule;

import java.time.LocalDate;
import java.time.LocalTime;

public interface OccupationScheduleService extends IService<OccupationSchedule> {
    boolean checkTimeConflict(Integer teacherId, LocalDate date, LocalTime startTime, LocalTime endTime);
}