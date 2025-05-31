package com.example.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.server.model.Salary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SalaryMapper extends BaseMapper<Salary> {

    @Select("SELECT COUNT(*) FROM Salary WHERE TeacherId = #{teacherId} AND Month = #{month}")
    int existsByTeacherAndMonth(@Param("teacherId") Integer teacherId,
                                @Param("month") String month);

    @Select("SELECT * FROM Salary WHERE TeacherId = #{teacherId} AND Month = #{month}")
    Salary selectByCompositeKey(@Param("teacherId") Integer teacherId,
                                @Param("month") String month);
}