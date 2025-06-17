package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.model.OccupationTypeBackup;

public interface OccupationTypeBackupService extends IService<OccupationTypeBackup> {
    // 由于备份操作通过存储过程完成，这里不需要额外的自定义方法
}