package com.example.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.server.model.Teacher;
import com.example.server.service.TeacherService;
import com.example.server.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("/teachers")
@Tag(name = "教师管理",description = "教师管理接口")
public class TeacherController {

    @Resource
    private TeacherService teacherService;

    @Operation(summary = "创建教师")
    @PostMapping
    public Result<Teacher> createTeacher(@Valid @RequestBody Teacher teacher) {
        boolean saved = teacherService.save(teacher);
        return saved ? Result.success("创建成功", teacher) : Result.fail("创建失败");
    }

    @Operation(summary = "根据id更新教师信息")
    @PutMapping("/{id}")
    public Result<Teacher> updateTeacher(
            @PathVariable @NotNull(message = "ID不能为空") Integer id,
            @Parameter(name = "教师信息")
            @Valid @RequestBody Teacher teacher) {
        teacher.setTeacherId(id);
        boolean updated = teacherService.updateById(teacher);
        return updated ? Result.<Teacher>success("更新成功",teacher) : Result.<Teacher>fail("更新失败");
    }

    @Operation(summary = "根据id查询教师信息")
    @GetMapping("/{id}")
    public Result<Teacher> getTeacherById(
            @PathVariable @NotNull(message = "ID不能为空") Integer id) {
        Teacher teacher = teacherService.getById(id);
        return teacher != null ? Result.success(teacher) : Result.fail("教师不存在");
    }


    @Operation(summary = "分页查询教师列表")
    @GetMapping
    public Result<Page<Teacher>> getTeachers(
            @Parameter(name = "页号")
            @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(name = "页的大小")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(name = "教师的名称")
            @RequestParam(required = false) String name) {

        Page<Teacher> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like("name", name);
        }
        return Result.success(teacherService.page(page, wrapper));
    }

    @Operation(summary = "根据ID删除教师")
    @DeleteMapping("/{id}")
    public Result<Teacher> deleteTeacher(
            @PathVariable @NotNull(message = "ID不能为空") Integer id) {
        boolean removed = teacherService.removeById(id);
        return removed ? Result.<Teacher>success("删除成功",null) : Result.<Teacher>fail("删除失败");
    }
}