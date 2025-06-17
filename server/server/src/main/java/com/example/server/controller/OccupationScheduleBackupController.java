package com.example.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.server.model.OccupationScheduleBackup;
import com.example.server.service.OccupationScheduleBackupService;
import com.example.server.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/occupation-schedule-backups")
@Tag(name = "职业作息备份管理", description = "职业作息备份管理接口")
@CrossOrigin
public class OccupationScheduleBackupController {

    @Autowired
    private OccupationScheduleBackupService occupationScheduleBackupService;

    @Operation(summary = "获取所有职业作息备份记录")
    @GetMapping
    public Result getAllOccupationScheduleBackups() {
        List<OccupationScheduleBackup> backups = occupationScheduleBackupService.list();
        return Result.ok().data("backups", backups);
    }

    @Operation(summary = "根据备份记录 ID 获取职业作息备份记录")
    @GetMapping("/{backupRecordId}")
    public Result getOccupationScheduleBackupsByBackupRecordId(@PathVariable Integer backupRecordId) {
        List<OccupationScheduleBackup> backups = occupationScheduleBackupService.list(
                new QueryWrapper<OccupationScheduleBackup>().eq("BackupRecordId", backupRecordId));
        return Result.ok().data("backups", backups);
    }
}