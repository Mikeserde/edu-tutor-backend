package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.mapper.PaymentBackupMapper;
import com.example.server.model.PaymentBackup;
import com.example.server.service.PaymentBackupService;
import org.springframework.stereotype.Service;

@Service
public class PaymentBackupServiceImpl extends ServiceImpl<PaymentBackupMapper, PaymentBackup>
        implements PaymentBackupService {
    // 由于备份操作通过存储过程完成，这里不需要额外的自定义方法
}