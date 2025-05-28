package com.example.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.mapper.StudentMapper;
import com.example.server.model.Student;
import com.example.server.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public boolean isPhoneUnique(String phone, Integer excludeId) {
        return studentMapper.countByPhoneExcludeId(phone, excludeId) == 0;
    }
}