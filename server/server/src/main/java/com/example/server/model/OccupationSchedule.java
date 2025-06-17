package com.example.server.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@TableName("OccupationSchedule")
public class OccupationSchedule {

    @TableId(value = "ScheduleId", type = IdType.AUTO)
    private Integer scheduleId;

    @NotNull(message = "职业ID不能为空")
    @TableField(value = "OccupationId")
    private Integer occupationId;

    @NotNull(message = "教师ID不能为空")
    @TableField(value = "TeacherId")
    private Integer teacherId;

    @NotNull(message = "日期不能为空")
    @TableField(value = "Date")
    private LocalDate date;

    @NotNull(message = "开始时间不能为空")
    @TableField(value = "StartTime")
    private LocalTime startTime;

    @NotNull(message = "结束时间不能为空")
    @TableField(value = "EndTime")
    private LocalTime endTime;
}