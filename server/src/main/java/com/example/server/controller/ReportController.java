package com.example.server.controller;

import com.example.server.model.OccupationDemandDTO;
import com.example.server.model.TeacherHoursDTO;
import com.example.server.service.ReportService;
import com.example.server.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Tag(name = "统计数据",description = "数据统计接口")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/teacher-hours")
    @Operation(summary = "统计某段时间内各教师的工作时长")
    public Result<List<TeacherHoursDTO>> getTeacherHours(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<TeacherHoursDTO> report = reportService.getTeacherHoursReport(startDate, endDate);
        return Result.success(report);
    }

    @GetMapping("/occupation-demand")
    @Operation(summary = "职业需求统计")
    public Result<List<OccupationDemandDTO>> getOccupationDemand() {
        List<OccupationDemandDTO> report = reportService.getOccupationDemandReport();
        return Result.success(report);
    }
}