package com.example.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.server.model.OccupationSchedule;
import com.example.server.service.OccupationScheduleService;
import com.example.server.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.time.LocalDate;

@Validated
@RestController
@RequestMapping("/occupation-schedules")
@Tag(name = "职业排班管理", description = "职业排班管理接口")
@CrossOrigin
public class OccupationScheduleController {

    @Resource
    private OccupationScheduleService scheduleService;

    @Operation(summary = "创建排班")
    @PostMapping
    public Result createSchedule(@Valid @RequestBody OccupationSchedule schedule) {
        if (scheduleService.checkTimeConflict(
                schedule.getTeacherId(),
                schedule.getDate(),
                schedule.getStartTime(),
                schedule.getEndTime())) {
            return Result.error().message("该时段已有排班冲突");
        }
        boolean saved = scheduleService.save(schedule);
        return saved ?
                Result.ok().message("创建成功").data("data", schedule) : // 数据使用明确字段名
                Result.error().message("创建失败");
    }

    @Operation(summary = "更新排班信息")
    @PutMapping("/{id}")
    public Result updateSchedule(
            @PathVariable @NotNull(message = "ID不能为空") Integer id,
            @Valid @RequestBody OccupationSchedule schedule) {
        schedule.setScheduleId(id);
        boolean updated = scheduleService.updateById(schedule);
        return updated ?
                Result.ok().message("更新成功").data("data", schedule) :
                Result.error().message("更新失败");
    }

    @Operation(summary = "根据ID查询排班")
    @GetMapping("/{id}")
    public Result getScheduleById(
            @PathVariable @NotNull(message = "ID不能为空") Integer id) {
        OccupationSchedule schedule = scheduleService.getById(id);
        return schedule != null ?
                Result.ok().data("data", schedule) : // 数据字段统一命名
                Result.error().message("排班不存在");
    }

    @Operation(summary = "分页查询排班列表")
    @GetMapping
    public Result getSchedules(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer teacherId,
            @RequestParam(required = false) String date) {

        Page<OccupationSchedule> page = new Page<>(pageNum, pageSize);
        QueryWrapper<OccupationSchedule> wrapper = new QueryWrapper<>();
        if (teacherId != null) {
            wrapper.eq("TeacherId", teacherId);
        }
        if (date != null) {
            wrapper.eq("Date", LocalDate.parse(date));
        }
        return Result.ok().data("page", scheduleService.page(page, wrapper));
    }

    @Operation(summary = "删除排班")
    @DeleteMapping("/{id}")
    public Result deleteSchedule(
            @PathVariable @NotNull(message = "ID不能为空") Integer id) {
        boolean removed = scheduleService.removeById(id);
        return removed ?
                Result.ok().message("删除成功") : // 无数据时可省略data
                Result.error().message("删除失败");
    }
}