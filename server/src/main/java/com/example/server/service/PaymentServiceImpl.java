package com.example.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.mapper.PaymentMapper;
import com.example.server.model.Payment;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment>
        implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public List<Payment> getByOccupationId(Integer occupationId) {
        return paymentMapper.findByOccupationId(occupationId);
    }

    @Override
    public List<Payment> getByDateRange(Date startDate, Date endDate) {
        return paymentMapper.findByPaymentDateRange(startDate, endDate);
    }

    @Override
    public boolean existsByOccupationAndDate(Integer occupationId, Date date) {
        return paymentMapper.existsByOccupationAndDate(occupationId, date);
    }

    @Override
    public Page<Payment> getPaymentsPage(Page<Payment> page,
                                         @Param("params") Map<String, Object> params) {

        QueryWrapper<Payment> wrapper = new QueryWrapper<>();

        // 按职业ID过滤
        if (params.containsKey("occupationId")) {
            wrapper.eq("OccupationId", params.get("occupationId"));
        }

        // 按日期范围过滤
        if (params.containsKey("startDate") && params.containsKey("endDate")) {
            wrapper.between("PaymentDate", params.get("startDate"), params.get("endDate"));
        }

        // 默认按支付日期倒序
        wrapper.orderByDesc("PaymentDate");

        return this.page(page, wrapper);
    }
}