package com.example.server.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("BackupRecord") // 指定表名
public class BackupRecord {

    @TableId(value = "BackupId", type = IdType.AUTO) // 主键字段，自增
    private Integer backupId;

    @TableField(value = "BackupTime", fill = FieldFill.INSERT) // 插入时自动填充
    private LocalDateTime backupTime;

    @TableField(value = "Comment")
    private String comment;
}