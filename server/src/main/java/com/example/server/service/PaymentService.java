package com.example.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.model.Payment;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PaymentService extends IService<Payment> {

    /**
     * 获取特定职业ID的支付记录（按日期倒序）
     * @param occupationId 职业ID
     * @return 支付记录列表
     */
    List<Payment> getByOccupationId(Integer occupationId);

    /**
     * 查询日期范围内的支付记录
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 支付记录列表
     */
    List<Payment> getByDateRange(Date startDate, Date endDate);

    /**
     * 检查特定职业在指定日期是否存在支付记录
     * @param occupationId 职业ID
     * @param date 查询日期
     * @return 是否存在
     */
    boolean existsByOccupationAndDate(Integer occupationId, Date date);

    /**
     * 分页查询支付记录
     */
    Page<Payment> getPaymentsPage(Page<Payment> page,
                                  @Param("params") Map<String, Object> params);
}