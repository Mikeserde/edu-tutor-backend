package com.example.server.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@TableName("OccupationSchedule_Backup")
public class OccupationScheduleBackup {

    @TableId(value = "BackupRecordId", type = IdType.INPUT)
    private Integer backupRecordId;

    @TableField("ScheduleId")
    private Integer scheduleId;

    @TableField("OccupationId")
    private Integer occupationId;

    @TableField("TeacherId")
    private Integer teacherId;

    @TableField("Date")
    private LocalDate date;

    @TableField("StartTime")
    private LocalTime startTime;

    @TableField("EndTime")
    private LocalTime endTime;
}