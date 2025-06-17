package com.example.server.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@TableName("Payment_Backup")
public class PaymentBackup {

    @TableId(value = "BackupRecordId", type = IdType.INPUT)
    private Integer backupRecordId;

    @TableField("PaymentId")
    private Integer paymentId;

    @TableField("OccupationId")
    private Integer occupationId;

    @TableField("PaymentDate")
    private LocalDate paymentDate;

    @TableField("Amount")
    private BigDecimal amount;
}