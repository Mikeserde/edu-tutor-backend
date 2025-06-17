package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.mapper.OccupationScheduleBackupMapper;
import com.example.server.model.OccupationScheduleBackup;
import com.example.server.service.OccupationScheduleBackupService;
import org.springframework.stereotype.Service;

@Service
public class OccupationScheduleBackupServiceImpl extends ServiceImpl<OccupationScheduleBackupMapper, OccupationScheduleBackup>
        implements OccupationScheduleBackupService {
    // 由于备份操作通过存储过程完成，这里不需要额外的自定义方法
}