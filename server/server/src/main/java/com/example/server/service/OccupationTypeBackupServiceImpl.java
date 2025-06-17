package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.mapper.OccupationTypeBackupMapper;
import com.example.server.model.OccupationTypeBackup;
import com.example.server.service.OccupationTypeBackupService;
import org.springframework.stereotype.Service;

@Service
public class OccupationTypeBackupServiceImpl extends ServiceImpl<OccupationTypeBackupMapper, OccupationTypeBackup>
        implements OccupationTypeBackupService {
    // 由于备份操作通过存储过程完成，这里不需要额外的自定义方法
}