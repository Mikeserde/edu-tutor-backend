package com.example.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.server.model.StudentBackup;
import com.example.server.service.StudentBackupService;
import com.example.server.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student-backups")
@Tag(name = "学生备份管理", description = "学生备份管理接口")
@CrossOrigin
public class StudentBackupController {

    @Autowired
    private StudentBackupService studentBackupService;

    @Operation(summary = "获取所有学生备份记录")
    @GetMapping
    public Result getAllStudentBackups() {
        List<StudentBackup> backups = studentBackupService.list();
        return Result.ok().data("backups", backups);
    }

    @Operation(summary = "根据备份记录 ID 获取学生备份记录")
    @GetMapping("/{backupRecordId}")
    public Result getStudentBackupsByBackupRecordId(@PathVariable Integer backupRecordId) {
        List<StudentBackup> backups = studentBackupService.list(
                new QueryWrapper<StudentBackup>().eq("BackupRecordId", backupRecordId));
        return Result.ok().data("backups", backups);
    }
}