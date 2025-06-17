package com.example.server.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@TableName("Teacher")
public class Teacher {

    @TableId(value = "TeacherId", type = IdType.AUTO)
    private Integer teacherId;

    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    @TableField(value = "Name")
    private String name;

    @NotBlank(message = "性别不能为空")
    @Pattern(regexp = "男|女", message = "性别只能是男或女")
    @TableField(value = "Gender")
    private String gender;

    @NotBlank(message = "电话不能为空")
    @Size(max = 20, message = "电话长度不能超过20个字符")
    @TableField(value = "Phone")
    private String phone;

    @DecimalMin(value = "0.01", message = "课时费必须大于0")
    @TableField(value = "HourlyFee")
    private BigDecimal hourlyFee;
}