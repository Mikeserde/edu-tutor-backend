package com.example.server.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@TableName("OccupationType")
public class OccupationType {

    @TableId(value = "OccupationTypeId", type = IdType.AUTO)
    private Integer occupationTypeId;

    @NotBlank(message = "职业类型名称不能为空")
    @Size(max = 50, message = "职业类型名称长度不能超过50个字符")
    @TableField(value = "Name")
    private String name;
}