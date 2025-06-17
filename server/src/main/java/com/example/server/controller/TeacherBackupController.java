package com.example.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.server.model.TeacherBackup;
import com.example.server.service.TeacherBackupService;
import com.example.server.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher-backups")
@Tag(name = "教师备份管理", description = "教师备份管理接口")
@CrossOrigin
public class TeacherBackupController {

    @Autowired
    private TeacherBackupService teacherBackupService;

    @Operation(summary = "获取所有教师备份记录")
    @GetMapping
    public Result getAllTeacherBackups() {
        List<TeacherBackup> backups = teacherBackupService.list();
        return Result.ok().data("backups", backups);
    }

    @Operation(summary = "根据备份记录 ID 获取教师备份记录")
    @GetMapping("/{backupRecordId}")
    public Result getTeacherBackupsByBackupRecordId(@PathVariable Integer backupRecordId) {
        List<TeacherBackup> backups = teacherBackupService.list(
                new QueryWrapper<TeacherBackup>().eq("BackupRecordId", backupRecordId));
        return Result.ok().data("backups", backups);
    }
}