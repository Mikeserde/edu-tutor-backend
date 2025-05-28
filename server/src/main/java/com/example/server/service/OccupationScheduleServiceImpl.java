package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.mapper.OccupationScheduleMapper;
import com.example.server.model.OccupationSchedule;
import com.example.server.service.OccupationScheduleService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class OccupationScheduleServiceImpl extends ServiceImpl<OccupationScheduleMapper, OccupationSchedule>
        implements OccupationScheduleService {
    @Override
    public boolean checkTimeConflict(Integer teacherId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        return baseMapper.checkTimeConflict(teacherId, date, startTime, endTime) > 0;
    }
}