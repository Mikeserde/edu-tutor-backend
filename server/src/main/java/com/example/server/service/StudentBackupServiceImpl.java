package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.mapper.StudentBackupMapper;
import com.example.server.model.StudentBackup;
import com.example.server.service.StudentBackupService;
import org.springframework.stereotype.Service;

@Service
public class StudentBackupServiceImpl extends ServiceImpl<StudentBackupMapper, StudentBackup>
        implements StudentBackupService {
    // 由于备份操作通过存储过程完成，这里不需要额外的自定义方法
}