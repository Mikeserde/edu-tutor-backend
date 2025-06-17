package com.example.server.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("Student_Backup")
public class StudentBackup {

    @TableId(value = "BackupRecordId", type = IdType.INPUT)
    private Integer backupRecordId;

    @TableField("StudentId")
    private Integer studentId;

    @TableField("Name")
    private String name;

    @TableField("Gender")
    private String gender;

    @TableField("Address")
    private String address;

    @TableField("ContactPhone")
    private String contactPhone;
}