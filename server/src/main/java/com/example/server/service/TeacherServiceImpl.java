package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.mapper.TeacherMapper;
import com.example.server.model.Teacher;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl
        extends ServiceImpl<TeacherMapper, Teacher>
        implements TeacherService {
    // 直接继承ServiceImpl即可使用MyBatis-Plus的增强功能
}