package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.mapper.TeacherBackupMapper;
import com.example.server.model.TeacherBackup;
import com.example.server.service.TeacherBackupService;
import org.springframework.stereotype.Service;

@Service
public class TeacherBackupServiceImpl extends ServiceImpl<TeacherBackupMapper, TeacherBackup>
        implements TeacherBackupService {
    // 由于备份操作通过存储过程完成，这里不需要额外的自定义方法
}