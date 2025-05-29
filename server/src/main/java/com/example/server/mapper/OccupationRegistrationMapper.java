package com.example.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.server.model.OccupationRegistration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OccupationRegistrationMapper extends BaseMapper<OccupationRegistration> {

    @Select("SELECT COUNT(*) FROM OccupationRegistration " +
            "WHERE OccupationTypeId = #{occupationTypeId} AND StudentId = #{studentId} " +
            "AND OccupationId != #{excludeId}")
    int countByTypeAndStudent(@Param("occupationTypeId") Integer occupationTypeId,
                              @Param("studentId") Integer studentId,
                              @Param("excludeId") Integer excludeId);
}