package com.example.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.server.model.OccupationSchedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalTime;

@Mapper
public interface OccupationScheduleMapper extends BaseMapper<OccupationSchedule> {

    @Select("SELECT COUNT(*) FROM OccupationRegistration WHERE OccupationTypeId = #{occupationTypeId}")
    Integer countByOccupationTypeId(@Param("occupationTypeId") Integer occupationTypeId);

    @Select("SELECT COUNT(*) FROM OccupationSchedule " +
            "WHERE TeacherId = #{teacherId} " +
            "AND Date = #{date} " +
            "AND ((StartTime < #{endTime} AND EndTime > #{startTime}))")
    int checkTimeConflict(@Param("teacherId") Integer teacherId,
                          @Param("date") LocalDate date,
                          @Param("startTime") LocalTime startTime,
                          @Param("endTime") LocalTime endTime);
}