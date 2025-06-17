package com.example.server.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("OccupationRegistration_Backup")
public class OccupationRegistrationBackup {

    @TableId(value = "BackupRecordId", type = IdType.INPUT)
    private Integer backupRecordId;

    @TableField("OccupationId")
    private Integer occupationId;

    @TableField("OccupationTypeId")
    private Integer occupationTypeId;

    @TableField("StudentId")
    private Integer studentId;
}