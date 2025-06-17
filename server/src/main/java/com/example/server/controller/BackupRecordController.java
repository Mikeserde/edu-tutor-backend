package com.example.server.controller;

import com.example.server.model.BackupRecord;
import com.example.server.service.BackupRecordService;
import com.example.server.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping("/backup-records")
@Tag(name = "备份记录管理", description = "备份记录管理接口")
@CrossOrigin
public class BackupRecordController {

    @Autowired
    private BackupRecordService backupRecordService;

    @Operation(summary = "创建备份记录")
    @PostMapping
    public Result createBackup(@NotBlank(message = "备份说明不能为空") @RequestParam String comment) {
        Integer backupId = backupRecordService.createBackup(comment);
        if (backupId != null) {
            return Result.ok().message("备份成功").data("backupId", backupId);
        } else {
            return Result.error().message("备份失败");
        }
    }

    @Operation(summary = "恢复备份")
    @PutMapping("/{backupId}")
    public Result restoreBackup(@NotNull(message = "备份ID不能为空") @PathVariable Integer backupId) {
        String result = backupRecordService.restoreBackup(backupId);
        return Result.ok().message(result);
    }

    @Operation(summary = "清理备份")
    @DeleteMapping
    public Result cleanBackupData(
            @RequestParam(required = false) Integer keepCount,
            @RequestParam(required = false) Integer keepDays) {
        String result = backupRecordService.cleanBackupData(keepCount, keepDays);
        return Result.ok().message(result);
    }

    @Operation(summary = "列出所有备份记录")
    @GetMapping
    public Result listBackups() {
        List<BackupRecord> backups = backupRecordService.listBackups();
        return Result.ok().data("backups", backups);
    }

    @Operation(summary = "根据备份ID查询备份记录")
    @GetMapping("/{backupId}")
    public Result getBackupById(@NotNull(message = "备份ID不能为空") @PathVariable Integer backupId) {
        BackupRecord backupRecord = backupRecordService.getBackupById(backupId);
        if (backupRecord != null) {
            return Result.ok().data("backupRecord", backupRecord);
        } else {
            return Result.error().message("备份记录不存在");
        }
    }
}