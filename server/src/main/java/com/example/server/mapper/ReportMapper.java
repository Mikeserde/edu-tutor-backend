package com.example.server.mapper;

import com.example.server.model.OccupationDemandDTO;
import com.example.server.model.TeacherHoursDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ReportMapper {
    List<TeacherHoursDTO> calculateTeacherHours(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // 统计职业需求次数
    List<OccupationDemandDTO> countOccupationDemand();
}
