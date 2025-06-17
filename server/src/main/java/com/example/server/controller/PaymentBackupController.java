package com.example.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.server.model.PaymentBackup;
import com.example.server.service.PaymentBackupService;
import com.example.server.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment-backups")
@Tag(name = "收费备份管理", description = "收费备份管理接口")
@CrossOrigin
public class PaymentBackupController {

    @Autowired
    private PaymentBackupService paymentBackupService;

    @Operation(summary = "获取所有收费备份记录")
    @GetMapping
    public Result getAllPaymentBackups() {
        List<PaymentBackup> backups = paymentBackupService.list();
        return Result.ok().data("backups", backups);
    }

    @Operation(summary = "根据备份记录 ID 获取收费备份记录")
    @GetMapping("/{backupRecordId}")
    public Result getPaymentBackupsByBackupRecordId(@PathVariable Integer backupRecordId) {
        List<PaymentBackup> backups = paymentBackupService.list(
                new QueryWrapper<PaymentBackup>().eq("BackupRecordId", backupRecordId));
        return Result.ok().data("backups", backups);
    }
}