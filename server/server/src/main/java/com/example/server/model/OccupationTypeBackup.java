package com.example.server.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("OccupationType_Backup")
public class OccupationTypeBackup {

    @TableId(value = "BackupRecordId", type = IdType.INPUT)
    private Integer backupRecordId;

    @TableField("OccupationTypeId")
    private Integer occupationTypeId;

    @TableField("Name")
    private String name;
}