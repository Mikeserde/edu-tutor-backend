package com.example.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.server.model.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Mapper
@Transactional(readOnly = true) // 整个接口声明为只读事务
public interface PaymentMapper extends BaseMapper<Payment> {

    // 查询特定职业注册ID的所有支付记录（按日期倒序）
    @Select("SELECT * FROM payment WHERE OccupationId = #{occupationId} ORDER BY PaymentDate DESC")
    List<Payment> findByOccupationId(@Param("occupationId") Integer occupationId);

    // 查询特定日期范围内的支付记录（倒序排列）
    @Select("SELECT * FROM payment WHERE PaymentDate BETWEEN #{startDate} AND #{endDate} ORDER BY PaymentDate DESC")
    List<Payment> findByPaymentDateRange(@Param("startDate") Date startDate,
                                         @Param("endDate") Date endDate);

    // 检查特定职业ID在给定日期是否有支付记录
    @Select("SELECT EXISTS(SELECT 1 FROM payment WHERE OccupationId = #{occupationId} AND PaymentDate = #{date})")
    boolean existsByOccupationAndDate(@Param("occupationId") Integer occupationId,
                                      @Param("date") Date date);
}