package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.model.StudentBackup;

public interface StudentBackupService extends IService<StudentBackup> {
    // 由于备份操作通过存储过程完成，这里不需要额外的自定义方法
}