package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.model.BackupRecord;

import java.util.List;

public interface BackupRecordService extends IService<BackupRecord> {
    // 创建备份
    Integer createBackup(String comment);

    // 恢复备份
    String restoreBackup(Integer backupId);

    // 清理备份
    String cleanBackupData(Integer keepCount, Integer keepDays);

    // 列出所有备份记录
    List<BackupRecord> listBackups();

    // 根据备份 ID 查询备份记录详情
    BackupRecord getBackupById(Integer backupId);
}