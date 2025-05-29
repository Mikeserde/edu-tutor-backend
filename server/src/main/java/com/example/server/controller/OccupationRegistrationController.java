package com.example.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.server.model.OccupationRegistration;
import com.example.server.service.OccupationRegistrationService;
import com.example.server.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping("/occupation-registrations")
@Tag(name = "职业注册管理", description = "学生职业注册管理接口")
public class OccupationRegistrationController {

    @Autowired
    private OccupationRegistrationService registrationService;

    @Operation(summary = "创建职业注册")
    @PostMapping
    public Result<OccupationRegistration> createRegistration(
            @Valid @RequestBody OccupationRegistration registration) {
        if (!registrationService.isRegistrationUnique(
                registration.getOccupationTypeId(),
                registration.getStudentId(),
                null)) {
            return Result.fail("该学生已注册此职业类型");
        }
        boolean saved = registrationService.save(registration);
        return saved ? Result.success("创建成功", registration) : Result.fail("创建失败");
    }

    @Operation(summary = "更新职业注册")
    @PutMapping("/{id}")
    public Result<OccupationRegistration> updateRegistration(
            @PathVariable @NotNull(message = "ID不能为空") Integer id,
            @Valid @RequestBody OccupationRegistration registration) {
        if (!registrationService.isRegistrationUnique(
                registration.getOccupationTypeId(),
                registration.getStudentId(),
                id)) {
            return Result.fail("该学生已注册此职业类型");
        }
        registration.setOccupationId(id);
        boolean updated = registrationService.updateById(registration);
        return updated ? Result.success("更新成功", registration) : Result.fail("更新失败");
    }

    @Operation(summary = "根据ID查询职业注册")
    @GetMapping("/{id}")
    public Result<OccupationRegistration> getRegistrationById(
            @PathVariable @NotNull(message = "ID不能为空") Integer id) {
        OccupationRegistration registration = registrationService.getById(id);
        return registration != null ? Result.success(registration) : Result.fail("注册记录不存在");
    }

    @Operation(summary = "分页查询职业注册列表")
    @GetMapping
    public Result<Page<OccupationRegistration>> getRegistrations(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer occupationTypeId,
            @RequestParam(required = false) Integer studentId) {

        Page<OccupationRegistration> page = new Page<>(pageNum, pageSize);
        QueryWrapper<OccupationRegistration> wrapper = new QueryWrapper<>();

        if (occupationTypeId != null) {
            wrapper.eq("OccupationTypeId", occupationTypeId);
        }
        if (studentId != null) {
            wrapper.eq("StudentId", studentId);
        }

        return Result.success(registrationService.page(page, wrapper));
    }

    @Operation(summary = "根据ID删除职业注册")
    @DeleteMapping("/{id}")
    public Result<String> deleteRegistration(
            @PathVariable @NotNull(message = "ID不能为空") Integer id) {
        boolean removed = registrationService.removeById(id);
        return removed ? Result.success("删除成功") : Result.fail("删除失败");
    }
}