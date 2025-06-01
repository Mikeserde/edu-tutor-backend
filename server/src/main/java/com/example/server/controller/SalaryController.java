package com.example.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.server.model.Salary;
import com.example.server.service.SalaryService;
import com.example.server.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Validated
@RestController
@RequestMapping("/salaries")
@Tag(name = "工资管理", description = "教师工资管理接口")
public class SalaryController {

    @Autowired
    private SalaryService salaryService;
    
    @Operation(summary = "获取特定教师某月工资")
    @GetMapping("/{teacherId}/{month}")
    public Result getSalary(
            @PathVariable @NotNull(message = "教师ID不能为空") Integer teacherId,
            @PathVariable @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "月份格式应为YYYY-MM") String month) {
        Salary salary = salaryService.getByCompositeKey(teacherId, month);
        if (salary != null) {
            return Result.ok().data("salary", salary);
        }
        return Result.error().message("工资记录不存在");
    }

    @Operation(summary = "分页查询工资记录")
    @GetMapping
    public Result getSalaries(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer teacherId,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount) {

        Page<Salary> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Salary> wrapper = new QueryWrapper<>();

        if (teacherId != null) {
            wrapper.eq("TeacherId", teacherId);
        }
        if (month != null && !month.isEmpty()) {
            wrapper.eq("Month", month);
        }
        if (minAmount != null) {
            wrapper.ge("TotalAmount", minAmount);
        }
        if (maxAmount != null) {
            wrapper.le("TotalAmount", maxAmount);
        }

        return Result.ok().data("page", salaryService.page(page, wrapper));
    }


}