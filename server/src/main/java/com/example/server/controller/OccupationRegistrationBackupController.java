package com.example.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.server.model.OccupationRegistrationBackup;
import com.example.server.service.OccupationRegistrationBackupService;
import com.example.server.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/occupation-registration-backups")
@Tag(name = "职业登记备份管理", description = "职业登记备份管理接口")
@CrossOrigin
public class OccupationRegistrationBackupController {

    @Autowired
    private OccupationRegistrationBackupService occupationRegistrationBackupService;

    @Operation(summary = "获取所有职业登记备份记录")
    @GetMapping
    public Result getAllOccupationRegistrationBackups() {
        List<OccupationRegistrationBackup> backups = occupationRegistrationBackupService.list();
        return Result.ok().data("backups", backups);
    }

    @Operation(summary = "根据备份记录 ID 获取职业登记备份记录")
    @GetMapping("/{backupRecordId}")
    public Result getOccupationRegistrationBackupsByBackupRecordId(@PathVariable Integer backupRecordId) {
        List<OccupationRegistrationBackup> backups = occupationRegistrationBackupService.list(
                new QueryWrapper<OccupationRegistrationBackup>().eq("BackupRecordId", backupRecordId));
        return Result.ok().data("backups", backups);
    }
}