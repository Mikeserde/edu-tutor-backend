package com.example.server.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("payment") // 指定对应的数据库表名
public class Payment {

    @TableId(value = "PaymentId", type = IdType.AUTO) // 主键自增
    private Integer paymentId;

    @NotNull(message = "OccupationID不能为空")
    @Positive(message = "OccupationID必须为正数")
    @TableField("OccupationId")
    private Integer occupationId;

    @NotNull(message = "支付日期不能为空")
    @PastOrPresent(message = "支付日期不能是未来日期")
    @TableField("PaymentDate")
    private Date paymentDate;

    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0.00", inclusive = false, message = "金额必须大于0")
    @Digits(integer = 8, fraction = 2, message = "金额格式应为00.00")
    @TableField("Amount")
    private BigDecimal amount;
}