package com.example.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.mapper.OccupationRegistrationBackupMapper;
import com.example.server.model.OccupationRegistrationBackup;
import com.example.server.service.OccupationRegistrationBackupService;
import org.springframework.stereotype.Service;

@Service
public class OccupationRegistrationBackupServiceImpl extends ServiceImpl<OccupationRegistrationBackupMapper, OccupationRegistrationBackup>
        implements OccupationRegistrationBackupService {
    // 由于备份操作通过存储过程完成，这里不需要额外的自定义方法
}