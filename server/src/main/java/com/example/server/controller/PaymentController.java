package com.example.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.server.model.Payment;
import com.example.server.service.PaymentService;
import com.example.server.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/payments")
@Tag(name = "支付记录管理", description = "支付记录查询接口")
@CrossOrigin
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Operation(summary = "获取特定职业ID的支付记录")
    @GetMapping("/by-occupation/{occupationId}")
    public Result getByOccupation(
            @PathVariable
            @NotNull(message = "职业ID不能为空")
            @Positive(message = "职业ID必须为正整数")
            Integer occupationId) {

        List<Payment> payments = paymentService.getByOccupationId(occupationId);
        if (payments.isEmpty()) {
            return Result.ok().message("未找到相关支付记录");
        }
        return Result.ok().data("payments", payments);
    }

    @Operation(summary = "按日期范围查询支付记录")
    @GetMapping("/by-date-range")
    public Result getByDateRange(
            @RequestParam @NotNull(message = "开始日期不能为空")
            @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "日期格式必须为YYYY-MM-DD")
            String startDate,

            @RequestParam @NotNull(message = "结束日期不能为空")
            @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "日期格式必须为YYYY-MM-DD")
            String endDate) {

        try {
            Date start = Date.valueOf(startDate);
            Date end = Date.valueOf(endDate);
            List<Payment> payments = paymentService.getByDateRange(start, end);
            return Result.ok().data("payments", payments);
        } catch (IllegalArgumentException e) {
            return Result.error().code(40000).message("日期格式无效");
        }
    }

    @Operation(summary = "检查支付记录是否存在")
    @GetMapping("/existence")
    public Result checkPaymentExists(
            @RequestParam @NotNull(message = "职业ID不能为空") Integer occupationId,
            @RequestParam @NotNull(message = "查询日期不能为空")
            @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "日期格式必须为YYYY-MM-DD")
            String date) {

        try {
            boolean exists = paymentService.existsByOccupationAndDate(
                    occupationId,
                    Date.valueOf(date)
            );
            return Result.ok().data("exists", exists);
        } catch (IllegalArgumentException e) {
            return Result.error().code(40000).message("日期格式无效");
        }
    }

    @Operation(summary = "分页查询支付记录")
    @GetMapping
    public Result getPaymentsPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer occupationId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
            ) {

        Page<Payment> page = new Page<>(pageNum, pageSize);
        Map<String, Object> params = new HashMap<>();

        if (occupationId != null) {
            params.put("occupationId", occupationId);
        }
        if (startDate != null && endDate != null) {
            try {
                params.put("startDate", Date.valueOf(startDate));
                params.put("endDate", Date.valueOf(endDate));
            } catch (IllegalArgumentException e) {
                return Result.error().code(40000).message("日期格式无效");
            }
        }

        Page<Payment> resultPage = paymentService.getPaymentsPage(page, params);
        return Result.ok().data("page", resultPage);
    }
}