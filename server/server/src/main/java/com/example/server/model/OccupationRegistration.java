package com.example.server.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@TableName("OccupationRegistration")
public class OccupationRegistration {

    @TableId(value = "OccupationId", type = IdType.AUTO)
    private Integer occupationId;

    @NotNull(message = "职业类型ID不能为空")
    @TableField("OccupationTypeId")
    private Integer occupationTypeId;

    @NotNull(message = "学生ID不能为空")
    @TableField("StudentId")
    private Integer studentId;
}