package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.mapper.BackupRecordMapper;
import com.example.server.model.BackupRecord;
import com.example.server.service.BackupRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BackupRecordServiceImpl extends ServiceImpl<BackupRecordMapper, BackupRecord>
        implements BackupRecordService {

    @Autowired
    private BackupRecordMapper backupRecordMapper;

    @Override
    public Integer createBackup(String comment) {
        return backupRecordMapper.createBackup(comment);
    }

    @Override
    public String restoreBackup(Integer backupId) {
        return backupRecordMapper.restoreBackup(backupId);
    }

    @Override
    public String cleanBackupData(Integer keepCount, Integer keepDays) {
        return backupRecordMapper.cleanBackupData(keepCount, keepDays);
    }

    @Override
    public List<BackupRecord> listBackups() {
        return backupRecordMapper.listBackups();
    }

    @Override
    public BackupRecord getBackupById(Integer backupId) {
        return backupRecordMapper.selectById(backupId);
    }
}