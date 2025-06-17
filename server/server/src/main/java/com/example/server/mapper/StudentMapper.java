package com.example.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.server.model.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    @Select("SELECT COUNT(*) FROM Student WHERE ContactPhone = #{phone} AND StudentId != #{excludeId}")
    int countByPhoneExcludeId(@Param("phone") String phone, @Param("excludeId") Integer excludeId);
}