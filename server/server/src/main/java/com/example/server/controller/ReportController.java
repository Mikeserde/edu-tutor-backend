package com.example.server.controller;

import com.example.server.model.OccupationDemandDTO;
import com.example.server.model.TeacherHoursDTO;
import com.example.server.service.ReportService;
import com.example.server.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Tag(name = "统计数据", description = "数据统计接口")
@CrossOrigin
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/teacher-hours")
    @Operation(summary = "统计某段时间内各教师的工作时长")
    public Result getTeacherHours(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<TeacherHoursDTO> report = reportService.getTeacherHoursReport(startDate, endDate);
        return Result.ok().data("list", report); // 明确列表数据字段命名
    }

    @GetMapping("/occupation-demand")
    @Operation(summary = "职业需求统计")
    public Result getOccupationDemand() {
        List<OccupationDemandDTO> report = reportService.getOccupationDemandReport();
        return Result.ok().data("list", report); // 保持数据结构一致性
    }
}