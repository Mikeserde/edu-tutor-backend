package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.mapper.SalaryMapper;
import com.example.server.model.Salary;
import com.example.server.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalaryServiceImpl extends ServiceImpl<SalaryMapper, Salary> implements SalaryService {

    @Autowired
    private SalaryMapper salaryMapper;

    @Override
    public boolean existsByTeacherAndMonth(Integer teacherId, String month) {
        return salaryMapper.existsByTeacherAndMonth(teacherId, month) > 0;
    }

    @Override
    public Salary getByCompositeKey(Integer teacherId, String month) {
        return salaryMapper.selectByCompositeKey(teacherId, month);
    }
}