package com.example.server.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@TableName("Salary")
public class Salary {

    @NotNull(message = "教师ID不能为空")
    @TableField("TeacherId")
    private Integer teacherId;

    @NotBlank(message = "月份不能为空")
    @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "月份格式应为YYYY-MM")
    @TableField("Month")
    private String month;

    @DecimalMin(value = "0.00", message = "总工时不能小于0")
    @Digits(integer = 8, fraction = 2, message = "总工时格式不正确")
    @TableField("TotalHours")
    private BigDecimal totalHours;

    @DecimalMin(value = "0.00", message = "总金额不能小于0")
    @Digits(integer = 8, fraction = 2, message = "总金额格式不正确")
    @TableField("TotalAmount")
    private BigDecimal totalAmount;
}