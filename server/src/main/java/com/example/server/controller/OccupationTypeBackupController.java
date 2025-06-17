package com.example.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.server.model.OccupationTypeBackup;
import com.example.server.service.OccupationTypeBackupService;
import com.example.server.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/occupation-type-backups")
@Tag(name = "职业类型备份管理", description = "职业类型备份管理接口")
@CrossOrigin
public class OccupationTypeBackupController {

    @Autowired
    private OccupationTypeBackupService occupationTypeBackupService;

    @Operation(summary = "获取所有职业类型备份记录")
    @GetMapping
    public Result getAllOccupationTypeBackups() {
        List<OccupationTypeBackup> backups = occupationTypeBackupService.list();
        return Result.ok().data("backups", backups);
    }

    @Operation(summary = "根据备份记录 ID 获取职业类型备份记录")
    @GetMapping("/{backupRecordId}")
    public Result getOccupationTypeBackupsByBackupRecordId(@PathVariable Integer backupRecordId) {
        List<OccupationTypeBackup> backups = occupationTypeBackupService.list(
                new QueryWrapper<OccupationTypeBackup>().eq("BackupRecordId", backupRecordId));
        return Result.ok().data("backups", backups);
    }
}