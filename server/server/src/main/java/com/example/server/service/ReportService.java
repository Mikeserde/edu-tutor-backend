package com.example.server.service;

import com.example.server.mapper.ReportMapper;
import com.example.server.model.OccupationDemandDTO;
import com.example.server.model.TeacherHoursDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportMapper reportMapper;

    //教师工作时长统计方法
    public List<TeacherHoursDTO> getTeacherHoursReport(LocalDate startDate, LocalDate endDate) {
        return reportMapper.calculateTeacherHours(startDate, endDate);
    }

    // 职业需求统计方法
    public List<OccupationDemandDTO> getOccupationDemandReport() {
        return reportMapper.countOccupationDemand();
    }
}