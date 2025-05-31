package com.example.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.server.model.Student;
import com.example.server.service.StudentService;
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
@RequestMapping("/students")
@Tag(name = "学生管理", description = "学生信息管理接口")
@CrossOrigin
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Operation(summary = "创建学生")
    @PostMapping
    public Result createStudent(@Valid @RequestBody Student student) {
        if (!studentService.isPhoneUnique(student.getContactPhone(), null)) {
            return Result.error().message("联系电话已存在"); // 错误状态自动携带50008
        }
        boolean saved = studentService.save(student);
        return saved ?
                Result.ok().message("创建成功").data("data", student) :
                Result.error().message("创建失败");
    }

    @Operation(summary = "更新学生信息")
    @PutMapping("/{id}")
    public Result updateStudent(
            @PathVariable @NotNull(message = "ID不能为空") Integer id,
            @Valid @RequestBody Student student) {
        if (!studentService.isPhoneUnique(student.getContactPhone(), id)) {
            return Result.error().message("联系电话已存在");
        }
        student.setStudentId(id);
        boolean updated = studentService.updateById(student);
        return updated ?
                Result.ok().message("更新成功").data("data", student) :
                Result.error().message("更新失败");
    }

    @Operation(summary = "根据ID查询学生")
    @GetMapping("/{id}")
    public Result getStudentById(
            @PathVariable @NotNull(message = "ID不能为空") Integer id) {
        Student student = studentService.getById(id);
        return student != null ?
                Result.ok().data("data", student) :
                Result.error().message("学生不存在");
    }

    @Operation(summary = "分页查询学生列表")
    @GetMapping
    public Result getStudents(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String gender) {

        Page<Student> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Student> wrapper = new QueryWrapper<>();

        if (name != null && !name.isEmpty()) {
            wrapper.like("name", name);  // 注意字段名大小写统一
        }
        if (gender != null && !gender.isEmpty()) {
            wrapper.eq("gender", gender);
        }

        return Result.ok().data("page", studentService.page(page, wrapper));
    }

    @Operation(summary = "根据ID删除学生")
    @DeleteMapping("/{id}")
    public Result deleteStudent(
            @PathVariable @NotNull(message = "ID不能为空") Integer id) {
        boolean removed = studentService.removeById(id);
        return removed ?
                Result.ok().message("删除成功") :
                Result.error().message("删除失败");
    }
}