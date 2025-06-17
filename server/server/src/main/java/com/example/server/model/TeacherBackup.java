package com.example.server.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("Teacher_Backup")
public class TeacherBackup {

    @TableId(value = "BackupRecordId", type = IdType.INPUT)
    private Integer backupRecordId;

    @TableField("TeacherId")
    private Integer teacherId;

    @TableField("Name")
    private String name;

    @TableField("Gender")
    private String gender;

    @TableField("Phone")
    private String phone;

    @TableField("HourlyFee")
    private BigDecimal hourlyFee;
}